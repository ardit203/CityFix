import React, { useState } from "react";
import {
    Box,
    TextField,
    Button,
    Typography,
    Container,
    Paper,
    InputLabel,
    Select,
    MenuItem,
    FormControl
} from "@mui/material";
import { useNavigate } from "react-router";
import authApi from "../../../service/authApi.js";
import useSnackbar from "../../../../common/hooks/useSnackbar.js";

const initialFormData = {
    name: "",
    surname: "",
    username: "",
    email: "",
    password: "",
    confirmPassword: "",
    phoneNumber: "",
    dateOfBirth: "",
    gender: "",
    address: {
        street: "",
        city: "",
        postalCode: ""
    }
};

const RegisterPage = () => {
    const navigate = useNavigate();

    const [formData, setFormData] = useState(initialFormData);
    const { showSnackbar } = useSnackbar();

    const handleChange = (event) => {
        const { name, value } = event.target;

        setFormData((prev) => ({
            ...prev,
            [name]: value
        }));
    };

    const handleAddressChange = (event) => {
        const { name, value } = event.target;

        setFormData((prev) => ({
            ...prev,
            address: {
                ...prev.address,
                [name]: value
            }
        }));
    };

    const handleSubmit = (event) => {
        event.preventDefault();

        const requestBody = {
            username: formData.username,
            email: formData.email,
            password: formData.password,
            confirmPassword: formData.confirmPassword,
            name: formData.name,
            surname: formData.surname,
            address: {
                street: formData.address.street,
                city: formData.address.city,
                postalCode: formData.address.postalCode
            },
            dateOfBirth: formData.dateOfBirth || null,
            gender: formData.gender || null,
            phoneNumber: formData.phoneNumber || null
        };

        console.log(requestBody)

        authApi
            .register(requestBody)
            .then(() => {
                showSnackbar("User registered successfully.", "success");
                setFormData(initialFormData);
                navigate("/login");
            })
            .catch((error) => {
                const message =
                    error.response?.data?.message ||
                    error.response?.data?.error ||
                    error.message ||
                    "Failed to register user";

                showSnackbar(message, "error");
            });
    };

    return (
        <Container maxWidth="sm">
            <Paper elevation={3} sx={{ padding: 4, mt: 4 }}>
                <Typography variant="h5" align="center" gutterBottom>
                    Register
                </Typography>

                <Box component="form" onSubmit={handleSubmit}>
                    <TextField
                        fullWidth
                        label="Name"
                        name="name"
                        margin="normal"
                        required
                        value={formData.name}
                        onChange={handleChange}
                    />

                    <TextField
                        fullWidth
                        label="Surname"
                        name="surname"
                        margin="normal"
                        required
                        value={formData.surname}
                        onChange={handleChange}
                    />

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
                        label="Email"
                        name="email"
                        type="email"
                        margin="normal"
                        required
                        value={formData.email}
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

                    <TextField
                        fullWidth
                        label="Phone number"
                        name="phoneNumber"
                        margin="normal"
                        value={formData.phoneNumber}
                        onChange={handleChange}
                    />

                    <TextField
                        fullWidth
                        label="Date of birth"
                        name="dateOfBirth"
                        type="date"
                        margin="normal"
                        value={formData.dateOfBirth}
                        onChange={handleChange}
                        slotProps={{
                            inputLabel: {
                                shrink: true
                            }
                        }}
                    />

                    <FormControl fullWidth margin="normal">
                        <InputLabel>Gender</InputLabel>
                        <Select
                            name="gender"
                            label="Gender"
                            value={formData.gender}
                            onChange={handleChange}
                        >
                            <MenuItem value="">Not selected</MenuItem>
                            <MenuItem value="MALE">Male</MenuItem>
                            <MenuItem value="FEMALE">Female</MenuItem>
                            <MenuItem value="OTHER">Other</MenuItem>
                        </Select>
                    </FormControl>

                    <TextField
                        fullWidth
                        label="Street"
                        name="street"
                        margin="normal"
                        required
                        value={formData.address.street}
                        onChange={handleAddressChange}
                    />

                    <TextField
                        fullWidth
                        label="City"
                        name="city"
                        margin="normal"
                        required
                        value={formData.address.city}
                        onChange={handleAddressChange}
                    />

                    <TextField
                        fullWidth
                        label="Postal code"
                        name="postalCode"
                        margin="normal"
                        value={formData.address.postalCode}
                        onChange={handleAddressChange}
                    />

                    <Button
                        fullWidth
                        variant="contained"
                        type="submit"
                        sx={{ mt: 2 }}
                    >
                        Register
                    </Button>
                    <Button
                        fullWidth
                        variant="outlined"
                        type="button"
                        sx={{mt: 2}}
                        onClick={() => navigate("/login")}
                    >
                        Login
                    </Button>
                </Box>
            </Paper>
        </Container>
    );
};

export default RegisterPage;