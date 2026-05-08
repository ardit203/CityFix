import React, { useEffect } from "react";
import { Button, MenuItem, TextField, Box, Grid } from "@mui/material";
import useForm from "../../../../common/hooks/useForm.js";
import useProfileActions from "../../../hooks/profile/useProflieActions.js";



import { emptyUpdateMyProfileDto } from "../../../dtos/authDto.js";

const ProfileForm = ({ user, onSuccess }) => {
    const { formData, handleChange, resetForm } = useForm(emptyUpdateMyProfileDto);

    const { updateMyProfile } = useProfileActions();

    useEffect(() => {
        if (user && user.profile) {
            resetForm(user.profile);
        }
    }, [user, resetForm]);

    const handleSubmit = async (event) => {
        event.preventDefault();
        await updateMyProfile(formData, onSuccess);
    };

    return (
        <Box component="form" onSubmit={handleSubmit} sx={{ maxWidth: 800, mx: 'auto', mt: 2 }}>
            <Grid container spacing={3}>
                <Grid item xs={12} sm={6}>
                    <TextField label="First Name" name="name" value={formData.name} onChange={handleChange} fullWidth required />
                </Grid>
                <Grid item xs={12} sm={6}>
                    <TextField label="Last Name" name="surname" value={formData.surname} onChange={handleChange} fullWidth required />
                </Grid>
                
                <Grid item xs={12} sm={6}>
                    <TextField label="Phone Number" name="phoneNumber" value={formData.phoneNumber} onChange={handleChange} fullWidth />
                </Grid>
                <Grid item xs={12} sm={6}>
                    <TextField 
                        label="Date of Birth" 
                        name="dateOfBirth" 
                        type="date" 
                        value={formData.dateOfBirth} 
                        onChange={handleChange} 
                        InputLabelProps={{ shrink: true }} 
                        fullWidth 
                    />
                </Grid>
                
                <Grid item xs={12}>
                    <TextField select label="Gender" name="gender" value={formData.gender} onChange={handleChange} fullWidth>
                        <MenuItem value="">Not selected</MenuItem>
                        <MenuItem value="MALE">Male</MenuItem>
                        <MenuItem value="FEMALE">Female</MenuItem>
                        <MenuItem value="OTHER">Other</MenuItem>
                    </TextField>
                </Grid>
                
                <Grid item xs={12}>
                    <TextField label="Street Address" name="address.street" value={formData.address.street} onChange={handleChange} fullWidth />
                </Grid>
                
                <Grid item xs={12} sm={8}>
                    <TextField label="City" name="address.city" value={formData.address.city} onChange={handleChange} fullWidth />
                </Grid>
                <Grid item xs={12} sm={4}>
                    <TextField label="Postal Code" name="address.postalCode" value={formData.address.postalCode} onChange={handleChange} fullWidth />
                </Grid>
                
                <Grid item xs={12} sx={{ mt: 2 }}>
                    <Button type="submit" variant="contained" size="large" fullWidth>
                        Save Profile Details
                    </Button>
                </Grid>
            </Grid>
        </Box>
    );
};

export default ProfileForm;
