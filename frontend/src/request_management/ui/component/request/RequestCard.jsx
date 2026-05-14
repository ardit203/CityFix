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
            minWidth: 0,
            width: '100%'
        }} className="entity-card">
            <CardContent className="entity-card-content">
                <Box>
                    <Typography variant="h6" noWrap title={request.title || "Untitled Request"}>
                        {request.title || "Untitled Request"}
                    </Typography>
                    <Typography variant="body2" color="text.secondary">
                        Request #{request.id}
                    </Typography>
                </Box>

                <Stack direction="row" spacing={1} flexWrap="wrap" useFlexGap>
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

                <Stack spacing={1} className="entity-card-meta">
                    <Box className="entity-card-row">
                        <CategoryIcon fontSize="small" />
                        <Typography variant="body2" noWrap>
                            {request.categoryName === "Unassigned" ? "No Category" : request.categoryName}
                        </Typography>
                    </Box>

                    <Box className="entity-card-row">
                        <BusinessIcon fontSize="small" />
                        <Typography variant="body2" noWrap>
                            {request.departmentName === "Unassigned" ? "No Department" : request.departmentName}
                        </Typography>
                    </Box>

                    <Box className="entity-card-row">
                        <LocationCityIcon fontSize="small" />
                        <Typography variant="body2" noWrap>
                            {request.municipalityCode === "Unassigned" ? "No Municipality" : request.municipalityCode}
                        </Typography>
                    </Box>
                </Stack>
            </CardContent>

            <CardActions className="entity-card-actions" sx={{justifyContent: "space-between"}}>
                <Button
                    size="small"
                    startIcon={<InfoIcon/>}
                    onClick={() => navigate(`/requests/${request.id}`)}
                >
                    Info
                </Button>

                <Box>
                    <Button
                        size="small"
                        startIcon={<EditIcon/>}
                        onClick={() => navigate(`/requests/${request.id}/edit`)}
                        color="warning"
                    >
                        Edit
                    </Button>

                    <Button
                        size="small"
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
