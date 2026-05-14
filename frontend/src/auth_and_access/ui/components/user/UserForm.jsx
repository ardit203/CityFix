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

import { emptyAdminUpdateUserDto } from "../../../dtos/userDto.js";

const UserForm = ({
    initialValues = emptyAdminUpdateUserDto,
    submitLabel = "Save",
    onSubmit
}) => {
    const { formData, handleChange, resetForm, handleSubmit } = useForm(emptyAdminUpdateUserDto);
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
            <Stack direction={{ xs: 'column', md: 'row' }} spacing={3}>
                
                {/* --- Account Information --- */}
                <Box sx={{ flex: 1 }}>
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
                </Box>

                {/* --- Personal Details --- */}
                <Box sx={{ flex: 1 }}>
                    <Card variant="outlined">
                        <CardContent>
                            <Typography variant="subtitle1" sx={{ mb: 2 }} color="primary">
                                Personal Details
                            </Typography>
                            <Stack spacing={2.5}>
                                <Stack direction="row" spacing={2}>
                                    <TextField
                                        label="First Name" name="name"
                                        value={formData.name || ""} onChange={handleChange}
                                        error={!!validationErrors.name} helperText={validationErrors.name}
                                        fullWidth required
                                    />
                                    <TextField
                                        label="Last Name" name="surname"
                                        value={formData.surname || ""} onChange={handleChange}
                                        error={!!validationErrors.surname} helperText={validationErrors.surname}
                                        fullWidth required
                                    />
                                </Stack>
                                <Stack direction="row" spacing={2}>
                                    <TextField
                                        label="Date of Birth" name="dateOfBirth" type="date"
                                        value={formData.dateOfBirth || ""} onChange={handleChange}
                                        slotProps={{ inputLabel: { shrink: true } }}
                                        fullWidth
                                    />
                                    <TextField
                                        select label="Gender" name="gender"
                                        value={formData.gender || ""} onChange={handleChange}
                                        fullWidth
                                    >
                                        <MenuItem value="">Not selected</MenuItem>
                                        <MenuItem value="MALE">Male</MenuItem>
                                        <MenuItem value="FEMALE">Female</MenuItem>
                                        <MenuItem value="OTHER">Other</MenuItem>
                                    </TextField>
                                </Stack>
                                <TextField
                                    label="Phone Number" name="phoneNumber"
                                    value={formData.phoneNumber || ""} onChange={handleChange}
                                    fullWidth
                                />
                            </Stack>
                        </CardContent>
                    </Card>
                </Box>
            </Stack>

            {/* --- Address Information --- */}
            <Box sx={{ mt: 3 }}>
                    <Card variant="outlined">
                        <CardContent>
                            <Typography variant="subtitle1" sx={{ mb: 2 }} color="primary">
                                Address Information
                            </Typography>
                            <Stack direction={{ xs: 'column', sm: 'row' }} spacing={2}>
                                <TextField
                                    label="Street Address" name="address.street"
                                    value={formData.address?.street || ""} onChange={handleChange}
                                    fullWidth
                                />
                                <TextField
                                    label="City" name="address.city"
                                    value={formData.address?.city || ""} onChange={handleChange}
                                    fullWidth
                                />
                                <TextField
                                    label="Postal Code" name="address.postalCode"
                                    value={formData.address?.postalCode || ""} onChange={handleChange}
                                    sx={{ width: { sm: '200px' } }}
                                />
                            </Stack>
                        </CardContent>
                    </Card>
            </Box>

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
