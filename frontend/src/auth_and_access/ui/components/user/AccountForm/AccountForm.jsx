import { useEffect, useState } from "react";
import {
    Button,
    FormControlLabel,
    Stack,
    Switch,
    TextField
} from "@mui/material";
import useProfileActions from "../../../../hooks/useProfileActions.js";

const AccountSettingsForm = ({ user, onSuccess }) => {
    const [formData, setFormData] = useState({
        username: "",
        email: "",
        notificationsEnabled: true
    });

    const { updateMyAccount } = useProfileActions();

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

        await updateMyAccount(formData, onSuccess);
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