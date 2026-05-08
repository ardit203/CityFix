import { useState } from "react";
import {
    Box,
    Button,
    Container,
    Paper,
    TextField,
    Typography,
    Alert
} from "@mui/material";
import { useNavigate, useSearchParams } from "react-router";

import useForm from "../../../common/hooks/useForm.js";
import usePasswordReset from "../../hooks/auth/usePasswordReset.js";

import { emptyResetPasswordDto } from "../../dtos/authDto.js";

const ResetPasswordPage = () => {
    const navigate = useNavigate();
    const [searchParams] = useSearchParams();
    const token = searchParams.get("token");

    const { formData, handleChange, handleSubmit } = useForm(emptyResetPasswordDto);
    const [validationErrors, setValidationErrors] = useState({});

    // Pulling in the custom hook
    const { resetPassword, loading, error: apiError } = usePasswordReset();

    const validate = () => {
        let tempErrors = {};
        
        if (!formData.newPassword) tempErrors.newPassword = "Password is required";
        if (!formData.confirmPassword) {
            tempErrors.confirmPassword = "Confirm password is required";
        } else if (formData.newPassword !== formData.confirmPassword) {
            tempErrors.confirmPassword = "Passwords do not match";
        }

        setValidationErrors(tempErrors);
        return Object.keys(tempErrors).length === 0;
    };

    const handleFormSubmit = async (event) => {
        event.preventDefault();
        
        if (validate()) {
            // The API DTO expects { token, newPassword, confirmPassword }
            await resetPassword({
                token,
                newPassword: formData.newPassword,
                confirmPassword: formData.confirmPassword
            });
        }
    };

    if (!token) {
        return (
            <Container maxWidth="sm">
                <Paper elevation={3} sx={{ padding: 4, mt: 8 }}>
                    <Typography variant="h5" align="center" gutterBottom color="error">
                        Invalid Reset Link
                    </Typography>

                    <Typography color="text.secondary" align="center" sx={{ mb: 3 }}>
                        The reset link is missing a token. Please request a new password reset email.
                    </Typography>

                    <Button
                        fullWidth
                        variant="contained"
                        onClick={() => navigate("/forgot-password")}
                    >
                        Request New Reset Link
                    </Button>
                </Paper>
            </Container>
        );
    }

    return (
        <Container maxWidth="sm">
            <Paper elevation={3} sx={{ padding: 4, mt: 8 }}>
                <Typography variant="h5" align="center" gutterBottom>
                    Reset Password
                </Typography>

                <Typography
                    variant="body2"
                    color="text.secondary"
                    align="center"
                    sx={{ mb: 3 }}
                >
                    Enter your new password below.
                </Typography>

                {apiError && (
                    <Alert severity="error" sx={{ mb: 3 }}>
                        {apiError}
                    </Alert>
                )}

                <Box component="form" onSubmit={handleFormSubmit} noValidate>
                    <TextField
                        fullWidth
                        label="New Password"
                        name="newPassword"
                        type="password"
                        margin="normal"
                        required
                        value={formData.newPassword}
                        onChange={handleChange}
                        error={!!validationErrors.newPassword}
                        helperText={validationErrors.newPassword}
                        autoFocus
                    />

                    <TextField
                        fullWidth
                        label="Confirm Password"
                        name="confirmPassword"
                        type="password"
                        margin="normal"
                        required
                        value={formData.confirmPassword}
                        onChange={handleChange}
                        error={!!validationErrors.confirmPassword}
                        helperText={validationErrors.confirmPassword}
                    />

                    <Button
                        fullWidth
                        variant="contained"
                        type="submit"
                        sx={{ mt: 3 }}
                        disabled={loading}
                    >
                        {loading ? "Resetting..." : "Reset Password"}
                    </Button>

                    <Button
                        fullWidth
                        variant="outlined"
                        sx={{ mt: 1 }}
                        onClick={() => navigate("/login")}
                        disabled={loading}
                    >
                        Back to Login
                    </Button>
                </Box>
            </Paper>
        </Container>
    );
};

export default ResetPasswordPage;
