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
import { useNavigate } from "react-router";

import usePasswordReset from "../../hooks/auth/usePasswordReset.js";

const ForgotPasswordPage = () => {
    const navigate = useNavigate();
    
    const [email, setEmail] = useState("");
    const [emailSent, setEmailSent] = useState(false);

    // Using the custom hook for the API call
    const { requestPasswordReset, loading, error: apiError } = usePasswordReset();

    const handleSubmit = async (event) => {
        event.preventDefault();
        
        if (!email.trim()) return;

        // The hook returns true if successful
        const success = await requestPasswordReset(email);
        if (success) {
            setEmailSent(true);
        }
    };

    const handleResend = async () => {
        await requestPasswordReset(email);
    };

    return (
        <Box className="auth-page">
            <Container maxWidth="sm">
                <Paper elevation={3} className="auth-card" sx={{ p: {xs: 3, sm: 4.5} }}>
                    <Box className="auth-heading">
                        <Box className="auth-logo">C</Box>
                        <Box>
                            <Typography variant="h4">
                                Reset access
                            </Typography>
                            <Typography variant="body2" color="text.secondary">
                                We will send a recovery link to your email.
                            </Typography>
                        </Box>
                    </Box>

                    {apiError && (
                        <Alert severity="error" sx={{ mb: 3 }}>
                            {apiError}
                        </Alert>
                    )}

                    {!emailSent ? (
                        <>
                            <Typography
                                variant="body2"
                                color="text.secondary"
                                align="center"
                                sx={{ mb: 3 }}
                            >
                                Enter your email address and we will send you a password reset link.
                            </Typography>

                            <Box component="form" onSubmit={handleSubmit} noValidate>
                                <TextField
                                    fullWidth
                                    label="Email"
                                    name="email"
                                    type="email"
                                    margin="normal"
                                    required
                                    value={email}
                                    onChange={(e) => setEmail(e.target.value)}
                                    autoFocus
                                />

                                <Button
                                    fullWidth
                                    variant="contained"
                                    type="submit"
                                    sx={{ mt: 2 }}
                                    disabled={loading || !email.trim()}
                                >
                                    {loading ? "Sending..." : "Send Reset Email"}
                                </Button>

                                <Button
                                    fullWidth
                                    variant="outlined"
                                    sx={{ mt: 2 }}
                                    onClick={() => navigate("/login")}
                                    disabled={loading}
                                >
                                    Back to Login
                                </Button>
                            </Box>
                        </>
                    ) : (
                        <>
                            <Typography
                                variant="h6"
                                align="center"
                                sx={{ mt: 2, mb: 1 }}
                            >
                                Check your email
                            </Typography>

                            <Typography
                                variant="body2"
                                color="text.secondary"
                                align="center"
                                sx={{ mb: 3 }}
                            >
                                We sent a password reset link to {email}. Open the link from your email to continue.
                            </Typography>

                            <Button
                                fullWidth
                                variant="contained"
                                onClick={handleResend}
                                disabled={loading}
                                sx={{ mt: 2 }}
                            >
                                {loading ? "Resending..." : "Resend Email"}
                            </Button>

                            <Button
                                fullWidth
                                variant="outlined"
                                sx={{ mt: 2 }}
                                onClick={() => navigate("/login")}
                                disabled={loading}
                            >
                                Back to Login
                            </Button>
                        </>
                    )}
                </Paper>
            </Container>
        </Box>
    );
};

export default ForgotPasswordPage;
