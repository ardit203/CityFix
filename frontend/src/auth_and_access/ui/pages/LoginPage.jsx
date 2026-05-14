import React, { useState } from "react";
import {
    Box,
    TextField,
    Button,
    Typography,
    Container,
    Paper,
    Alert
} from "@mui/material";
import { useNavigate } from "react-router";
import useForm from "../../../common/hooks/useForm.js";
import useLogin from "../../hooks/auth/useLogin.js";

import { emptyLoginDto } from "../../dtos/authDto.js";

const LoginPage = () => {
    const navigate = useNavigate();
    const { formData, handleChange, handleSubmit } = useForm(emptyLoginDto);
    const [validationErrors, setValidationErrors] = useState({});
    const { login, loading, error: apiError } = useLogin();

    const validate = () => {
        let tempErrors = {};

        if (!formData.username.trim()) tempErrors.username = "Username is required";
        if (!formData.password) tempErrors.password = "Password is required";

        setValidationErrors(tempErrors);
        return Object.keys(tempErrors).length === 0;
    };

    return (
        <Box className="auth-page">
            <Container maxWidth="sm">
                <Paper elevation={3} className="auth-card" sx={{ p: {xs: 3, sm: 4.5} }}>
                    <Box className="auth-heading">
                        <Box className="auth-logo">C</Box>
                        <Box>
                            <Typography variant="h4">
                                Welcome back
                            </Typography>
                            <Typography variant="body2" color="text.secondary">
                                Sign in to continue managing CityFix.
                            </Typography>
                        </Box>
                    </Box>

                    {apiError && (
                        <Alert severity="error" sx={{ mb: 2 }}>
                            {apiError}
                        </Alert>
                    )}

                    <Box component="form" onSubmit={(event) => handleSubmit(event, validate, login)} noValidate>
                        <TextField
                            fullWidth
                            label="Username"
                            name="username"
                            margin="normal"
                            required
                            value={formData.username}
                            onChange={handleChange}
                            error={!!validationErrors.username}
                            helperText={validationErrors.username}
                            autoFocus
                        />

                        <TextField
                            fullWidth
                            label="Password"
                            name="password"
                            type="password"
                            margin="normal"
                            required
                            value={formData.password}
                            onChange={handleChange}
                            error={!!validationErrors.password}
                            helperText={validationErrors.password}
                        />

                        <Button
                            fullWidth
                            variant="text"
                            sx={{ mt: 1}}
                            onClick={() => navigate("/forgot-password")}
                        >
                            Forgot password?
                        </Button>

                        <Button
                            fullWidth
                            variant="contained"
                            type="submit"
                            disabled={loading}
                            sx={{ mt: 2 }}
                        >
                            {loading ? "Logging in..." : "Login"}
                        </Button>

                        <Button
                            fullWidth
                            variant="outlined"
                            sx={{ mt: 1 }}
                            disabled={loading}
                            onClick={() => navigate("/register")}
                        >
                            Create an account
                        </Button>
                    </Box>
                </Paper>
            </Container>
        </Box>
    );
};

export default LoginPage;
