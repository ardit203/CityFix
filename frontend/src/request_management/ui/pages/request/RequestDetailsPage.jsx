import React from "react";
import {useNavigate, useParams} from "react-router";
import {
    Box,
    Card,
    CardContent,
    Divider,
    Grid,
    Paper,
    Stack,
    Typography,
    Chip
} from "@mui/material";
import {
    ReportProblem as ReportProblemIcon,
    Business,
    Category as CategoryIcon,
    LocationCity,
    LocationOn,
    Badge,
    Description
} from "@mui/icons-material";

import useRequestActions from "../../../hooks/request/useRequestActions.js";
import useRequestDetails from "../../../hooks/request/useRequestDetails.js";
import useCategories from "../../../../administration/hooks/category/useCategories.js";
import useDepartments from "../../../../administration/hooks/department/useDepartments.js";
import useMunicipalities from "../../../../administration/hooks/municipality/useMunicipalities.js";

import AsyncDataView from "../../../../common/ui/components/AsyncDataView.jsx";
import ActionBar from "../../../../common/ui/components/ActionBar.jsx";

const getPriorityColor = (priority) => {
    switch (priority) {
        case 'HIGH': return 'error';
        case 'MEDIUM': return 'warning';
        case 'LOW': return 'info';
        default: return 'default';
    }
};

const getStatusColor = (status) => {
    switch (status) {
        case 'OPEN': return 'primary';
        case 'IN_PROGRESS': return 'secondary';
        case 'RESOLVED': return 'success';
        case 'CLOSED': return 'default';
        case 'REJECTED': return 'error';
        default: return 'default';
    }
};

const getRoutingStatusColor = (routingStatus) => {
    switch (routingStatus) {
        case 'ROUTED': return 'success';
        case 'PENDING': return 'warning';
        case 'FAILED': return 'error';
        default: return 'default';
    }
};

const RequestDetailsPage = () => {
    const {id} = useParams();
    const navigate = useNavigate();

    const {request, loading, error} = useRequestDetails(id);
    const {deleteRequest} = useRequestActions();

    const { categories } = useCategories({ paged: false });
    const { departments } = useDepartments({ paged: false });
    const { municipalities } = useMunicipalities({ paged: false });

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

    // Helper functions to get names from IDs
    const getCategoryName = (id) => categories.find(c => c.id === id)?.name || "Unassigned";
    const getDepartmentName = (id) => departments.find(d => d.id === id)?.name || "Unassigned";
    const getMunicipalityCode = (id) => municipalities.find(m => m.id === id)?.code || "Unassigned";

    return (
        <Box>
            <ActionBar
                onSecondaryBack={() => navigate("/requests")}
                secondaryBackLabel="Back to Requests"
                onEdit={() => navigate(`/requests/${request.id}/edit`)}
                onDelete={handleDelete}
            />

            <Paper elevation={2} sx={{p: 4, borderRadius: 4}}>
                <Grid container spacing={4}>
                    <Grid size={{xs: 12}}>
                        <Stack direction="row" spacing={2} alignItems="center" sx={{mb: 2}}>
                            <ReportProblemIcon color="primary" sx={{fontSize: 40}}/>

                            <Box sx={{ flexGrow: 1 }}>
                                <Typography variant="h4" fontWeight={600}>
                                    {request.title || "Untitled Request"}
                                </Typography>

                                <Stack direction="row" spacing={1} sx={{ mt: 1 }}>
                                    <Chip 
                                        label={request.status || "Unknown Status"} 
                                        color={getStatusColor(request.status)}
                                        size="small"
                                    />
                                    <Chip 
                                        label={request.priority || "No Priority"} 
                                        color={getPriorityColor(request.priority)}
                                        size="small"
                                        variant="outlined"
                                    />
                                    <Chip 
                                        label={request.routingStatus || "Not Routed"} 
                                        color={getRoutingStatusColor(request.routingStatus)}
                                        size="small"
                                        variant="outlined"
                                    />
                                </Stack>
                            </Box>
                        </Stack>

                        <Divider sx={{mb: 3}}/>

                        <Grid container spacing={2}>

                            {/* 1. Basic Info & Summary */}
                            <Grid size={{xs: 12, md: 4}}>
                                <Card variant="outlined" sx={{height: '100%'}}>
                                    <CardContent>
                                        <Typography variant="h6" sx={{mb: 2}}>
                                            Details
                                        </Typography>

                                        <Stack spacing={2}>
                                            <Stack direction="row" spacing={1} alignItems="center">
                                                <Badge color="primary"/>
                                                <Typography>
                                                    ID: {request.id}
                                                </Typography>
                                            </Stack>
                                            
                                            {request.userId && (
                                                <Stack direction="row" spacing={1} alignItems="center">
                                                    <Typography>
                                                        <strong>User ID:</strong> {request.userId}
                                                    </Typography>
                                                </Stack>
                                            )}

                                            <Box sx={{ mt: 2 }}>
                                                <Typography variant="subtitle2" color="text.secondary">
                                                    Summary
                                                </Typography>
                                                <Typography variant="body2">
                                                    {request.summary || "No AI summary available."}
                                                </Typography>
                                            </Box>
                                        </Stack>
                                    </CardContent>
                                </Card>
                            </Grid>

                            {/* 2. Routing Info */}
                            <Grid size={{xs: 12, md: 4}}>
                                <Card variant="outlined" sx={{height: '100%'}}>
                                    <CardContent>
                                        <Typography variant="h6" sx={{mb: 2}}>
                                            Routing Information
                                        </Typography>

                                        <Stack spacing={2}>
                                            <Stack direction="row" spacing={1} alignItems="center">
                                                <CategoryIcon color="primary"/>
                                                <Typography>
                                                    <strong>Category:</strong> {getCategoryName(request.categoryId)}
                                                </Typography>
                                            </Stack>

                                            <Stack direction="row" spacing={1} alignItems="center">
                                                <Business color="primary"/>
                                                <Typography>
                                                    <strong>Department:</strong> {getDepartmentName(request.departmentId)}
                                                </Typography>
                                            </Stack>
                                            
                                            <Stack direction="row" spacing={1} alignItems="center">
                                                <LocationCity color="primary"/>
                                                <Typography>
                                                    <strong>Municipality:</strong> {getMunicipalityCode(request.municipalityId)}
                                                </Typography>
                                            </Stack>
                                        </Stack>
                                    </CardContent>
                                </Card>
                            </Grid>

                            {/* 3. Description & Location */}
                            <Grid size={{xs: 12, md: 4}}>
                                <Card variant="outlined" sx={{height: '100%'}}>
                                    <CardContent>
                                        <Typography variant="h6" sx={{mb: 2}}>
                                            Description & Location
                                        </Typography>

                                        <Stack spacing={2}>
                                            <Stack direction="row" spacing={1} alignItems="flex-start">
                                                <Description color="primary"/>
                                                <Typography variant="body2">
                                                    {request.description || "No description provided"}
                                                </Typography>
                                            </Stack>

                                            <Stack direction="row" spacing={1} alignItems="flex-start">
                                                <LocationOn color="primary"/>
                                                <Box>
                                                    <Typography variant="body2">
                                                        <strong>Latitude:</strong> {request.location?.latitude ?? "N/A"}
                                                    </Typography>
                                                    <Typography variant="body2">
                                                        <strong>Longitude:</strong> {request.location?.longitude ?? "N/A"}
                                                    </Typography>
                                                </Box>
                                            </Stack>
                                        </Stack>
                                    </CardContent>
                                </Card>
                            </Grid>

                        </Grid>
                    </Grid>
                </Grid>
            </Paper>
        </Box>
    );
};

export default RequestDetailsPage;
