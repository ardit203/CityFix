import React from "react";
import {
    Box,
    Button,
    Container,
    Divider,
    Paper,
    Stack,
    Typography
} from "@mui/material";
import GppMaybeOutlinedIcon from "@mui/icons-material/GppMaybeOutlined";
import ArrowBackRoundedIcon from "@mui/icons-material/ArrowBackRounded";
import HomeRoundedIcon from "@mui/icons-material/HomeRounded";
import LoginRoundedIcon from "@mui/icons-material/LoginRounded";
import {useNavigate} from "react-router";
import useAuth from "../../hooks/auth/useAuth.js";

const UnauthorizedPage = () => {
    const navigate = useNavigate();
    const {isLoggedIn} = useAuth();

    return (
        <Box className="auth-page">
            <Container maxWidth="md">
                <Paper
                    elevation={3}
                    className="auth-card"
                    sx={{
                        p: {xs: 3, sm: 4.5},
                        display: "grid",
                        gridTemplateColumns: {xs: "1fr", md: "0.82fr 1.18fr"},
                        gap: {xs: 3, md: 4},
                        alignItems: "center"
                    }}
                >
                    <Box
                        sx={{
                            display: "grid",
                            placeItems: "center",
                            minHeight: {xs: 190, sm: 230},
                            borderRadius: 4,
                            border: "1px solid rgba(15, 143, 206, 0.14)",
                            background:
                                "linear-gradient(135deg, rgba(15, 143, 206, 0.16), rgba(22, 166, 115, 0.1))"
                        }}
                    >
                        <Box
                            sx={{
                                width: {xs: 110, sm: 138},
                                height: {xs: 110, sm: 138},
                                display: "grid",
                                placeItems: "center",
                                borderRadius: "34px",
                                color: "primary.main",
                                backgroundColor: "rgba(255, 255, 255, 0.76)",
                                boxShadow: "0 22px 46px rgba(15, 143, 206, 0.2)"
                            }}
                        >
                            <GppMaybeOutlinedIcon sx={{fontSize: {xs: 62, sm: 78}}} />
                        </Box>
                    </Box>

                    <Box>
                        <Typography
                            variant="overline"
                            sx={{
                                color: "primary.main",
                                fontWeight: 800,
                                letterSpacing: 0
                            }}
                        >
                            Access restricted
                        </Typography>

                        <Typography
                            variant="h3"
                            sx={{
                                mt: 0.5,
                                fontFamily: "var(--font-display)",
                                fontSize: {xs: "2rem", sm: "2.75rem"},
                                lineHeight: 1.05
                            }}
                        >
                            You do not have permission to view this page.
                        </Typography>

                        <Typography color="text.secondary" sx={{mt: 2, maxWidth: 560, lineHeight: 1.7}}>
                            Your account is active, but this area is limited to another role. Go back to the previous
                            page or return to the CityFix dashboard.
                        </Typography>

                        <Divider sx={{my: 3}} />

                        <Stack direction={{xs: "column", sm: "row"}} spacing={1.5}>
                            <Button
                                variant="contained"
                                startIcon={<HomeRoundedIcon />}
                                onClick={() => navigate("/")}
                            >
                                Go to dashboard
                            </Button>

                            <Button
                                variant="outlined"
                                startIcon={<ArrowBackRoundedIcon />}
                                onClick={() => navigate(-1)}
                            >
                                Go back
                            </Button>

                            {!isLoggedIn && (
                                <Button
                                    variant="text"
                                    startIcon={<LoginRoundedIcon />}
                                    onClick={() => navigate("/login")}
                                >
                                    Login
                                </Button>
                            )}
                        </Stack>
                    </Box>
                </Paper>
            </Container>
        </Box>
    );
};

export default UnauthorizedPage;
