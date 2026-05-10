import React from 'react';
import {useNavigate} from "react-router";
import {Box, Button, Card, CardActions, CardContent, Chip, Stack, Typography, Tooltip} from "@mui/material";
import InfoIcon from "@mui/icons-material/Info";
import EditIcon from "@mui/icons-material/Edit";
import DeleteIcon from "@mui/icons-material/Delete";
import LocationCityIcon from '@mui/icons-material/LocationCity';
import CategoryIcon from '@mui/icons-material/Category';
import BusinessIcon from '@mui/icons-material/Business';
import useRequestActions from "../../../hooks/request/useRequestActions.js";

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

const RequestCard = ({request, onDelete}) => {
    const navigate = useNavigate();
    const {deleteRequest} = useRequestActions();

    const handleDelete = async () => {
        await deleteRequest(request.id, onDelete);
    };

    return (
        <Card sx={{
            height: '100%', 
            display: 'flex', 
            flexDirection: 'column', 
            minWidth: 310
        }}>
            <CardContent sx={{flexGrow: 1}}>
                <Typography variant="h6" gutterBottom noWrap title={request.title || "Untitled Request"}>
                    {request.title || "Untitled Request"}
                </Typography>

                <Stack direction="row" spacing={1} flexWrap="wrap" useFlexGap sx={{ mb: 2, mt: 1 }}>
                    <Tooltip title="Status">
                        <Chip 
                            label={request.status || "Unknown Status"} 
                            color={getStatusColor(request.status)}
                            size="small"
                        />
                    </Tooltip>
                    
                    <Tooltip title="Priority">
                        <Chip 
                            label={request.priority || "No Priority"} 
                            color={getPriorityColor(request.priority)}
                            size="small"
                            variant="outlined"
                        />
                    </Tooltip>
                    
                    <Tooltip title="Routing Status">
                        <Chip 
                            label={request.routingStatus || "Not Routed"} 
                            color={getRoutingStatusColor(request.routingStatus)}
                            size="small"
                            variant="outlined"
                        />
                    </Tooltip>
                </Stack>

                <Stack spacing={1} sx={{ mt: 2 }}>
                    <Box sx={{ display: 'flex', alignItems: 'center', gap: 1, color: 'text.secondary' }}>
                        <CategoryIcon fontSize="small" />
                        <Typography variant="body2" noWrap>
                            {request.categoryName === "Unassigned" ? "No Category" : request.categoryName}
                        </Typography>
                    </Box>

                    <Box sx={{ display: 'flex', alignItems: 'center', gap: 1, color: 'text.secondary' }}>
                        <BusinessIcon fontSize="small" />
                        <Typography variant="body2" noWrap>
                            {request.departmentName === "Unassigned" ? "No Department" : request.departmentName}
                        </Typography>
                    </Box>

                    <Box sx={{ display: 'flex', alignItems: 'center', gap: 1, color: 'text.secondary' }}>
                        <LocationCityIcon fontSize="small" />
                        <Typography variant="body2" noWrap>
                            {request.municipalityCode === "Unassigned" ? "No Municipality" : request.municipalityCode}
                        </Typography>
                    </Box>
                </Stack>
            </CardContent>

            <CardActions sx={{justifyContent: "space-between"}}>
                <Button
                    startIcon={<InfoIcon/>}
                    onClick={() => navigate(`/requests/${request.id}`)}
                >
                    Info
                </Button>

                <Box>
                    <Button
                        startIcon={<EditIcon/>}
                        onClick={() => navigate(`/requests/${request.id}/edit`)}
                        color="warning"
                    >
                        Edit
                    </Button>

                    <Button
                        startIcon={<DeleteIcon/>}
                        onClick={handleDelete}
                        color="error"
                    >
                        Delete
                    </Button>
                </Box>
            </CardActions>
        </Card>
    );
};

export default RequestCard;
