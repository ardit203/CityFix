import React from "react";
import {useNavigate, useParams} from "react-router";
import {
    Alert,
    Box,
    Button,
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
    Badge,
    Business,
    Category as CategoryIcon,
    Description,
    Download,
    LocationCity,
    LocationOn,
    Person,
    Psychology,
    ReportProblem
} from "@mui/icons-material";

import useAuth from "../../../../auth_and_access/hooks/auth/useAuth.js";
import useRequestActions from "../../../hooks/request/useRequestActions.js";
import useRequestDetails from "../../../hooks/request/useRequestDetails.js";
import useCategories from "../../../../administration/hooks/category/useCategories.js";
import useDepartments from "../../../../administration/hooks/department/useDepartments.js";
import useMunicipalities from "../../../../administration/hooks/municipality/useMunicipalities.js";

import AsyncDataView from "../../../../common/ui/components/AsyncDataView.jsx";
import ActionBar from "../../../../common/ui/components/ActionBar.jsx";
import RequestCommentsSection from "../../component/request/RequestCommentsSection.jsx";
import RequestAssignmentPanel from "../../component/requestAssignments/RequestAssignmentPanel.jsx";
import LocationPicker from "../../component/request/LocationPicker.jsx";
import RequireRole from "../../../../auth_and_access/ui/components/auth/RequireRole.jsx";

const staffRoles = ["ADMINISTRATOR", "MANAGER", "EMPLOYEE"];
const managementRoles = ["ADMINISTRATOR", "MANAGER"];

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

const getPriorityColor = (priority) => {
    switch (priority) {
        case "HIGH": return "error";
        case "MEDIUM": return "warning";
        case "LOW": return "info";
        default: return "default";
    }
};

const getStatusColor = (status) => {
    switch (status) {
        case "SUBMITTED": return "info";
        case "IN_REVIEW": return "warning";
        case "ASSIGNED": return "secondary";
        case "IN_PROGRESS": return "primary";
        case "RESOLVED": return "success";
        case "REJECTED": return "error";
        case "CANCELED": return "default";
        default: return "default";
    }
};

const getRoutingStatusColor = (routingStatus) => {
    switch (routingStatus) {
        case "CONFIRMED": return "success";
        case "PENDING_REVIEW": return "warning";
        case "REJECTED": return "error";
        default: return "default";
    }
};

const InfoRow = ({icon, label, value}) => (
    <Stack direction="row" spacing={1.5} alignItems="flex-start">
        {icon}
        <Box sx={{minWidth: 0}}>
            <Typography variant="caption" color="text.secondary">
                {label}
            </Typography>
            <Typography variant="body2" sx={{wordBreak: "break-word"}}>
                {value || "Not set"}
            </Typography>
        </Box>
    </Stack>
);

const RequestDetailsPage = () => {
    const {id} = useParams();
    const navigate = useNavigate();
    const {user} = useAuth();

    const {request, loading, error} = useRequestDetails(id);
    const {deleteRequest, exportRequestPdf, isExecuting} = useRequestActions();

    const {categories} = useCategories({paged: false});
    const {departments} = useDepartments({paged: false});
    const {municipalities} = useMunicipalities({paged: false});

    const canEdit = getUserRoles(user).some((role) => staffRoles.includes(role));
    const canManageAssignments = getUserRoles(user).some((role) => managementRoles.includes(role));

    const handleDelete = async () => {
        await deleteRequest(request.id, () => {
            navigate("/requests");
        });
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

    const categoryName = categories.find((category) => category.id === request.categoryId)?.name || "Unassigned";
    const departmentName = departments.find((department) => department.id === request.departmentId)?.name || "Unassigned";
    const municipality = municipalities.find((item) => item.id === request.municipalityId);
    const municipalityLabel = municipality?.name || municipality?.code || "Unassigned";

    return (
        <Box>
            <ActionBar
                onBack={() => navigate(`/requests/${request.id}/logs`)}
                backLabel="Timeline"
                onSecondaryBack={() => navigate("/requests")}
                secondaryBackLabel="Back to Requests"
                onEdit={canEdit ? () => navigate(`/requests/${request.id}/edit`) : undefined}
                onDelete={handleDelete}
            >
                <Button
                    variant="outlined"
                    startIcon={<Download />}
                    onClick={() => exportRequestPdf(request.id)}
                    disabled={isExecuting}
                >
                    Export PDF
                </Button>
            </ActionBar>

            <Stack spacing={3}>
                <Paper elevation={0} className="request-case-file">
                    <Stack spacing={3}>
                        <Stack
                            className="request-case-header"
                            direction={{xs: "column", md: "row"}}
                            spacing={2}
                            alignItems={{xs: "flex-start", md: "center"}}
                            justifyContent="space-between"
                        >
                            <Stack direction="row" spacing={2} alignItems="center">
                                <Box className="request-case-icon">
                                    <ReportProblem />
                                </Box>
                                <Box>
                                    <Typography variant="h4">
                                        {request.title || "Untitled Request"}
                                    </Typography>
                                    <Typography variant="body2" color="text.secondary">
                                        Request #{request.id}
                                    </Typography>
                                </Box>
                            </Stack>

                            <Stack direction="row" spacing={1} flexWrap="wrap" useFlexGap>
                                <Chip label={formatLabel(request.status)} color={getStatusColor(request.status)} />
                                <Chip label={formatLabel(request.priority)} color={getPriorityColor(request.priority)} variant="outlined" />
                                <Chip label={formatLabel(request.routingStatus)} color={getRoutingStatusColor(request.routingStatus)} variant="outlined" />
                            </Stack>
                        </Stack>

                        <Divider />

                        {request.routingStatus === "PENDING_REVIEW" && (
                            <Alert severity="warning">
                                Routing is pending review. Staff can confirm or adjust the triage from the edit page.
                            </Alert>
                        )}

                        <Grid container spacing={2.5}>
                            <Grid size={{xs: 12, md: 7}}>
                                <Card variant="outlined" className="detail-card" sx={{height: "100%"}}>
                                    <CardContent>
                                        <Stack spacing={2}>
                                            <Stack direction="row" spacing={1} alignItems="center">
                                                <Description color="primary" />
                                                <Typography variant="h6">
                                                    Description
                                                </Typography>
                                            </Stack>
                                            <Typography variant="body1" sx={{whiteSpace: "pre-wrap"}}>
                                                {request.description || "No description provided."}
                                            </Typography>
                                        </Stack>
                                    </CardContent>
                                </Card>
                            </Grid>

                            <Grid size={{xs: 12, md: 5}}>
                                <Card variant="outlined" className="detail-card" sx={{height: "100%"}}>
                                    <CardContent>
                                        <Stack spacing={2}>
                                            <Stack direction="row" spacing={1} alignItems="center">
                                                <Psychology color="primary" />
                                                <Typography variant="h6">
                                                    Summary
                                                </Typography>
                                            </Stack>
                                            <Typography variant="body2" color={request.summary ? "text.primary" : "text.secondary"} sx={{whiteSpace: "pre-wrap"}}>
                                                {request.summary || "No summary is available yet."}
                                            </Typography>
                                        </Stack>
                                    </CardContent>
                                </Card>
                            </Grid>

                            {request.routingStatus !== "PENDING_REVIEW" && (
                                <RequireRole allowedRoles={['ADMINISTRATOR', 'MANAGER', 'EMPLOYEE']}>
                                    <Grid size={{ xs: 12, md: 4 }}>
                                        <RequestAssignmentPanel
                                            request={request}
                                            canManage={canManageAssignments}
                                        />
                                    </Grid>
                                </RequireRole>

                            )}

                            <Grid size={{xs: 12, md: 4}}>
                                <Card variant="outlined" className="detail-card" sx={{height: "100%"}}>
                                    <CardContent>
                                        <Typography variant="h6" sx={{mb: 2}}>
                                            Routing
                                        </Typography>
                                        <Stack spacing={2}>
                                            <InfoRow icon={<CategoryIcon color="primary" />} label="Category" value={categoryName} />
                                            <InfoRow icon={<Business color="primary" />} label="Department" value={departmentName} />
                                            <InfoRow icon={<LocationCity color="primary" />} label="Municipality" value={municipalityLabel} />
                                        </Stack>
                                    </CardContent>
                                </Card>
                            </Grid>

                            <Grid size={{xs: 12, md: 4}}>
                                <Card variant="outlined" className="detail-card" sx={{height: "100%"}}>
                                    <CardContent>
                                        <Typography variant="h6" sx={{mb: 2}}>
                                            Location
                                        </Typography>
                                        <Stack spacing={2}>
                                            {request.location?.latitude && request.location?.longitude ? (
                                                <Box className="map-frame">
                                                    <LocationPicker
                                                        initialLocation={request.location}
                                                        readOnly
                                                        height={260}
                                                    />
                                                </Box>
                                            ) : (
                                                <Typography variant="body2" color="text.secondary">
                                                    No location provided.
                                                </Typography>
                                            )}
                                            <InfoRow icon={<LocationOn color="primary" />} label="Latitude" value={request.location?.latitude} />
                                            <InfoRow icon={<LocationOn color="primary" />} label="Longitude" value={request.location?.longitude} />
                                        </Stack>
                                    </CardContent>
                                </Card>
                            </Grid>

                            <Grid size={{xs: 12, md: 4}}>
                                <Card variant="outlined" className="detail-card" sx={{height: "100%"}}>
                                    <CardContent>
                                        <Typography variant="h6" sx={{mb: 2}}>
                                            Request Info
                                        </Typography>
                                        <Stack spacing={2}>
                                            <InfoRow icon={<Badge color="primary" />} label="Request ID" value={request.id} />
                                            <InfoRow icon={<Person color="primary" />} label="Citizen ID" value={request.userId} />
                                        </Stack>
                                    </CardContent>
                                </Card>
                            </Grid>
                        </Grid>
                    </Stack>
                </Paper>

                <RequestCommentsSection requestId={request.id} />
            </Stack>
        </Box>
    );
};

export default RequestDetailsPage;
