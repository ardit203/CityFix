import { useState } from "react";
import {
    Box,
    Button,
    Container,
    Paper,
    TextField,
    Typography
} from "@mui/material";
import { useNavigate, useSearchParams } from "react-router";
import authApi from "../../../service/authApi.js";
import useSnackbar from "../../../../common/hooks/useSnackbar.js";

const initialFormData = {
    newPassword: "",
    confirmPassword: ""
};

const ResetPasswordPage = () => {
    const navigate = useNavigate();
    const [searchParams] = useSearchParams();
    const { showSnackbar } = useSnackbar();

    const token = searchParams.get("token");

    const [formData, setFormData] = useState(initialFormData);
    const [loading, setLoading] = useState(false);

    const handleChange = (event) => {
        const { name, value } = event.target;

        setFormData((prev) => ({
            ...prev,
            [name]: value
        }));
    };

    const handleSubmit = async (event) => {
        event.preventDefault();

        if (!token) {
            showSnackbar("Invalid or missing reset token", "error");
            return;
        }

        if (formData.newPassword !== formData.confirmPassword) {
            showSnackbar("Passwords do not match", "error");
            return;
        }

        setLoading(true);

        try {
            // await authApi.resetPassword({
            //     token,
            //     newPassword: formData.newPassword,
            //     confirmPassword: formData.confirmPassword
            // });

            showSnackbar("Password reset successfully", "success");

            setFormData(initialFormData);
            navigate("/login");
        } catch (error) {
            const message =
                error.response?.data?.message ||
                error.response?.data?.error ||
                error.message ||
                "Failed to reset password";

            showSnackbar(message, "error");
        } finally {
            setLoading(false);
        }
    };

    if (!token) {
        return (
            <Container maxWidth="sm">
                <Paper elevation={3} sx={{ padding: 4, mt: 8 }}>
                    <Typography variant="h5" align="center" gutterBottom>
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

                <Box component="form" onSubmit={handleSubmit}>
                    <TextField
                        fullWidth
                        label="New Password"
                        name="newPassword"
                        type="password"
                        margin="normal"
                        required
                        value={formData.newPassword}
                        onChange={handleChange}
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
                    />

                    <Button
                        fullWidth
                        variant="contained"
                        type="submit"
                        sx={{ mt: 2 }}
                        disabled={loading}
                    >
                        {loading ? "Resetting..." : "Reset Password"}
                    </Button>

                    <Button
                        fullWidth
                        variant="outlined"
                        sx={{ mt: 2 }}
                        onClick={() => navigate("/login")}
                    >
                        Back to Login
                    </Button>
                </Box>
            </Paper>
        </Container>
    );
};

export default ResetPasswordPage;