import React, { useEffect } from "react";
import { Button, FormControlLabel, Stack, Switch, TextField, Box } from "@mui/material";
import useForm from "../../../../common/hooks/useForm.js";
import useProfileActions from "../../../hooks/profile/useProflieActions.js";



const AccountForm = ({ user, onSuccess }) => {
    const { formData, handleChange, resetForm} = useForm({
        username: "",
        email: "",
        notificationsEnabled: true
    });

    const { updateMyAccount } = useProfileActions();

    useEffect(() => {
        if (user) {
            resetForm({
                username: user.username || "",
                email: user.email || "",
                notificationsEnabled: user.notificationsEnabled ?? true
            });
        }
    }, [user, resetForm]);

    const handleFormSubmit = async (event) => {
        event.preventDefault();
        await updateMyAccount(formData, onSuccess);
    };

    return (
        <Box component="form" onSubmit={handleFormSubmit} sx={{ maxWidth: 600, mx: 'auto', mt: 2 }}>
            <Stack spacing={3}>
                <TextField 
                    label="Username" 
                    name="username" 
                    value={formData.username} 
                    onChange={handleChange} 
                    fullWidth 
                    required 
                />
                
                <TextField 
                    label="Email" 
                    name="email" 
                    type="email" 
                    value={formData.email} 
                    onChange={handleChange} 
                    fullWidth 
                    required 
                />
                
                <FormControlLabel
                    control={
                        <Switch 
                            checked={formData.notificationsEnabled} 
                            onChange={handleChange} 
                            name="notificationsEnabled" 
                            color="primary"
                        />
                    }
                    label="Receive Email Notifications"
                />
                
                <Button type="submit" variant="contained" size="large">
                    Save Account Settings
                </Button>
            </Stack>
        </Box>
    );
};

export default AccountForm;
