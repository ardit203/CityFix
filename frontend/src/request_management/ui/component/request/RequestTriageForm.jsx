import React, { useEffect, useState } from "react";
import {
    Box,
    Button,
    Card,
    CardContent,
    FormControl,
    InputLabel,
    MenuItem,
    Select,
    Stack,
    TextField,
    Typography,
    Alert,
    CircularProgress,
    Divider
} from "@mui/material";
import { AutoAwesome as AutoAwesomeIcon, CheckCircle as CheckCircleIcon, Cancel as CancelIcon } from "@mui/icons-material";

import useAiSuggestionDetails from "../../../hooks/request/useAiSuggestionDetails.js";
import useAiSuggestionActions from "../../../hooks/request/useAiSuggestionActions.js";
import useRequestActions from "../../../hooks/request/useRequestActions.js";

import useCategories from "../../../../administration/hooks/category/useCategories.js";

const RequestTriageForm = ({ requestId, onTriageComplete }) => {
    const { suggestion, loading: suggestionLoading, error: suggestionError } = useAiSuggestionDetails(requestId);
    const { processSuggestion, rejectSuggestion, isExecuting: isAiExecuting } = useAiSuggestionActions();
    const { confirmRouting, isExecuting: isRoutingExecuting } = useRequestActions();
    const { categories } = useCategories({ paged: false });

    const [categoryId, setCategoryId] = useState('');
    const [priority, setPriority] = useState('');
    const [summary, setSummary] = useState('');
    const [rejectReason, setRejectReason] = useState('');

    // Pre-fill form when suggestion loads
    useEffect(() => {
        if (suggestion && suggestion.status === 'PENDING_REVIEW') {
            // eslint-disable-next-line react-hooks/set-state-in-effect
            setCategoryId(suggestion.categoryId || '');
            setPriority(suggestion.priority || '');
            setSummary(suggestion.aiSummary || '');
        }
    }, [suggestion]);

    const handleConfirmAndRoute = async () => {
        if (!categoryId || !priority) return;
        
        // 1. Process the AI suggestion (which also updates the Request entity)
        await processSuggestion(requestId, {
            categoryId,
            priority,
            summary
        }, async () => {
            // 2. On success, officially confirm the routing
            await confirmRouting(requestId, () => {
                if (onTriageComplete) onTriageComplete();
            });
        });
    };

    const handleRejectRequest = async () => {
        if (!rejectReason.trim()) return;

        await rejectSuggestion(requestId, { reason: rejectReason }, () => {
             setRejectReason('');
             if (onTriageComplete) onTriageComplete();
        });
    };

    if (suggestionLoading) {
        return <CircularProgress />;
    }

    if (suggestionError || !suggestion) {
        return <Alert severity="warning">No pending AI suggestion found for this request.</Alert>;
    }

    console.log(suggestion)
    if (suggestion.status === 'NOT_GENERATED') {
        return <Alert severity="warning">AI could not generate a suggestion for this request. Please review the request manually and choose the category and priority yourself.</Alert>;
    }

    if (suggestion.status !== 'PENDING_REVIEW') {
         return <Alert severity="info">This AI suggestion has already been {suggestion.status}.</Alert>;
    }

    const isExecuting = isAiExecuting || isRoutingExecuting;

    return (
        <Card
            variant="outlined"
            className="entity-card"
            sx={{ height: '100%', borderColor: 'primary.main', borderWidth: 1 }}
        >
            <CardContent>
                <Stack direction="row" spacing={1} alignItems="center" sx={{ mb: 2 }}>
                    <Box className="entity-card-icon" sx={{width: 40, height: 40, borderRadius: 2}}>
                        <AutoAwesomeIcon />
                    </Box>
                    <Box>
                        <Typography variant="h6" color="primary.main">
                            AI Triage Assistant
                        </Typography>
                        <Typography variant="body2" color="text.secondary">
                            Suggested routing review
                        </Typography>
                    </Box>
                </Stack>

                <Alert severity="info" sx={{ mb: 3 }}>
                    Review the suggested category, priority, and summary. Confirming applies the values to the request and marks routing as confirmed.
                </Alert>

                <Stack spacing={3}>
                    <FormControl fullWidth>
                        <InputLabel>Category</InputLabel>
                        <Select
                            value={categoryId}
                            label="Category"
                            onChange={(e) => setCategoryId(e.target.value)}
                            disabled={isExecuting}
                        >
                            {categories.map((cat) => (
                                <MenuItem key={cat.id} value={cat.id}>
                                    {cat.name}
                                </MenuItem>
                            ))}
                        </Select>
                    </FormControl>

                    <FormControl fullWidth>
                        <InputLabel>Priority</InputLabel>
                        <Select
                            value={priority}
                            label="Priority"
                            onChange={(e) => setPriority(e.target.value)}
                            disabled={isExecuting}
                        >
                            <MenuItem value="LOW">LOW</MenuItem>
                            <MenuItem value="MEDIUM">MEDIUM</MenuItem>
                            <MenuItem value="HIGH">HIGH</MenuItem>
                        </Select>
                    </FormControl>

                    <TextField
                        fullWidth
                        multiline
                        minRows={4}
                        label="Summary"
                        value={summary}
                        onChange={(e) => setSummary(e.target.value)}
                        disabled={isExecuting}
                    />

                    <Button
                        variant="contained"
                        color="primary"
                        startIcon={<CheckCircleIcon />}
                        onClick={handleConfirmAndRoute}
                        disabled={isExecuting || !categoryId || !priority}
                    >
                        Confirm & Route
                    </Button>

                    <Divider />

                    <TextField
                        fullWidth
                        multiline
                        minRows={2}
                        label="Reason for rejecting suggestion"
                        value={rejectReason}
                        onChange={(e) => setRejectReason(e.target.value)}
                        disabled={isExecuting}
                    />

                    <Stack direction="row" spacing={2} justifyContent="flex-end">
                        <Button
                            variant="outlined"
                            color="error"
                            startIcon={<CancelIcon />}
                            onClick={handleRejectRequest}
                            disabled={isExecuting || !rejectReason.trim()}
                        >
                            Reject Suggestion
                        </Button>
                    </Stack>
                </Stack>
            </CardContent>
        </Card>
    );
};

export default RequestTriageForm;
