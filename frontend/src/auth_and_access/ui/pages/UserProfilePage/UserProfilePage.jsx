import {
    Avatar,
    Box,
    Button,
    Card,
    CardContent,
    Chip,
    CircularProgress,
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
    Person,
    Phone,
    Wc
} from "@mui/icons-material";
import { useNavigate } from "react-router";
import useProfile from "../../../hooks/useProfile.js";

const UserProfilePage = () => {
    const navigate = useNavigate();
    const { profileUser: user, loading, error } = useProfile();

    if (loading) {
        return (
            <Box sx={{ display: "flex", justifyContent: "center", alignItems: "center", minHeight: "60vh" }}>
                <CircularProgress />
            </Box>
        );
    }

    if (error) {
        return (
            <Box>
                <Typography color="error">{error}</Typography>
            </Box>
        );
    }

    if (!user) {
        return <Typography>Profile not found.</Typography>;
    }

    const profile = user.profile;
    const address = profile?.address;
    const fullName = `${profile?.name || ""} ${profile?.surname || ""}`.trim();

    return (
        <Box>
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
                    <Grid size={{ xs: 12, md: 3 }}>
                        <Box sx={{ display: "flex", justifyContent: "center" }}>
                            <Avatar
                                src={profile?.profilePictureUrl || undefined}
                                sx={{
                                    width: 160,
                                    height: 160,
                                    fontSize: 56
                                }}
                            >
                                {profile?.name?.charAt(0) || user.username?.charAt(0) || "U"}
                            </Avatar>
                        </Box>
                    </Grid>

                    <Grid size={{ xs: 12, md: 9 }}>
                        <Stack direction="row" spacing={2} alignItems="center" sx={{ mb: 2, flexWrap: "wrap" }}>
                            <Typography variant="h4" fontWeight={600}>
                                {fullName || user.username}
                            </Typography>

                            <Chip
                                label={user.role}
                                color="primary"
                                variant="outlined"
                            />

                            <Chip
                                label={user.locked ? "Locked" : "Unlocked"}
                                color={user.locked ? "error" : "success"}
                                variant="outlined"
                            />

                            <Chip
                                label={user.notificationsEnabled ? "Notifications enabled" : "Notifications disabled"}
                                color={user.notificationsEnabled ? "success" : "default"}
                                variant="outlined"
                            />
                        </Stack>

                        <Typography variant="body1" color="text.secondary" sx={{ mb: 3 }}>
                            @{user.username}
                        </Typography>

                        <Divider sx={{ mb: 3 }} />

                        <Grid container spacing={2}>
                            <Grid size={{ xs: 12, md: 6 }}>
                                <Card variant="outlined">
                                    <CardContent>
                                        <Typography variant="h6" sx={{ mb: 2 }}>
                                            Account Information
                                        </Typography>

                                        <Stack spacing={2}>
                                            <Stack direction="row" spacing={1} alignItems="center">
                                                <Person color="primary" />
                                                <Typography>
                                                    Username: {user.username}
                                                </Typography>
                                            </Stack>

                                            <Stack direction="row" spacing={1} alignItems="center">
                                                <Email color="primary" />
                                                <Typography>
                                                    Email: {user.email}
                                                </Typography>
                                            </Stack>

                                            <Stack direction="row" spacing={1} alignItems="center">
                                                <Badge color="primary" />
                                                <Typography>
                                                    Role: {user.role}
                                                </Typography>
                                            </Stack>

                                            <Stack direction="row" spacing={1} alignItems="center">
                                                <Notifications color="primary" />
                                                <Typography>
                                                    Notifications: {user.notificationsEnabled ? "Enabled" : "Disabled"}
                                                </Typography>
                                            </Stack>
                                        </Stack>
                                    </CardContent>
                                </Card>
                            </Grid>

                            <Grid size={{ xs: 12, md: 6 }}>
                                <Card variant="outlined">
                                    <CardContent>
                                        <Typography variant="h6" sx={{ mb: 2 }}>
                                            Profile Information
                                        </Typography>

                                        <Stack spacing={2}>
                                            <Stack direction="row" spacing={1} alignItems="center">
                                                <Person color="primary" />
                                                <Typography>
                                                    Name: {profile?.name || "Not provided"} {profile?.surname || ""}
                                                </Typography>
                                            </Stack>

                                            <Stack direction="row" spacing={1} alignItems="center">
                                                <Phone color="primary" />
                                                <Typography>
                                                    Phone: {profile?.phoneNumber || "Not provided"}
                                                </Typography>
                                            </Stack>

                                            <Stack direction="row" spacing={1} alignItems="center">
                                                <Cake color="primary" />
                                                <Typography>
                                                    Date of birth: {profile?.dateOfBirth || "Not provided"}
                                                </Typography>
                                            </Stack>

                                            <Stack direction="row" spacing={1} alignItems="center">
                                                <Wc color="primary" />
                                                <Typography>
                                                    Gender: {profile?.gender || "Not provided"}
                                                </Typography>
                                            </Stack>
                                        </Stack>
                                    </CardContent>
                                </Card>
                            </Grid>

                            <Grid size={{ xs: 12 }}>
                                <Card variant="outlined">
                                    <CardContent>
                                        <Typography variant="h6" sx={{ mb: 2 }}>
                                            Address
                                        </Typography>

                                        <Stack spacing={2}>
                                            <Stack direction="row" spacing={1} alignItems="center">
                                                <Home color="primary" />
                                                <Typography>
                                                    Street: {address?.street || "Not provided"}
                                                </Typography>
                                            </Stack>

                                            <Typography>
                                                City: {address?.city || "Not provided"}
                                            </Typography>

                                            <Typography>
                                                Postal code: {address?.postalCode || "Not provided"}
                                            </Typography>
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

export default UserProfilePage;