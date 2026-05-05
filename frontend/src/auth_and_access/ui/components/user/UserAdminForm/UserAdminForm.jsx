import {Box, Button, Stack, TextField, MenuItem} from "@mui/material";
import {useEffect, useState} from "react";
import useUserDetails from "../../../../hooks/useUserDetails.js";
import useUserActions from "../../../../hooks/useUserActions.js";

const emptyUser = {
    username: "",
    email: "",
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

const UserAdminForm = ({initialValues = emptyUser, submitLabel = "Save", onSubmit}) => {
    const [formData, setFormData] = useState(emptyUser);

    useEffect(() => {
        setFormData({
            username: initialValues?.username || "",
            email: initialValues?.email || "",
            notificationsEnabled: initialValues?.notificationsEnabled ?? true,
            profile: {
                name: initialValues?.profile?.name || "",
                surname: initialValues?.profile?.surname || "",
                phoneNumber: initialValues?.profile?.phoneNumber || "",
                dateOfBirth: initialValues?.profile?.dateOfBirth || "",
                gender: initialValues?.profile?.gender || "",
                address: {
                    street: initialValues?.profile?.address?.street || "",
                    city: initialValues?.profile?.address?.city || "",
                    postalCode: initialValues?.profile?.address?.postalCode || ""
                }
            }
        });
    }, [initialValues]);

    const handleChange = (event) => {
        const {name, value} = event.target;

        setFormData((prev) => ({
            ...prev,
            [name]: value
        }));
    };

    const handleProfileChange = (event) => {
        const {name, value} = event.target;

        setFormData((prev) => ({
            ...prev,
            profile: {
                ...prev.profile,
                [name]: value
            }
        }));
    };

    const handleAddressChange = (event) => {
        const {name, value} = event.target;

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

    const handleSubmit = async (event) => {
        event.preventDefault();

        const requestBody = {
            username: formData.username,
            email: formData.email,
            notificationsEnabled: formData.notificationsEnabled,
            name: formData.profile.name,
            surname: formData.profile.surname,
            address: {
                street: formData.profile.address.street,
                city: formData.profile.address.city,
                postalCode: formData.profile.address.postalCode
            },
            dateOfBirth: formData.profile.dateOfBirth || null,
            gender: formData.profile.gender || null,
            phoneNumber: formData.profile.phoneNumber || null
        };

        await onSubmit(requestBody);
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
                    slotProps={{
                        inputLabel: {
                            shrink: true
                        }
                    }}
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
                    <MenuItem value="">Not selected</MenuItem>
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