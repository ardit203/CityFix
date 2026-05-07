import React, {useState} from "react";
import {useNavigate} from "react-router";
import {Box, Paper, Tab, Tabs} from "@mui/material";
import {AccountCircle, Person, Security, AddAPhoto} from "@mui/icons-material";

// Path imports:
import useProfile from "../../hooks/profile/useProfile.js";
import AsyncDataView from "../../../common/ui/components/AsyncDataView.jsx";
import ActionBar from "../../../common/ui/components/ActionBar.jsx";

import AccountForm from "../components/profile/AccountForm.jsx";
import ProfileForm from "../components/profile/ProfileForm.jsx";
import ChangePasswordForm from "../components/profile/ChangePasswordForm.jsx";
import ProfilePictureForm from "../components/profile/ProfilePictureForm.jsx";

const ProfileEditPage = () => {
    const navigate = useNavigate();
    const [activeTab, setActiveTab] = useState(0);

    const {user, loading, error, fetchProfile} = useProfile();

    const handleTabChange = (event, newValue) => {
        setActiveTab(newValue);
    };

    if (loading || error || !user) {
        return (
            <AsyncDataView
                loading={loading}
                error={error}
                data={user}
                notFoundMessage="Profile not found."
                backUrl="/profile"
            />
        );
    }

    return (
        <Box sx={{maxWidth: 1000, mx: 'auto', p: 3}}>

            <ActionBar
                title="Profile Settings"
                onBack={() => navigate("/profile")}
                backLabel="Back to Profile"
            />

            <Paper elevation={2} sx={{borderRadius: 4, overflow: 'hidden'}}>
                <Tabs
                    value={activeTab}
                    onChange={handleTabChange}
                    variant="scrollable"
                    scrollButtons="auto"
                    sx={{borderBottom: 1, borderColor: "divider", px: 2, pt: 1}}
                >
                    <Tab icon={<AccountCircle/>} iconPosition="start" label="Account"/>
                    <Tab icon={<Person/>} iconPosition="start" label="Profile Details"/>
                    <Tab icon={<Security/>} iconPosition="start" label="Security"/>
                    <Tab icon={<AddAPhoto/>} iconPosition="start" label="Profile Picture"/>
                </Tabs>

                <Box sx={{p: {xs: 3, md: 5}}}>
                    {activeTab === 0 && (
                        <AccountForm user={user} onSuccess={fetchProfile}/>
                    )}

                    {activeTab === 1 && (
                        <ProfileForm user={user} onSuccess={fetchProfile}/>
                    )}

                    {activeTab === 2 && (
                        <ChangePasswordForm/>
                    )}

                    {activeTab === 3 && (
                        <ProfilePictureForm user={user} onSuccess={fetchProfile}/>
                    )}
                </Box>
            </Paper>
        </Box>
    );
};

export default ProfileEditPage;
