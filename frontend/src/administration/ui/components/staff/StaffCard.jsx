import React from "react";
import { useNavigate } from "react-router";
import {
    Box,
    Button,
    Card,
    CardActions,
    CardContent,
    Typography,
    Stack,
    Avatar
} from "@mui/material";
import {
    Info as InfoIcon,
    Edit as EditIcon,
    Delete as DeleteIcon,
    Email as EmailIcon,
    Business as BusinessIcon,
    LocationCity as CityIcon
} from "@mui/icons-material";

// IMPORTANT: Adjust this to point to your actual Staff hook!
import useStaffActions from "../../../hooks/staff/useStaffActions.js";

const StaffCard = ({ staff, onDelete }) => {
    const navigate = useNavigate();

    // Make sure you have created useStaffActions
    const { deleteStaff } = useStaffActions();

    const handleDelete = async () => {
        await deleteStaff(staff.id, onDelete);
    };

    return (
        <Card sx={{
            maxWidth: 350,
            display: 'flex',
            flexDirection: 'column',
            height: '100%' // Ensures all cards in a grid are exactly the same height!
        }}>
            <CardContent sx={{ flexGrow: 1 }}>

                {/* 1. Profile Header: Avatar + Name + Username */}
                <Stack direction="row" spacing={2} alignItems="center" sx={{ mb: 3 }}>
                    <Avatar sx={{ bgcolor: 'primary.main', width: 48, height: 48 }}>
                        {staff.name?.charAt(0)}{staff.surname?.charAt(0)}
                    </Avatar>

                    <Box sx={{ overflow: 'hidden' }}>
                        <Typography variant="h6" fontWeight={600} lineHeight={1.2} noWrap>
                            {staff.name} {staff.surname}
                        </Typography>
                        <Typography variant="body2" color="text.secondary" noWrap>
                            @{staff.username}
                        </Typography>
                        <Typography variant="body2" color="text.secondary" noWrap>
                            {staff.role.replace("ROLE_","")}
                        </Typography>
                    </Box>
                </Stack>

                {/* 2. Details Section: Email, Department, Municipality */}
                <Stack spacing={1.5}>

                    <Stack direction="row" spacing={1} alignItems="center">
                        <EmailIcon fontSize="small" color="action" />
                        <Typography variant="body2" noWrap title={staff.email}>
                            {staff.email}
                        </Typography>
                    </Stack>

                    {staff.departmentName && (
                        <Stack direction="row" spacing={1} alignItems="center">
                            <BusinessIcon fontSize="small" color="action" />
                            <Typography variant="body2" noWrap title={staff.departmentName}>
                                {staff.departmentName}
                            </Typography>
                        </Stack>
                    )}

                    {(staff.municipalityName || staff.municipalityCode) && (
                        <Stack direction="row" spacing={1} alignItems="center">
                            <CityIcon fontSize="small" color="action" />
                            <Typography variant="body2" noWrap>
                                {staff.municipalityName} ({staff.municipalityCode})
                            </Typography>
                        </Stack>
                    )}

                </Stack>
            </CardContent>

            {/* 3. Actions Section */}
            <CardActions sx={{ justifyContent: "space-between", px: 2, pb: 2 }}>
                <Button
                    size="small"
                    startIcon={<InfoIcon />}
                    onClick={() => navigate(`/staff/${staff.id}`)}
                >
                    Info
                </Button>

                <Box>
                    <Button
                        size="small"
                        startIcon={<EditIcon />}
                        onClick={() => navigate(`/staff/${staff.id}/edit`)}
                        color="warning"
                        sx={{ mr: 1 }}
                    >
                        Edit
                    </Button>

                    <Button
                        size="small"
                        startIcon={<DeleteIcon />}
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

export default StaffCard;
