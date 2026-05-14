import React, {useEffect, useState} from "react";
import {useNavigate, useParams} from "react-router";
import {
    Alert,
    Box,
    Button,
    Card,
    CardContent,
    Chip,
    Divider,
    FormControl,
    Grid,
    InputLabel,
    MenuItem,
    Paper,
    Select,
    Stack,
    TextField,
    Typography
} from "@mui/material";
import {
    AssignmentTurnedIn,
    CheckCircle,
    Route as RouteIcon,
    Save,
    Undo
} from "@mui/icons-material";

import useAuth from "../../../../auth_and_access/hooks/auth/useAuth.js";
import useRequestActions from "../../../hooks/request/useRequestActions.js";
import useRequestDetails from "../../../hooks/request/useRequestDetails.js";
import useAiSuggestionDetails from "../../../hooks/request/useAiSuggestionDetails.js";
import useCategories from "../../../../administration/hooks/category/useCategories.js";
import useMunicipalities from "../../../../administration/hooks/municipality/useMunicipalities.js";

import ActionBar from "../../../../common/ui/components/ActionBar.jsx";
import AsyncDataView from "../../../../common/ui/components/AsyncDataView.jsx";
import RequestTriageForm from "../../component/request/RequestTriageForm.jsx";

const priorityOptions = ["LOW", "MEDIUM", "HIGH"];
const statusOptions = [
    "SUBMITTED",
    "IN_REVIEW",
    "ASSIGNED",
    "IN_PROGRESS",
    "RESOLVED",
    "REJECTED",
    "CANCELED"
];

const staffRoles = ["ADMINISTRATOR", "MANAGER", "EMPLOYEE"];

const getUserRoles = (user) => {
    const roles = user?.roles || [];

    return roles.map((role) => {
        const roleName = typeof role === "string" ? role : role.authority;
        return roleName?.replace("ROLE_", "");
    }).filter(Boolean);
};

const formatLabel = (value) => {
    if (!value) return "Not set";

    return value
        .replaceAll("_", " ")
        .toLowerCase()
        .replace(/\b\w/g, (char) => char.toUpperCase());
};

const RequestEditPageContent = () => {
    const {id} = useParams();
    const navigate = useNavigate();

    const {request, loading, error, fetchRequest} = useRequestDetails(id);
    const {suggestion: aiSuggestion} = useAiSuggestionDetails(id);
    const {
        changeStatus,
        confirmRouting,
        rejectRouting,
        reopenRouting,
        updateRouting,
        isExecuting
    } = useRequestActions();

    const {categories, loading: loadingCategories} = useCategories({paged: false});
    const {municipalities, loading: loadingMunicipalities} = useMunicipalities({paged: false});

    const [routingForm, setRoutingForm] = useState({
        categoryId: "",
        departmentId: "",
        municipalityId: "",
        priority: "",
        summary: "",
        note: ""
    });
    const [routingRejectReason, setRoutingRejectReason] = useState("");
    const [statusForm, setStatusForm] = useState({
        status: "",
        reason: "",
        note: ""
    });

    useEffect(() => {
        if (!request) return;

        // eslint-disable-next-line react-hooks/set-state-in-effect
        setRoutingForm({
            categoryId: request.categoryId || "",
            departmentId: request.departmentId || "",
            municipalityId: request.municipalityId || "",
            priority: request.priority || "",
            summary: request.summary || "",
            note: ""
        });
        setStatusForm({
            status: request.status || "",
            reason: "",
            note: ""
        });
    }, [request]);

    const handleRoutingChange = (event) => {
        const {name, value} = event.target;
        setRoutingForm((prev) => ({...prev, [name]: value}));
    };

    const handleStatusChange = (event) => {
        const {name, value} = event.target;
        setStatusForm((prev) => ({...prev, [name]: value}));
    };

    const handleUpdateRouting = async () => {
        await updateRouting(request.id, {
            ...routingForm,
            categoryId: routingForm.categoryId || null,
            departmentId: routingForm.departmentId || null,
            municipalityId: routingForm.municipalityId || null,
            priority: routingForm.priority || null,
            summary: routingForm.summary,
            note: routingForm.note
        }, fetchRequest);
    };

    const handleConfirmRouting = async () => {
        await confirmRouting(request.id, fetchRequest);
    };

    const handleRejectRouting = async () => {
        if (!routingRejectReason.trim()) return;

        await rejectRouting(request.id, {reason: routingRejectReason}, () => {
            setRoutingRejectReason("");
            fetchRequest();
        });
    };

    const handleReopenRouting = async () => {
        await reopenRouting(request.id, fetchRequest);
    };

    const handleChangeStatus = async () => {
        if (!statusForm.status) return;

        await changeStatus(request.id, statusForm, fetchRequest);
    };

    if (loading || error || !request) {
        return (
            <AsyncDataView
                loading={loading}
                error={error}
                data={request}
                notFoundMessage="Request not found."
                backUrl="/requests"
            />
        );
    }

    const isAiSuggestionPending = (aiSuggestion?.status === "PENDING_REVIEW" || aiSuggestion?.status === "NOT_GENERATED");
    const disableManualEditing = isExecuting || isAiSuggestionPending;

    return (
        <Box>
            <ActionBar
                onBack={() => navigate(`/requests/${request.id}`)}
                backLabel="Back to Details"
                onSecondaryBack={() => navigate("/requests")}
                secondaryBackLabel="Back to Requests"
            />

            <Stack spacing={3}>
                <Paper elevation={2} sx={{p: {xs: 2, md: 4}, borderRadius: 3}}>
                    <Stack spacing={2}>
                        <Stack direction={{xs: "column", md: "row"}} justifyContent="space-between" spacing={2}>
                            <Box>
                                <Typography variant="h4" fontWeight={700}>
                                    Edit Request #{request.id}
                                </Typography>
                                <Typography variant="body1" color="text.secondary">
                                    {request.title || "Untitled Request"}
                                </Typography>
                            </Box>

                            <Stack direction="row" spacing={1} flexWrap="wrap" useFlexGap>
                                <Chip label={formatLabel(request.status)} color="primary" variant="outlined" />
                                <Chip label={formatLabel(request.routingStatus)} color="warning" variant="outlined" />
                            </Stack>
                        </Stack>

                        <Alert severity="info">
                            Use this page for staff decisions: triage, routing, and status changes. Request text and citizen-submitted details stay read-only on the details page.
                        </Alert>
                    </Stack>
                </Paper>

                <Grid container spacing={3}>
                    <Grid size={{xs: 12, lg: 6}}>
                        {request.routingStatus === "PENDING_REVIEW" ? (
                            <RequestTriageForm requestId={request.id} onTriageComplete={fetchRequest} />
                        ) : (
                            <Card variant="outlined" sx={{height: "100%", borderRadius: 2}}>
                                <CardContent>
                                    <Stack spacing={2}>
                                        <Stack direction="row" spacing={1} alignItems="center">
                                            <AssignmentTurnedIn color="success" />
                                            <Typography variant="h6" fontWeight={700}>
                                                AI Triage
                                            </Typography>
                                        </Stack>
                                        <Alert severity="success">
                                            AI triage is not pending. Reopen routing if this request needs another triage pass.
                                        </Alert>
                                        <Button
                                            variant="outlined"
                                            color="warning"
                                            startIcon={<Undo />}
                                            onClick={handleReopenRouting}
                                            disabled={isExecuting}
                                        >
                                            Reopen Routing
                                        </Button>
                                    </Stack>
                                </CardContent>
                            </Card>
                        )}
                    </Grid>

                    <Grid size={{xs: 12, lg: 6}}>
                        <Card variant="outlined" sx={{height: "100%", borderRadius: 2}}>
                            <CardContent>
                                <Stack spacing={2.5}>
                                    <Stack direction="row" spacing={1} alignItems="center">
                                        <RouteIcon color="primary" />
                                        <Typography variant="h6" fontWeight={700}>
                                            Routing Management
                                        </Typography>
                                    </Stack>

                                    {isAiSuggestionPending && (
                                        <Alert severity="warning">
                                            Process the pending AI triage before changing routing manually.
                                        </Alert>
                                    )}

                                    <Grid container spacing={2}>
                                        <Grid size={{xs: 12, md: 6}}>
                                            <FormControl fullWidth>
                                                <InputLabel>Category</InputLabel>
                                                <Select
                                                    name="categoryId"
                                                    value={routingForm.categoryId}
                                                    label="Category"
                                                    onChange={handleRoutingChange}
                                                    disabled={disableManualEditing || loadingCategories}
                                                >
                                                    <MenuItem value=""><em>Unassigned</em></MenuItem>
                                                    {categories.map((category) => (
                                                        <MenuItem key={category.id} value={category.id}>
                                                            {category.name}
                                                        </MenuItem>
                                                    ))}
                                                </Select>
                                            </FormControl>
                                        </Grid>

                                        {/*<Grid size={{xs: 12, md: 6}}>*/}
                                        {/*    <FormControl fullWidth>*/}
                                        {/*        <InputLabel>Department</InputLabel>*/}
                                        {/*        <Select*/}
                                        {/*            name="departmentId"*/}
                                        {/*            value={routingForm.departmentId}*/}
                                        {/*            label="Department"*/}
                                        {/*            onChange={handleRoutingChange}*/}
                                        {/*            disabled={isExecuting || loadingDepartments}*/}
                                        {/*        >*/}
                                        {/*            <MenuItem value=""><em>Unassigned</em></MenuItem>*/}
                                        {/*            {departments.map((department) => (*/}
                                        {/*                <MenuItem key={department.id} value={department.id}>*/}
                                        {/*                    {department.name}*/}
                                        {/*                </MenuItem>*/}
                                        {/*            ))}*/}
                                        {/*        </Select>*/}
                                        {/*    </FormControl>*/}
                                        {/*</Grid>*/}

                                        <Grid size={{xs: 12, md: 6}}>
                                            <FormControl fullWidth>
                                                <InputLabel>Municipality</InputLabel>
                                                <Select
                                                    name="municipalityId"
                                                    value={routingForm.municipalityId}
                                                    label="Municipality"
                                                    onChange={handleRoutingChange}
                                                    disabled={disableManualEditing || loadingMunicipalities}
                                                >
                                                    <MenuItem value=""><em>Unassigned</em></MenuItem>
                                                    {municipalities.map((municipality) => (
                                                        <MenuItem key={municipality.id} value={municipality.id}>
                                                            {municipality.name || municipality.code}
                                                        </MenuItem>
                                                    ))}
                                                </Select>
                                            </FormControl>
                                        </Grid>

                                        <Grid size={{xs: 12, md: 6}}>
                                            <FormControl fullWidth>
                                                <InputLabel>Priority</InputLabel>
                                                <Select
                                                    name="priority"
                                                    value={routingForm.priority}
                                                    label="Priority"
                                                    onChange={handleRoutingChange}
                                                    disabled={disableManualEditing}
                                                >
                                                    <MenuItem value=""><em>Not set</em></MenuItem>
                                                    {priorityOptions.map((priority) => (
                                                        <MenuItem key={priority} value={priority}>
                                                            {formatLabel(priority)}
                                                        </MenuItem>
                                                    ))}
                                                </Select>
                                            </FormControl>
                                        </Grid>
                                    </Grid>

                                    <TextField
                                        name="summary"
                                        label="AI / Staff Summary"
                                        value={routingForm.summary}
                                        onChange={handleRoutingChange}
                                        multiline
                                        minRows={3}
                                        disabled={disableManualEditing}
                                        fullWidth
                                    />

                                    <TextField
                                        name="note"
                                        label="Routing note for timeline"
                                        value={routingForm.note}
                                        onChange={handleRoutingChange}
                                        multiline
                                        minRows={2}
                                        disabled={disableManualEditing}
                                        fullWidth
                                    />

                                    <Stack direction={{xs: "column", sm: "row"}} spacing={1.5}>
                                        <Button
                                            variant="contained"
                                            startIcon={<Save />}
                                            onClick={handleUpdateRouting}
                                            disabled={disableManualEditing}
                                        >
                                            Save Routing
                                        </Button>
                                        <Button
                                            variant="outlined"
                                            color="success"
                                            startIcon={<CheckCircle />}
                                            onClick={handleConfirmRouting}
                                            disabled={disableManualEditing || request.routingStatus === "CONFIRMED"}
                                        >
                                            Confirm Routing
                                        </Button>
                                    </Stack>

                                    <Divider />

                                    <Stack spacing={1.5}>
                                        <TextField
                                            label="Reason for rejecting routing"
                                            value={routingRejectReason}
                                            onChange={(event) => setRoutingRejectReason(event.target.value)}
                                            multiline
                                            minRows={2}
                                            disabled={disableManualEditing}
                                            fullWidth
                                        />
                                        <Button
                                            variant="outlined"
                                            color="error"
                                            onClick={handleRejectRouting}
                                            disabled={disableManualEditing || !routingRejectReason.trim() || request.routingStatus === "REJECTED"}
                                        >
                                            Reject Routing
                                        </Button>
                                    </Stack>
                                </Stack>
                            </CardContent>
                        </Card>
                    </Grid>

                    <Grid size={{xs: 12}}>
                        <Card variant="outlined" sx={{borderRadius: 2}}>
                            <CardContent>
                                <Stack spacing={2.5}>
                                    <Typography variant="h6" fontWeight={700}>
                                        Status Change
                                    </Typography>

                                    {isAiSuggestionPending && (
                                        <Alert severity="warning">
                                            Status changes are disabled until the pending AI triage is processed.
                                        </Alert>
                                    )}

                                    <Grid container spacing={2}>
                                        <Grid size={{xs: 12, md: 4}}>
                                            <FormControl fullWidth>
                                                <InputLabel>Status</InputLabel>
                                                <Select
                                                    name="status"
                                                    value={statusForm.status}
                                                    label="Status"
                                                    onChange={handleStatusChange}
                                                    disabled={disableManualEditing}
                                                >
                                                    {statusOptions.map((status) => (
                                                        <MenuItem key={status} value={status}>
                                                            {formatLabel(status)}
                                                        </MenuItem>
                                                    ))}
                                                </Select>
                                            </FormControl>
                                        </Grid>

                                        <Grid size={{xs: 12, md: 4}}>
                                            <TextField
                                                name="reason"
                                                label="Reason"
                                                value={statusForm.reason}
                                                onChange={handleStatusChange}
                                                disabled={disableManualEditing}
                                                fullWidth
                                            />
                                        </Grid>

                                        <Grid size={{xs: 12, md: 4}}>
                                            <TextField
                                                name="note"
                                                label="Timeline note"
                                                value={statusForm.note}
                                                onChange={handleStatusChange}
                                                disabled={disableManualEditing}
                                                fullWidth
                                            />
                                        </Grid>
                                    </Grid>

                                    <Box>
                                        <Button
                                            variant="contained"
                                            startIcon={<Save />}
                                            onClick={handleChangeStatus}
                                            disabled={disableManualEditing || !statusForm.status || statusForm.status === request.status}
                                        >
                                            Update Status
                                        </Button>
                                    </Box>
                                </Stack>
                            </CardContent>
                        </Card>
                    </Grid>
                </Grid>
            </Stack>
        </Box>
    );
};

const RequestEditPage = () => {
    const {user, loading} = useAuth();
    const canEdit = getUserRoles(user).some((role) => staffRoles.includes(role));

    if (loading) return null;

    if (!canEdit) {
        return (
            <Alert severity="warning">
                You do not have permission to edit request triage or routing.
            </Alert>
        );
    }

    return <RequestEditPageContent />;
};

export default RequestEditPage;
