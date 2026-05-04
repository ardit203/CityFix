import { useEffect, useState } from "react";
import {
    Button,
    FormControlLabel,
    Stack,
    Switch,
    TextField
} from "@mui/material";
import useSnackbar from "../../../../../common/hooks/useSnackbar.js";
import useConfirmDialog from "../../../../../common/hooks/useConfirmDialog.js";
import userApi from "../../../../service/userApi.js";
// import userApi from "../../../../service/userApi.js";

const AccountSettingsForm = ({ user, onSuccess }) => {
    const [formData, setFormData] = useState({
        username: "",
        email: "",
        notificationsEnabled: true
    });

    const { confirm } = useConfirmDialog();
    const { showSnackbar } = useSnackbar();

    useEffect(() => {
        if (user) {
            setFormData({
                username: user.username || "",
                email: user.email || "",
                notificationsEnabled: user.notificationsEnabled ?? true
            });
        }
    }, [user]);

    const handleChange = (event) => {
        const { name, value } = event.target;

        setFormData((prev) => ({
            ...prev,
            [name]: value
        }));
    };

    const handleSwitchChange = (event) => {
        setFormData((prev) => ({
            ...prev,
            notificationsEnabled: event.target.checked
        }));
    };

    const handleSubmit = async (event) => {
        event.preventDefault();

        const confirmed = await confirm({
            title: "Update account?",
            message: "Are you sure you want to update your account information?",
            confirmText: "Update",
            cancelText: "Cancel"
        });

        if (!confirmed) {
            return;
        }

        try {
            console.log("Update account", formData);

            await userApi.updateMyAccount(formData);

            showSnackbar("Account updated successfully", "success");

            if (onSuccess) {
                onSuccess();
            }
        } catch (error) {
            const message =
                error.response?.data?.message ||
                error.response?.data?.error ||
                error.message ||
                "Failed to update account";

            showSnackbar(message, "error");
        }
    };

    return (
        <Stack component="form" spacing={2} onSubmit={handleSubmit}>
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
                type="email"
                value={formData.email}
                onChange={handleChange}
                fullWidth
            />

            <FormControlLabel
                control={
                    <Switch
                        checked={formData.notificationsEnabled}
                        onChange={handleSwitchChange}
                    />
                }
                label="Notifications enabled"
            />

            <Button type="submit" variant="contained">
                Save Account
            </Button>
        </Stack>
    );
};

export default AccountSettingsForm;