import React from "react";
import { useNavigate, useParams } from "react-router";
import {
    Box,
    Card,
    CardContent,
    Chip,
    Divider,
    Grid,
    Paper,
    Stack,
    Typography
} from "@mui/material";
import {
    History as HistoryIcon,
    Badge,
    Person,
    Assignment,
    EditNote,
    Notes,
    AccessTime
} from "@mui/icons-material";

import AsyncDataView from "../../../../common/ui/components/AsyncDataView.jsx";
import ActionBar from "../../../../common/ui/components/ActionBar.jsx";
import useRequestLogDetails from "../../../hooks/requestLogs/useRequestLogDetails.js";

const getActionColor = (action) => {
    switch (action) {
        case "CREATED":
            return "success";
        case "UPDATED":
            return "info";
        case "STATUS_CHANGED":
            return "warning";
        case "ROUTING_CHANGED":
            return "secondary";
        case "DELETED":
            return "error";
        default:
            return "default";
    }
};

const formatLabel = (value) => {
    if (!value) return "N/A";

    return value
        .replaceAll("_", " ")
        .toLowerCase()
        .replace(/\b\w/g, char => char.toUpperCase());
};

const formatDateTime = (value) => {
    if (!value) return "N/A";

    return new Date(value).toLocaleString();
};

const RequestLogDetailsPage = () => {
    const { id, requestId } = useParams();
    const navigate = useNavigate();

    const { requestLog, loading, error } = useRequestLogDetails(id, requestId);

    if (loading || error || !requestLog) {
        return (
            <AsyncDataView
                loading={loading}
                error={error}
                data={requestLog}
                notFoundMessage="Request log not found."
                backUrl={`/requests/${requestId}`}
            />
        );
    }

    return (
        <Box>
            <ActionBar
                onSecondaryBack={() => navigate(`/requests/${requestId}`)}
                secondaryBackLabel="Back to Request"
            />

            <Paper elevation={2} sx={{ p: 4, borderRadius: 4 }}>
                <Stack direction="row" spacing={2} alignItems="center" sx={{ mb: 2 }}>
                    <HistoryIcon color="primary" sx={{ fontSize: 40 }} />

                    <Box sx={{ flexGrow: 1 }}>
                        <Typography variant="h4" fontWeight={600}>
                            Request Log Details #{requestId}
                        </Typography>

                        <Stack direction="row" spacing={1} sx={{ mt: 1 }}>
                            <Chip
                                label={formatLabel(requestLog.action)}
                                color={getActionColor(requestLog.action)}
                                size="small"
                            />

                            <Chip
                                label={`Log ID: ${requestLog.id}`}
                                variant="outlined"
                                size="small"
                            />
                        </Stack>
                    </Box>
                </Stack>

                <Divider sx={{ mb: 3 }} />

                <Grid container spacing={3}>
                    <Grid size={{ xs: 12, md: 4 }}>
                        <Card variant="outlined" sx={{ height: "100%" }}>
                            <CardContent>
                                <Typography variant="h6" sx={{ mb: 2 }}>
                                    Basic Information
                                </Typography>

                                <Stack spacing={2}>
                                    <Stack direction="row" spacing={1} alignItems="center">
                                        <Badge color="primary" />
                                        <Typography>
                                            <strong>Log ID:</strong> {requestLog.id}
                                        </Typography>
                                    </Stack>

                                    <Stack direction="row" spacing={1} alignItems="center">
                                        <Assignment color="primary" />
                                        <Typography>
                                            <strong>Request ID:</strong> {requestLog.requestId}
                                        </Typography>
                                    </Stack>

                                    <Stack direction="row" spacing={1} alignItems="center">
                                        <Person color="primary" />
                                        <Typography>
                                            <strong>User ID:</strong> {requestLog.userId ?? "System"}
                                        </Typography>
                                    </Stack>

                                    <Stack direction="row" spacing={1} alignItems="center">
                                        <AccessTime color="primary" />
                                        <Typography>
                                            <strong>Created At:</strong> {formatDateTime(requestLog.createdAt)}
                                        </Typography>
                                    </Stack>
                                </Stack>
                            </CardContent>
                        </Card>
                    </Grid>

                    <Grid size={{ xs: 12, md: 4 }}>
                        <Card variant="outlined" sx={{ height: "100%" }}>
                            <CardContent>
                                <Typography variant="h6" sx={{ mb: 2 }}>
                                    Change Details
                                </Typography>

                                <Stack spacing={2}>
                                    <Stack direction="row" spacing={1} alignItems="center">
                                        <EditNote color="primary" />
                                        <Typography>
                                            <strong>Action:</strong> {formatLabel(requestLog.action)}
                                        </Typography>
                                    </Stack>

                                    <Box>
                                        <Typography variant="subtitle2" color="text.secondary">
                                            Old Value
                                        </Typography>
                                        <Typography variant="body2">
                                            {requestLog.oldValue || "N/A"}
                                        </Typography>
                                    </Box>

                                    <Box>
                                        <Typography variant="subtitle2" color="text.secondary">
                                            New Value
                                        </Typography>
                                        <Typography variant="body2">
                                            {requestLog.newValue || "N/A"}
                                        </Typography>
                                    </Box>
                                </Stack>
                            </CardContent>
                        </Card>
                    </Grid>

                    <Grid size={{ xs: 12, md: 4 }}>
                        <Card variant="outlined" sx={{ height: "100%" }}>
                            <CardContent>
                                <Typography variant="h6" sx={{ mb: 2 }}>
                                    Note
                                </Typography>

                                <Stack direction="row" spacing={1} alignItems="flex-start">
                                    <Notes color="primary" />

                                    <Typography variant="body2">
                                        {requestLog.note || "No note provided."}
                                    </Typography>
                                </Stack>
                            </CardContent>
                        </Card>
                    </Grid>
                </Grid>
            </Paper>
        </Box>
    );
};

export default RequestLogDetailsPage;