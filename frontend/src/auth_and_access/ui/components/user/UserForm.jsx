import React, { useState, useEffect } from "react";
import {
    Box,
    Button,
    Grid,
    MenuItem,
    Stack,
    TextField,
    Typography,
    Card,
    CardContent,
    FormControlLabel,
    Switch
} from "@mui/material";

import useForm from "../../../../common/hooks/useForm.js";

const emptyUser = {
    username: "",
    email: "",
    notificationsEnabled: true,
    profile: {
        name: "",
        surname: "",
        phoneNumber: "",
        dateOfBirth: "",
        gender: "",
        address: {
            street: "",
            city: "",
            postalCode: ""
        }
    }
};

const UserForm = ({
    initialValues = emptyUser,
    submitLabel = "Save",
    onSubmit
}) => {
    const { formData, handleChange, resetForm, handleSubmit } = useForm(emptyUser);
    const [validationErrors, setValidationErrors] = useState({});

    useEffect(() => {
        if (initialValues) {
            resetForm(initialValues);
        }
    }, [initialValues, resetForm]);

    const validate = () => {
        let tempErrors = {};

        if (!formData.username?.trim()) tempErrors.username = "Username is required";
        if (!formData.email?.trim()) tempErrors.email = "Email is required";
        
        if (!formData.profile?.name?.trim()) tempErrors.name = "Name is required";
        if (!formData.profile?.surname?.trim()) tempErrors.surname = "Surname is required";

        setValidationErrors(tempErrors);
        return Object.keys(tempErrors).length === 0;
    };

    return (
        <Box component="form" onSubmit={(event) => handleSubmit(event, validate, onSubmit)}>
            <Grid container spacing={3}>
                
                {/* --- Account Information --- */}
                <Grid item xs={12} md={6}>
                    <Card variant="outlined">
                        <CardContent>
                            <Typography variant="subtitle1" sx={{ mb: 2 }} color="primary">
                                Account Information
                            </Typography>
                            <Stack spacing={2.5}>
                                <TextField
                                    label="Username" name="username"
                                    value={formData.username || ""} onChange={handleChange}
                                    error={!!validationErrors.username} helperText={validationErrors.username}
                                    fullWidth required
                                />
                                <TextField
                                    label="Email" name="email" type="email"
                                    value={formData.email || ""} onChange={handleChange}
                                    error={!!validationErrors.email} helperText={validationErrors.email}
                                    fullWidth required
                                />
                                <FormControlLabel
                                    control={
                                        <Switch
                                            checked={formData.notificationsEnabled ?? true}
                                            onChange={handleChange}
                                            name="notificationsEnabled"
                                            color="primary"
                                        />
                                    }
                                    label="Enable Notifications"
                                />
                            </Stack>
                        </CardContent>
                    </Card>
                </Grid>

                {/* --- Personal Details --- */}
                <Grid item xs={12} md={6}>
                    <Card variant="outlined">
                        <CardContent>
                            <Typography variant="subtitle1" sx={{ mb: 2 }} color="primary">
                                Personal Details
                            </Typography>
                            <Stack spacing={2.5}>
                                <Stack direction="row" spacing={2}>
                                    <TextField
                                        label="First Name" name="profile.name"
                                        value={formData.profile?.name || ""} onChange={handleChange}
                                        error={!!validationErrors.name} helperText={validationErrors.name}
                                        fullWidth required
                                    />
                                    <TextField
                                        label="Last Name" name="profile.surname"
                                        value={formData.profile?.surname || ""} onChange={handleChange}
                                        error={!!validationErrors.surname} helperText={validationErrors.surname}
                                        fullWidth required
                                    />
                                </Stack>
                                <Stack direction="row" spacing={2}>
                                    <TextField
                                        label="Date of Birth" name="profile.dateOfBirth" type="date"
                                        value={formData.profile?.dateOfBirth || ""} onChange={handleChange}
                                        InputLabelProps={{ shrink: true }}
                                        fullWidth
                                    />
                                    <TextField
                                        select label="Gender" name="profile.gender"
                                        value={formData.profile?.gender || ""} onChange={handleChange}
                                        fullWidth
                                    >
                                        <MenuItem value="">Not selected</MenuItem>
                                        <MenuItem value="MALE">Male</MenuItem>
                                        <MenuItem value="FEMALE">Female</MenuItem>
                                        <MenuItem value="OTHER">Other</MenuItem>
                                    </TextField>
                                </Stack>
                                <TextField
                                    label="Phone Number" name="profile.phoneNumber"
                                    value={formData.profile?.phoneNumber || ""} onChange={handleChange}
                                    fullWidth
                                />
                            </Stack>
                        </CardContent>
                    </Card>
                </Grid>

                {/* --- Address Information --- */}
                <Grid item xs={12}>
                    <Card variant="outlined">
                        <CardContent>
                            <Typography variant="subtitle1" sx={{ mb: 2 }} color="primary">
                                Address Information
                            </Typography>
                            <Stack direction={{ xs: 'column', sm: 'row' }} spacing={2}>
                                <TextField
                                    label="Street Address" name="profile.address.street"
                                    value={formData.profile?.address?.street || ""} onChange={handleChange}
                                    fullWidth
                                />
                                <TextField
                                    label="City" name="profile.address.city"
                                    value={formData.profile?.address?.city || ""} onChange={handleChange}
                                    fullWidth
                                />
                                <TextField
                                    label="Postal Code" name="profile.address.postalCode"
                                    value={formData.profile?.address?.postalCode || ""} onChange={handleChange}
                                    sx={{ width: { sm: '200px' } }}
                                />
                            </Stack>
                        </CardContent>
                    </Card>
                </Grid>
            </Grid>

            {/* Standard Submit Button at the bottom */}
            <Box sx={{ mt: 3, display: 'flex', justifyContent: 'flex-end' }}>
                <Button type="submit" variant="contained" size="large">
                    {submitLabel}
                </Button>
            </Box>
        </Box>
    );
};

export default UserForm;
