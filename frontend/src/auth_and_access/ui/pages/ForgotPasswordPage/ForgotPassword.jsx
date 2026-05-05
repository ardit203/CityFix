import { useState } from "react";
import {
    Box,
    Button,
    Container,
    Paper,
    TextField,
    Typography
} from "@mui/material";
import { useNavigate } from "react-router";
import useSnackbar from "../../../../common/hooks/useSnackbar.js";

const ForgotPasswordPage = () => {
    const navigate = useNavigate();
    const { showSnackbar } = useSnackbar();

    const [email, setEmail] = useState("");
    const [loading, setLoading] = useState(false);
    const [emailSent, setEmailSent] = useState(false);

    const sendResetEmail = async () => {
        setLoading(true);

        try {
            // await authService.requestPasswordReset({
            //     email
            // });

            setEmailSent(true);
            showSnackbar("Password reset instructions were sent to your email.", "success");
        } catch (error) {
            const message =
                error.response?.data?.message ||
                error.response?.data?.error ||
                error.message ||
                "Failed to send password reset request";

            showSnackbar(message, "error");
        } finally {
            setLoading(false);
        }
    };

    const handleSubmit = async (event) => {
        event.preventDefault();
        await sendResetEmail();
    };

    const handleResend = async () => {
        await sendResetEmail();
    };

    return (
        <Container maxWidth="sm">
            <Paper elevation={3} sx={{ padding: 4, mt: 8 }}>
                <Typography variant="h5" align="center" gutterBottom>
                    Forgot Password
                </Typography>

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

                        <Box component="form" onSubmit={handleSubmit}>
                            <TextField
                                fullWidth
                                label="Email"
                                name="email"
                                type="email"
                                margin="normal"
                                required
                                value={email}
                                onChange={(e) => setEmail(e.target.value)}
                            />

                            <Button
                                fullWidth
                                variant="contained"
                                type="submit"
                                sx={{ mt: 2 }}
                                disabled={loading}
                            >
                                {loading ? "Sending..." : "Send Reset Email"}
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
                    </>
                ) : (
                    <>
                        <Typography
                            variant="body1"
                            align="center"
                            sx={{ mt: 2, mb: 1 }}
                        >
                            Check your email.
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
                        >
                            Back to Login
                        </Button>
                    </>
                )}
            </Paper>
        </Container>
    );
};

export default ForgotPasswordPage;