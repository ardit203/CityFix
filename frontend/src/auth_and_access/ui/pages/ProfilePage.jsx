import React from "react";
import {
    Avatar,
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
    Cake,
    Edit,
    Email,
    Home,
    Notifications,
    NotificationsOff,
    Person,
    Phone,
    Wc
} from "@mui/icons-material";
import { useNavigate } from "react-router";

// Adjust path as needed!
import useProfile from "../../hooks/profile/useProfile.js";
import AsyncDataView from "../../../common/ui/components/AsyncDataView.jsx";

const ProfilePage = () => {
    const navigate = useNavigate();
    const { user, loading, error } = useProfile();

    if (loading || error || !user) {
        return (
            <AsyncDataView
                loading={loading}
                error={error}
                data={user}
                notFoundMessage="Profile not found."
                backUrl="/"
            />
        );
    }

    const profile = user.profile;
    const address = profile?.address;
    const fullName = `${profile?.name || ""} ${profile?.surname || ""}`.trim();

    return (
        <Box sx={{ maxWidth: 1200, mx: 'auto', p: 3 }}>
            <Stack
                direction="row"
                justifyContent="space-between"
                alignItems="center"
                sx={{ mb: 3 }}
            >
                <Typography variant="h4" fontWeight={600}>
                    My Profile
                </Typography>

                <Button
                    variant="contained"
                    startIcon={<Edit />}
                    onClick={() => navigate("/profile/edit")}
                >
                    Edit Profile
                </Button>
            </Stack>

            <Paper elevation={2} sx={{ p: 4, borderRadius: 4 }}>
                <Grid container spacing={4}>
                    
                    {/* --- Left Column: Avatar --- */}
                    <Grid item xs={12} md={3}>
                        <Box sx={{ display: "flex", justifyContent: "center" }}>
                            <Avatar
                                src={profile?.profilePictureUrl || undefined}
                                sx={{
                                    width: 160,
                                    height: 160,
                                    fontSize: '4rem',
                                    bgcolor: 'primary.main',
                                    boxShadow: 2
                                }}
                            >
                                {profile?.name?.charAt(0) || user.username?.charAt(0) || "U"}
                            </Avatar>
                        </Box>
                    </Grid>

                    {/* --- Right Column: Details --- */}
                    <Grid item xs={12} md={9}>
                        <Stack direction="row" spacing={2} alignItems="center" sx={{ mb: 2, flexWrap: "wrap" }}>
                            <Typography variant="h4" fontWeight={600}>
                                {fullName || user.username}
                            </Typography>

                            <Chip
                                label={user.role ? user.role.replace("ROLE_", "") : "User"}
                                color="primary"
                                variant="outlined"
                            />

                            <Chip
                                label={user.locked ? "Locked" : "Active"}
                                color={user.locked ? "error" : "success"}
                                variant="outlined"
                            />

                            <Chip
                                icon={user.notificationsEnabled ? <Notifications fontSize="small"/> : <NotificationsOff fontSize="small"/>}
                                label={user.notificationsEnabled ? "Notifications enabled" : "Notifications disabled"}
                                color={user.notificationsEnabled ? "success" : "default"}
                                variant="outlined"
                            />
                        </Stack>

                        <Typography variant="body1" color="text.secondary" sx={{ mb: 3 }}>
                            @{user.username}
                        </Typography>

                        <Divider sx={{ mb: 3 }} />

                        <Grid container spacing={3}>
                            
                            {/* --- Account Information --- */}
                            <Grid item xs={12} md={6}>
                                <Card variant="outlined" sx={{ height: '100%' }}>
                                    <CardContent sx={{ p: 3 }}>
                                        <Typography variant="h6" color="primary" sx={{ mb: 3 }}>
                                            Account Information
                                        </Typography>

                                        <Stack spacing={2.5}>
                                            <Stack direction="row" spacing={1.5} alignItems="center">
                                                <Person color="action" />
                                                <Box>
                                                    <Typography variant="caption" color="text.secondary">Username</Typography>
                                                    <Typography variant="body1">{user.username}</Typography>
                                                </Box>
                                            </Stack>

                                            <Stack direction="row" spacing={1.5} alignItems="center">
                                                <Email color="action" />
                                                <Box>
                                                    <Typography variant="caption" color="text.secondary">Email Address</Typography>
                                                    <Typography variant="body1">{user.email}</Typography>
                                                </Box>
                                            </Stack>

                                            <Stack direction="row" spacing={1.5} alignItems="center">
                                                <Badge color="action" />
                                                <Box>
                                                    <Typography variant="caption" color="text.secondary">Role</Typography>
                                                    <Typography variant="body1">{user.role}</Typography>
                                                </Box>
                                            </Stack>
                                        </Stack>
                                    </CardContent>
                                </Card>
                            </Grid>

                            {/* --- Personal Information --- */}
                            <Grid item xs={12} md={6}>
                                <Card variant="outlined" sx={{ height: '100%' }}>
                                    <CardContent sx={{ p: 3 }}>
                                        <Typography variant="h6" color="primary" sx={{ mb: 3 }}>
                                            Personal Information
                                        </Typography>

                                        <Stack spacing={2.5}>
                                            <Stack direction="row" spacing={1.5} alignItems="center">
                                                <Phone color="action" />
                                                <Box>
                                                    <Typography variant="caption" color="text.secondary">Phone Number</Typography>
                                                    <Typography variant="body1">{profile?.phoneNumber || "Not provided"}</Typography>
                                                </Box>
                                            </Stack>

                                            <Stack direction="row" spacing={1.5} alignItems="center">
                                                <Cake color="action" />
                                                <Box>
                                                    <Typography variant="caption" color="text.secondary">Date of Birth</Typography>
                                                    <Typography variant="body1">{profile?.dateOfBirth || "Not provided"}</Typography>
                                                </Box>
                                            </Stack>

                                            <Stack direction="row" spacing={1.5} alignItems="center">
                                                <Wc color="action" />
                                                <Box>
                                                    <Typography variant="caption" color="text.secondary">Gender</Typography>
                                                    <Typography variant="body1" sx={{ textTransform: 'capitalize' }}>
                                                        {profile?.gender?.toLowerCase() || "Not provided"}
                                                    </Typography>
                                                </Box>
                                            </Stack>
                                        </Stack>
                                    </CardContent>
                                </Card>
                            </Grid>

                            {/* --- Address Information --- */}
                            <Grid item xs={12}>
                                <Card variant="outlined">
                                    <CardContent sx={{ p: 3 }}>
                                        <Typography variant="h6" color="primary" sx={{ mb: 3 }}>
                                            Address Details
                                        </Typography>

                                        <Stack direction={{ xs: 'column', md: 'row' }} spacing={4}>
                                            <Box sx={{ display: 'flex', gap: 1.5 }}>
                                                <Home color="action" />
                                                <Box>
                                                    <Typography variant="caption" color="text.secondary">Street Address</Typography>
                                                    <Typography variant="body1">{address?.street || "Not provided"}</Typography>
                                                </Box>
                                            </Box>

                                            <Box sx={{ display: 'flex', gap: 1.5 }}>
                                                <Box>
                                                    <Typography variant="caption" color="text.secondary">City</Typography>
                                                    <Typography variant="body1">{address?.city || "Not provided"}</Typography>
                                                </Box>
                                            </Box>

                                            <Box sx={{ display: 'flex', gap: 1.5 }}>
                                                <Box>
                                                    <Typography variant="caption" color="text.secondary">Postal Code</Typography>
                                                    <Typography variant="body1">{address?.postalCode || "Not provided"}</Typography>
                                                </Box>
                                            </Box>
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

export default ProfilePage;
