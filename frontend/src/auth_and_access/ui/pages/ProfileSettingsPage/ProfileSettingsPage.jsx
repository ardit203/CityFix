import { useState } from "react";
import {
    Box,
    CircularProgress,
    Paper,
    Tab,
    Tabs,
    Typography
} from "@mui/material";

import useProfile from "../../../hooks/useProfile.js";
import AccountSettingsForm from "../../components/user/AccountForm/AccountForm.jsx";
import ProfileSettingsForm from "../../components/user/ProfileForm/ProfileForm.jsx";
import ChangePasswordForm from "../../components/user/SecurityForm/SecurityForm.jsx";
import ProfilePictureForm from "../../components/user/ProfilePictureForm/ProfilePictureFrom.jsx";

const ProfileSettingsPage = () => {
    const [activeTab, setActiveTab] = useState(0);

    const {
        user,
        loading,
        error,
        updateProfileUserInState
    } = useProfile();

    const handleTabChange = (event, newValue) => {
        setActiveTab(newValue);
    };

    if (loading) {
        return (
            <Box sx={{ display: "flex", justifyContent: "center", mt: 5 }}>
                <CircularProgress />
            </Box>
        );
    }

    if (error) {
        return <Typography color="error">{error}</Typography>;
    }

    if (!user) {
        return <Typography>Profile not found.</Typography>;
    }

    return (
        <Box>
            <Typography variant="h4" fontWeight={600} sx={{ mb: 3 }}>
                Profile Settings
            </Typography>

            <Paper sx={{ borderRadius: 3 }}>
                <Tabs
                    value={activeTab}
                    onChange={handleTabChange}
                    variant="scrollable"
                    scrollButtons="auto"
                    sx={{ borderBottom: 1, borderColor: "divider" }}
                >
                    <Tab label="Account" />
                    <Tab label="Profile" />
                    <Tab label="Security" />
                    <Tab label="Profile Picture" />
                </Tabs>

                <Box sx={{ p: 3 }}>
                    {activeTab === 0 && (
                        <AccountSettingsForm
                            user={user}
                            onSuccess={updateProfileUserInState}
                        />
                    )}

                    {activeTab === 1 && (
                        <ProfileSettingsForm
                            user={user}
                            onSuccess={updateProfileUserInState}
                        />
                    )}

                    {activeTab === 2 && (
                        <ChangePasswordForm />
                    )}

                    {activeTab === 3 && (
                        <ProfilePictureForm
                            user={user}
                            onSuccess={updateProfileUserInState}
                        />
                    )}
                </Box>
            </Paper>
        </Box>
    );
};

export default ProfileSettingsPage;