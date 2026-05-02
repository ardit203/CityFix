import { Box, Button, MenuItem, Stack, TextField } from "@mui/material";
import { useState } from "react";

const emptyUser = {
    username: "",
    email: "",
    role: "",
    notificationsEnabled: true,
    profile: {
        name: "",
        surname: "",
        phoneNumber: "",
        dateOfBirth: "",
        gender: "",
        address: {
            street: "",
            city: "",
            postalCode: ""
        }
    }
};

const UserAdminForm = ({ initialValues = emptyUser, onSubmit, submitLabel = "Save" }) => {
    const [formData, setFormData] = useState(initialValues);

    const handleChange = (event) => {
        const { name, value } = event.target;

        setFormData((prev) => ({
            ...prev,
            [name]: value
        }));
    };

    const handleProfileChange = (event) => {
        const { name, value } = event.target;

        setFormData((prev) => ({
            ...prev,
            profile: {
                ...prev.profile,
                [name]: value
            }
        }));
    };

    const handleAddressChange = (event) => {
        const { name, value } = event.target;

        setFormData((prev) => ({
            ...prev,
            profile: {
                ...prev.profile,
                address: {
                    ...prev.profile.address,
                    [name]: value
                }
            }
        }));
    };

    const handleSubmit = (event) => {
        event.preventDefault();
        onSubmit(formData);
    };

    return (
        <Box component="form" onSubmit={handleSubmit}>
            <Stack spacing={2}>
                <TextField
                    label="Username"
                    name="username"
                    value={formData.username}
                    onChange={handleChange}
                    fullWidth
                />

                <TextField
                    label="Email"
                    name="email"
                    value={formData.email}
                    onChange={handleChange}
                    fullWidth
                />

                <TextField
                    select
                    label="Role"
                    name="role"
                    value={formData.role}
                    onChange={handleChange}
                    fullWidth
                >
                    <MenuItem value="ROLE_CITIZEN">Citizen</MenuItem>
                    <MenuItem value="ROLE_EMPLOYEE">Employee</MenuItem>
                    <MenuItem value="ROLE_MANAGER">Manager</MenuItem>
                    <MenuItem value="ROLE_ADMINISTRATOR">Administrator</MenuItem>
                </TextField>

                <TextField
                    label="Name"
                    name="name"
                    value={formData.profile.name}
                    onChange={handleProfileChange}
                    fullWidth
                />

                <TextField
                    label="Surname"
                    name="surname"
                    value={formData.profile.surname}
                    onChange={handleProfileChange}
                    fullWidth
                />

                <TextField
                    label="Phone number"
                    name="phoneNumber"
                    value={formData.profile.phoneNumber}
                    onChange={handleProfileChange}
                    fullWidth
                />

                <TextField
                    label="Date of birth"
                    name="dateOfBirth"
                    type="date"
                    value={formData.profile.dateOfBirth}
                    onChange={handleProfileChange}
                    InputLabelProps={{ shrink: true }}
                    fullWidth
                />

                <TextField
                    select
                    label="Gender"
                    name="gender"
                    value={formData.profile.gender}
                    onChange={handleProfileChange}
                    fullWidth
                >
                    <MenuItem value="MALE">Male</MenuItem>
                    <MenuItem value="FEMALE">Female</MenuItem>
                    <MenuItem value="OTHER">Other</MenuItem>
                </TextField>

                <TextField
                    label="Street"
                    name="street"
                    value={formData.profile.address.street}
                    onChange={handleAddressChange}
                    fullWidth
                />

                <TextField
                    label="City"
                    name="city"
                    value={formData.profile.address.city}
                    onChange={handleAddressChange}
                    fullWidth
                />

                <TextField
                    label="Postal code"
                    name="postalCode"
                    value={formData.profile.address.postalCode}
                    onChange={handleAddressChange}
                    fullWidth
                />

                <Button type="submit" variant="contained">
                    {submitLabel}
                </Button>
            </Stack>
        </Box>
    );
};

export default UserAdminForm;