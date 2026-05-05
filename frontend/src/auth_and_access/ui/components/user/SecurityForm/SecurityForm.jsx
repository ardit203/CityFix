import { useState } from "react";
import {
    Button,
    Stack,
    TextField
} from "@mui/material";
import useProfileActions from "../../../../hooks/useProfileActions.js";

const initialFormData = {
    currentPassword: "",
    newPassword: "",
    confirmNewPassword: ""
};

const ChangePasswordForm = () => {
    const [formData, setFormData] = useState(initialFormData);

    const { changeMyPassword } = useProfileActions();

    const handleChange = (event) => {
        const { name, value } = event.target;

        setFormData((prev) => ({
            ...prev,
            [name]: value
        }));
    };

    const handleSubmit = async (event) => {
        event.preventDefault();

        const success = await changeMyPassword(formData);

        if (success) {
            setFormData(initialFormData);
        }
    };

    return (
        <Stack component="form" spacing={2} onSubmit={handleSubmit}>
            <TextField
                label="Current password"
                name="currentPassword"
                type="password"
                value={formData.currentPassword}
                onChange={handleChange}
                fullWidth
            />

            <TextField
                label="New password"
                name="newPassword"
                type="password"
                value={formData.newPassword}
                onChange={handleChange}
                fullWidth
            />

            <TextField
                label="Confirm new password"
                name="confirmNewPassword"
                type="password"
                value={formData.confirmNewPassword}
                onChange={handleChange}
                fullWidth
            />

            <Button type="submit" variant="contained">
                Change Password
            </Button>
        </Stack>
    );
};

export default ChangePasswordForm;