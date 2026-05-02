import { useEffect, useState } from "react";
import {
    Button,
    MenuItem,
    Stack,
    TextField
} from "@mui/material";
import useSnackbar from "../../../../../common/hooks/useSnackbar.js";
import useConfirmDialog from "../../../../../common/hooks/useConfirmDialog.js";
// import userApi from "../../../../service/userApi.js";

const ProfileSettingsForm = ({ user, onSuccess }) => {
    const [formData, setFormData] = useState({
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
    });

    const { confirm } = useConfirmDialog();
    const { showSnackbar } = useSnackbar();

    useEffect(() => {
        const profile = user?.profile;
        const address = profile?.address;

        setFormData({
            name: profile?.name || "",
            surname: profile?.surname || "",
            phoneNumber: profile?.phoneNumber || "",
            dateOfBirth: profile?.dateOfBirth || "",
            gender: profile?.gender || "",
            address: {
                street: address?.street || "",
                city: address?.city || "",
                postalCode: address?.postalCode || ""
            }
        });
    }, [user]);

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

    const handleSubmit = async (event) => {
        event.preventDefault();

        const confirmed = await confirm({
            title: "Update profile?",
            message: "Are you sure you want to update your profile information?",
            confirmText: "Update",
            cancelText: "Cancel"
        });

        if (!confirmed) {
            return;
        }

        try {
            console.log("Update profile", formData);

            // await userApi.updateMyProfile(formData);

            showSnackbar("Profile updated successfully", "success");

            if (onSuccess) {
                onSuccess();
            }
        } catch (error) {
            const message =
                error.response?.data?.message ||
                error.response?.data?.error ||
                error.message ||
                "Failed to update profile";

            showSnackbar(message, "error");
        }
    };

    return (
        <Stack component="form" spacing={2} onSubmit={handleSubmit}>
            <TextField
                label="Name"
                name="name"
                value={formData.name}
                onChange={handleChange}
                fullWidth
            />

            <TextField
                label="Surname"
                name="surname"
                value={formData.surname}
                onChange={handleChange}
                fullWidth
            />

            <TextField
                label="Phone number"
                name="phoneNumber"
                value={formData.phoneNumber}
                onChange={handleChange}
                fullWidth
            />

            <TextField
                label="Date of birth"
                name="dateOfBirth"
                type="date"
                value={formData.dateOfBirth}
                onChange={handleChange}
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
                value={formData.gender}
                onChange={handleChange}
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
                value={formData.address.street}
                onChange={handleAddressChange}
                fullWidth
            />

            <TextField
                label="City"
                name="city"
                value={formData.address.city}
                onChange={handleAddressChange}
                fullWidth
            />

            <TextField
                label="Postal code"
                name="postalCode"
                value={formData.address.postalCode}
                onChange={handleAddressChange}
                fullWidth
            />

            <Button type="submit" variant="contained">
                Save Profile
            </Button>
        </Stack>
    );
};

export default ProfileSettingsForm;