import React from "react";
import { useNavigate, useParams } from "react-router";
import {
    Box,
    Card,
    CardContent,
    Divider,
    Paper,
    Stack,
    Typography,
    Avatar,
    Chip,
    Grid
} from "@mui/material";
import {
    Person,
    Email,
    Phone,
    Cake,
    Wc,
    LocationOn,
    NotificationsActive,
    NotificationsOff,
    AdminPanelSettings as AdminIcon,
    Engineering as EmployeeIcon,
    ManageAccounts as ManagerIcon,
    Person as PersonIcon,
    Lock,
    LockOpen,
    Badge
} from "@mui/icons-material";

// IMPORTANT: Adjust paths as necessary!
import useUserDetails from "../../hooks/user/useUserDetails.js";
import useUserActions from "../../hooks/user/useUserActions.js";
import AsyncDataView from "../../../common/ui/components/AsyncDataView.jsx";
import ActionBar from "../../../common/ui/components/ActionBar.jsx";

// Helper to format role visually
const getRoleDetails = (role) => {
    switch (role) {
        case 'ROLE_ADMINISTRATOR': return { icon: <AdminIcon fontSize="small" />, color: 'error', label: 'Administrator' };
        case 'ROLE_MANAGER': return { icon: <ManagerIcon fontSize="small" />, color: 'warning', label: 'Manager' };
        case 'ROLE_EMPLOYEE': return { icon: <EmployeeIcon fontSize="small" />, color: 'info', label: 'Employee' };
        case 'ROLE_CITIZEN': return { icon: <PersonIcon fontSize="small" />, color: 'success', label: 'Citizen' };
        default: return { icon: <PersonIcon fontSize="small" />, color: 'default', label: 'Unknown Role' };
    }
};

const UserDetailsPage = () => {
    const { id } = useParams();
    const navigate = useNavigate();

    const { user, loading, error } = useUserDetails(id);
    const { deleteUser } = useUserActions();

    const handleDelete = async () => {
        await deleteUser(user.id, () => {
            navigate("/users");
        });
    };

    if (loading || error || !user) {
        return (
            <AsyncDataView
                loading={loading}
                error={error}
                data={user}
                notFoundMessage="User not found."
                backUrl="/users"
            />
        );
    }

    const roleData = getRoleDetails(user.role);

    return (
        <Box>
            <ActionBar
                onSecondaryBack={() => navigate("/users")}
                secondaryBackLabel="Back to Users"
                onEdit={() => navigate(`/users/${user.id}/edit`)}
                onDelete={handleDelete}
            />

            <Paper elevation={2} sx={{ p: 4, borderRadius: 4 }}>
                <Box>
                        
                        <Stack direction="row" spacing={3} sx={{ alignItems: 'center', mb: 2 }}>
                            <Avatar 
                                src={user.profile?.profilePictureUrl}
                                sx={{ width: 80, height: 80, bgcolor: 'primary.main', fontSize: '2rem' }}
                            >
                                {!user.profile?.profilePictureUrl && user.profile?.name?.charAt(0)}
                            </Avatar>

                            <Box>
                                <Typography variant="h4" fontWeight={600}>
                                    {user.profile?.name} {user.profile?.surname}
                                </Typography>

                                <Typography variant="body1" color="text.secondary">
                                    @{user.username}
                                </Typography>
                            </Box>
                        </Stack>

                        <Divider sx={{ mb: 3 }} />

                        <Stack direction={{ xs: 'column', md: 'row' }} spacing={2}>
                            {/* --- Basic Information --- */}
                            <Box sx={{ flex: 1 }}>
                                <Card variant="outlined" sx={{ height: '100%' }}>
                                    <CardContent>
                                        <Typography variant="h6" sx={{ mb: 3 }}>
                                            Account Status
                                        </Typography>

                                        <Stack spacing={2.5}>
                                            <Stack direction="row" spacing={1.5} sx={{ alignItems: 'center' }}>
                                                <Badge color="primary" />
                                                <Typography>
                                                    ID: {user.id}
                                                </Typography>
                                            </Stack>

                                            <Stack direction="row" spacing={1.5} sx={{ alignItems: 'center' }}>
                                                <Email color="primary" />
                                                <Typography>
                                                    Email: {user.email}
                                                </Typography>
                                            </Stack>

                                            <Stack direction="row" spacing={1.5} sx={{ alignItems: 'center' }}>
                                                <Typography component="div" sx={{ display: 'flex', alignItems: 'center', gap: 1 }}>
                                                    Role: 
                                                    <Chip 
                                                        icon={roleData.icon} 
                                                        label={roleData.label} 
                                                        color={roleData.color} 
                                                        size="small"
                                                        variant="outlined"
                                                    />
                                                </Typography>
                                            </Stack>

                                            <Stack direction="row" spacing={1.5} sx={{ alignItems: 'center' }}>
                                                <Typography component="div" sx={{ display: 'flex', alignItems: 'center', gap: 1 }}>
                                                    Status: 
                                                    {user.locked ? (
                                                        <Chip icon={<Lock fontSize="small"/>} label="Locked" color="error" size="small" variant="filled" />
                                                    ) : (
                                                        <Chip icon={<LockOpen fontSize="small"/>} label="Active" color="success" size="small" variant="outlined" />
                                                    )}
                                                </Typography>
                                            </Stack>

                                            <Stack direction="row" spacing={1.5} sx={{ alignItems: 'center' }}>
                                                {user.notificationsEnabled ? <NotificationsActive color="primary" /> : <NotificationsOff color="action" />}
                                                <Typography>
                                                    Notifications: {user.notificationsEnabled ? "Enabled" : "Disabled"}
                                                </Typography>
                                            </Stack>
                                        </Stack>
                                    </CardContent>
                                </Card>
                            </Box>

                            {/* --- Personal Details --- */}
                            <Box sx={{ flex: 1 }}>
                                <Card variant="outlined" sx={{ height: '100%' }}>
                                    <CardContent>
                                        <Typography variant="h6" sx={{ mb: 3 }}>
                                            Personal Details
                                        </Typography>

                                        <Stack spacing={2.5}>
                                            <Stack direction="row" spacing={1.5} sx={{ alignItems: 'center' }}>
                                                <Phone color="primary" />
                                                <Typography>
                                                    Phone: {user.profile?.phoneNumber || "N/A"}
                                                </Typography>
                                            </Stack>

                                            <Stack direction="row" spacing={1.5} sx={{ alignItems: 'center' }}>
                                                <Cake color="primary" />
                                                <Typography>
                                                    DOB: {user.profile?.dateOfBirth || "N/A"}
                                                </Typography>
                                            </Stack>

                                            <Stack direction="row" spacing={1.5} sx={{ alignItems: 'center' }}>
                                                <Wc color="primary" />
                                                <Typography sx={{ textTransform: 'capitalize' }}>
                                                    Gender: {user.profile?.gender?.toLowerCase() || "N/A"}
                                                </Typography>
                                            </Stack>

                                            <Stack direction="row" spacing={1.5} sx={{ alignItems: 'flex-start' }}>
                                                <LocationOn color="primary" />
                                                <Typography>
                                                    Address: {user.profile?.address?.street || user.profile?.address?.city || user.profile?.address?.postalCode ? (
                                                        <>
                                                            {user.profile?.address?.street && `${user.profile.address.street}, `}
                                                            {user.profile?.address?.city && `${user.profile.address.city} `}
                                                            {user.profile?.address?.postalCode && `(${user.profile.address.postalCode})`}
                                                        </>
                                                    ) : (
                                                        <Box component="span" sx={{ color: 'text.secondary' }}>No address provided</Box>
                                                    )}
                                                </Typography>
                                            </Stack>
                                        </Stack>
                                    </CardContent>
                                </Card>
                            </Box>

                        </Stack>
                </Box>
            </Paper>
        </Box>
    );
};

export default UserDetailsPage;
