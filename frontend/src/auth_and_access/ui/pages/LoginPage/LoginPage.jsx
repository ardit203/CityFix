import React, { useState } from "react";
import {
    Box,
    TextField,
    Button,
    Typography,
    Container,
    Paper
} from "@mui/material";
import { useNavigate } from "react-router";
import useAuth from "../../../hooks/useAuth.js";
import authService from "../../../services/authService.js";

const initialFormData = {
    username: "",
    password: "",
};

const LoginPage = () => {
    const navigate = useNavigate();

    const [formData, setFormData] = useState(initialFormData);

    const { login } = useAuth();

    const handleChange = (event) => {
        const { name, value } = event.target;

        setFormData((prev) => ({
            ...prev,
            [name]: value
        }));
    };

    const handleSubmit = (event) => {
        event.preventDefault();

        authService
            .login(formData)
            .then((response) => {
                console.log("The user is successfully logged in.");
                login(response.data.token);
                navigate("/");
            })
            .catch((error) => console.log(error));
    };

    return (
        <Container maxWidth="sm">
            <Paper elevation={3} sx={{ padding: 4, mt: 8 }}>
                <Typography variant="h5" align="center" gutterBottom>
                    Login
                </Typography>

                <Box component="form" onSubmit={handleSubmit}>
                    <TextField
                        fullWidth
                        label="Username"
                        name="username"
                        margin="normal"
                        required
                        value={formData.username}
                        onChange={handleChange}
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
                        sx={{ mt: 2 }}
                    >
                        Login
                    </Button>



                    <Button
                        fullWidth
                        variant="outlined"
                        sx={{ mt: 1 }}
                        onClick={() => navigate("/register")}
                    >
                        Register
                    </Button>
                </Box>
            </Paper>
        </Container>
    );
};

export default LoginPage;