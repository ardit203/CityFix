import { useState } from "react";
import {
    Button,
    Stack,
    TextField
} from "@mui/material";
import useSnackbar from "../../../../../common/hooks/useSnackbar.js";
import useConfirmDialog from "../../../../../common/hooks/useConfirmDialog.js";
import userApi from "../../../../service/userApi.js";
// import userApi from "../../../../service/userApi.js";

const initialFormData = {
    currentPassword: "",
    newPassword: "",
    confirmNewPassword: ""
};

const ChangePasswordForm = () => {
    const [formData, setFormData] = useState(initialFormData);

    const { confirm } = useConfirmDialog();
    const { showSnackbar } = useSnackbar();

    const handleChange = (event) => {
        const { name, value } = event.target;

        setFormData((prev) => ({
            ...prev,
            [name]: value
        }));
    };

    const handleSubmit = async (event) => {
        event.preventDefault();

        if (formData.newPassword !== formData.confirmNewPassword) {
            showSnackbar("New passwords do not match", "error");
            return;
        }

        const confirmed = await confirm({
            title: "Change password?",
            message: "Are you sure you want to change your password?",
            confirmText: "Change Password",
            cancelText: "Cancel"
        });

        if (!confirmed) {
            return;
        }

        try {
            console.log("Change password", formData);

            await userApi.changeMyPassword(formData);

            showSnackbar("Password changed successfully", "success");
            setFormData(initialFormData);
        } catch (error) {
            const message =
                error.response?.data?.message ||
                error.response?.data?.error ||
                error.message ||
                "Failed to change password";

            showSnackbar(message, "error");
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