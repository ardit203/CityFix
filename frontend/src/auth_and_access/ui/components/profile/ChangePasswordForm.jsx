import React, { useState } from "react";
import { Button, Stack, TextField, Box } from "@mui/material";
import useProfileActions from "../../../hooks/profile/useProflieActions.js";



const initialFormData = {
    currentPassword: "",
    newPassword: "",
    confirmNewPassword: ""
};

const ChangePasswordForm = () => {
    const [formData, setFormData] = useState(initialFormData);
    const { changeMyPassword } = useProfileActions();

    const handleChange = (event) => {
        const { name, value } = event.target;
        setFormData(prev => ({ ...prev, [name]: value }));
    };

    const handleSubmit = async (event) => {
        event.preventDefault();
        const success = await changeMyPassword(formData);
        if (success) {
            setFormData(initialFormData);
        }
    };

    return (
        <Box component="form" onSubmit={handleSubmit} sx={{ maxWidth: 600, mx: 'auto', mt: 2 }}>
            <Stack spacing={3}>
                <TextField 
                    label="Current Password" 
                    name="currentPassword" 
                    type="password" 
                    value={formData.currentPassword} 
                    onChange={handleChange} 
                    fullWidth 
                    required 
                />
                
                <TextField 
                    label="New Password" 
                    name="newPassword" 
                    type="password" 
                    value={formData.newPassword} 
                    onChange={handleChange} 
                    fullWidth 
                    required 
                />
                
                <TextField 
                    label="Confirm New Password" 
                    name="confirmNewPassword" 
                    type="password" 
                    value={formData.confirmNewPassword} 
                    onChange={handleChange} 
                    fullWidth 
                    required 
                />
                
                <Button type="submit" variant="contained" color="primary" size="large">
                    Update Password
                </Button>
            </Stack>
        </Box>
    );
};

export default ChangePasswordForm;
