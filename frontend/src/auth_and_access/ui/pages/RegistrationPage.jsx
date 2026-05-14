import React, { useState } from "react";
import {
    Box,
    Button,
    Container,
    Paper,
    Divider,
    MenuItem,
    Stack,
    TextField,
    Typography,
    Alert,
    Grid
} from "@mui/material";
import { useNavigate } from "react-router";

import useForm from "../../../common/hooks/useForm.js";
import useRegister from "../../hooks/auth/useRegister.js";

import { emptyRegisterDto } from "../../dtos/authDto.js";

const RegisterPage = () => {
    const navigate = useNavigate();

    const { formData, handleChange, handleSubmit } = useForm(emptyRegisterDto);
    const [validationErrors, setValidationErrors] = useState({});

    const { register, loading, error: apiError } = useRegister();

    const validate = () => {
        let tempErrors = {};

        if (!formData.username.trim()) tempErrors.username = "Username is required";

        if (!formData.email.trim()) {
            tempErrors.email = "Email is required";
        } else if (!/^[^\s@]+@[^\s@]+\.[^\s@]+$/.test(formData.email)) {
            tempErrors.email = "Invalid email format";
        }

        if (!formData.password) tempErrors.password = "Password is required";

        if (!formData.confirmPassword) {
            tempErrors.confirmPassword = "Confirm password is required";
        } else if (formData.password !== formData.confirmPassword) {
            tempErrors.confirmPassword = "Passwords do not match";
        }

        if (!formData.name.trim()) tempErrors.name = "Name is required";
        if (!formData.surname.trim()) tempErrors.surname = "Surname is required";

        if (formData.phoneNumber && !/^\+?[1-9]\d{7,14}$/.test(formData.phoneNumber)) {
            tempErrors.phoneNumber = "Invalid phone format (e.g., +1234567890)";
        }

        setValidationErrors(tempErrors);
        return Object.keys(tempErrors).length === 0;
    };

    return (
        <Box className="auth-page">
            <Container maxWidth="md">
                <Paper elevation={3} className="auth-card" sx={{ p: { xs: 3, md: 4.5 } }}>
                    <Box className="auth-heading">
                        <Box className="auth-logo">C</Box>
                        <Box>
                            <Typography variant="h4">
                                Create your account
                            </Typography>
                            <Typography variant="body2" color="text.secondary">
                                Join CityFix to report issues and follow their progress.
                            </Typography>
                        </Box>
                    </Box>

                    {apiError && (
                        <Alert severity="error" sx={{ mb: 3 }}>
                            {apiError}
                        </Alert>
                    )}

                    <Box component="form" onSubmit={(event) => handleSubmit(event, validate, register)} noValidate>
                        <Grid container spacing={3}>

                            <Grid item xs={12} md={6}>
                                <Typography className="form-section-title" sx={{ mb: 2 }}>
                                    Account Details
                                </Typography>
                                <Stack spacing={2.5}>
                                <TextField
                                    label="Username" name="username"
                                    value={formData.username} onChange={handleChange}
                                    error={!!validationErrors.username} helperText={validationErrors.username}
                                    fullWidth required
                                />
                                <TextField
                                    label="Email Address" name="email" type="email"
                                    value={formData.email} onChange={handleChange}
                                    error={!!validationErrors.email} helperText={validationErrors.email}
                                    fullWidth required
                                />
                                <TextField
                                    label="Password" name="password" type="password"
                                    value={formData.password} onChange={handleChange}
                                    error={!!validationErrors.password} helperText={validationErrors.password}
                                    fullWidth required
                                />
                                <TextField
                                    label="Confirm Password" name="confirmPassword" type="password"
                                    value={formData.confirmPassword} onChange={handleChange}
                                    error={!!validationErrors.confirmPassword}
                                    helperText={validationErrors.confirmPassword}
                                    fullWidth required
                                />
                                </Stack>
                            </Grid>

                            <Grid item xs={12} md={6}>
                                <Typography className="form-section-title" sx={{ mb: 2 }}>
                                    Personal Details
                                </Typography>
                                <Stack spacing={2.5}>
                                <Stack direction={{xs: "column", sm: "row"}} spacing={2}>
                                    <TextField
                                        label="First Name" name="name"
                                        value={formData.name} onChange={handleChange}
                                        error={!!validationErrors.name} helperText={validationErrors.name}
                                        fullWidth required
                                    />
                                    <TextField
                                        label="Last Name" name="surname"
                                        value={formData.surname} onChange={handleChange}
                                        error={!!validationErrors.surname} helperText={validationErrors.surname}
                                        fullWidth required
                                    />
                                </Stack>

                                <Stack direction={{xs: "column", sm: "row"}} spacing={2}>
                                    <TextField
                                        label="Date of Birth" name="dateOfBirth" type="date"
                                        value={formData.dateOfBirth} onChange={handleChange}
                                        InputLabelProps={{ shrink: true }}
                                        fullWidth
                                        slotProps={{
                                            inputLabel: { shrink: true }
                                        }}
                                    />
                                    <TextField
                                        select label="Gender" name="gender"
                                        value={formData.gender} onChange={handleChange}
                                        fullWidth
                                    >
                                        <MenuItem value="MALE">Male</MenuItem>
                                        <MenuItem value="FEMALE">Female</MenuItem>
                                        <MenuItem value="OTHER">Other</MenuItem>
                                    </TextField>
                                </Stack>

                                <TextField
                                    label="Phone Number" name="phoneNumber"
                                    value={formData.phoneNumber} onChange={handleChange}
                                    error={!!validationErrors.phoneNumber}
                                    helperText={validationErrors.phoneNumber || "e.g. +38970123456"}
                                    fullWidth
                                />
                                </Stack>
                            </Grid>

                            <Grid item xs={12}>
                                <Divider sx={{ my: 1 }} />
                                <Typography className="form-section-title" sx={{ mt: 1, mb: 2 }}>
                                    Address (Optional)
                                </Typography>
                                <Stack direction={{ xs: 'column', sm: 'row' }} spacing={2}>
                                <TextField
                                    label="Street Address" name="address.street"
                                    value={formData.address.street} onChange={handleChange}
                                    fullWidth
                                />
                                <TextField
                                    label="City" name="address.city"
                                    value={formData.address.city} onChange={handleChange}
                                    fullWidth
                                />
                                <TextField
                                    label="Postal Code" name="address.postalCode"
                                    value={formData.address.postalCode} onChange={handleChange}
                                    sx={{ width: { sm: '200px' } }}
                                />
                                </Stack>
                            </Grid>

                        </Grid>

                        <Box sx={{ mt: 5 }}>
                            <Button
                                fullWidth
                                variant="contained"
                                type="submit"
                                disabled={loading}
                                sx={{ mt: 2 }}
                            >
                                {loading ? "Registering..." : "Register"}
                            </Button>

                            <Button
                                fullWidth
                                variant="outlined"
                                sx={{ mt: 1 }}
                                disabled={loading}
                                onClick={() => navigate("/login")}
                            >
                                Already have an account? Login
                            </Button>
                        </Box>

                    </Box>
                </Paper>
            </Container>
        </Box>
    );
};

export default RegisterPage;
