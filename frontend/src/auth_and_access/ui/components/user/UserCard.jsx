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
    Avatar,
    Chip
} from "@mui/material";
import {
    Info as InfoIcon,
    Edit as EditIcon,
    Delete as DeleteIcon,
    AdminPanelSettings as AdminIcon,
    Person as PersonIcon,
    Engineering as EmployeeIcon,
    ManageAccounts as ManagerIcon
} from "@mui/icons-material";

// IMPORTANT: Adjust this to point to your actual User hook!
import useUserActions from "../../../hooks/user/useUserActions.js";

// Helper to pick the right icon
const getRoleIcon = (role) => {
    switch(role) {
        case 'ROLE_ADMINISTRATOR': return <AdminIcon />;
        case 'ROLE_MANAGER': return <ManagerIcon />;
        case 'ROLE_EMPLOYEE': return <EmployeeIcon />;
        default: return <PersonIcon />;
    }
};

// Helper to pick the right color
const getRoleColor = (role) => {
    switch(role) {
        case 'ROLE_ADMINISTRATOR': return 'error';
        case 'ROLE_MANAGER': return 'warning';
        case 'ROLE_EMPLOYEE': return 'info';
        default: return 'default';
    }
};

// Helper to format "ROLE_ADMINISTRATOR" into "Administrator"
const getRoleDisplayName = (role) => {
    if (!role) return 'Unknown Role';
    const cleanRole = role.replace('ROLE_', '');
    return cleanRole.charAt(0) + cleanRole.slice(1).toLowerCase();
}

const UserCard = ({ user, onDelete }) => {
    const navigate = useNavigate();

    // Make sure you have created useUserActions
    const { deleteUser } = useUserActions();

    const handleDelete = async () => {
        await deleteUser(user.id, onDelete);
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
                        {user.name?.charAt(0)}{user.surname?.charAt(0)}
                    </Avatar>

                    <Box sx={{ overflow: 'hidden' }}>
                        <Typography variant="h6" fontWeight={600} lineHeight={1.2} noWrap>
                            {user.name} {user.surname}
                        </Typography>
                        <Typography variant="body2" color="text.secondary" noWrap>
                            @{user.username}
                        </Typography>
                    </Box>
                </Stack>

                {/* 2. Details Section: Role Chip */}
                <Stack spacing={1.5}>
                    <Box>
                        <Chip
                            icon={getRoleIcon(user.role)}
                            label={getRoleDisplayName(user.role)}
                            color={getRoleColor(user.role)}
                            size="small"
                            variant="outlined"
                        />
                    </Box>
                </Stack>
            </CardContent>

            {/* 3. Actions Section */}
            <CardActions sx={{ justifyContent: "space-between", px: 2, pb: 2 }}>
                <Button
                    size="small"
                    startIcon={<InfoIcon />}
                    onClick={() => navigate(`/users/${user.id}`)}
                >
                    Info
                </Button>

                <Box>
                    <Button
                        size="small"
                        startIcon={<EditIcon />}
                        onClick={() => navigate(`/users/${user.id}/edit`)}
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

export default UserCard;
