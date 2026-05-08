import {
    Button,
    Dialog,
    DialogActions,
    DialogContent,
    DialogTitle,
    MenuItem,
    TextField,
    Typography
} from "@mui/material";
import { useEffect, useState } from "react";

const ChangeUserRoleDialog = ({ open, onClose, user, onSubmit}) => {
    const [selectedRole, setSelectedRole] = useState("");

    useEffect(() => {
        if (user) {
            let initialRole = user.role || "";
            if (initialRole && !initialRole.startsWith("ROLE_")) {
                initialRole = "ROLE_" + initialRole;
            }
            setSelectedRole(initialRole);
        }
    }, [user]);

    const handleSubmit = async () => {
        // executeAction handles the promise, and returns true on success
        const success = await onSubmit(selectedRole);
        if (success) {
            onClose();
        }
    };

    return (
        <Dialog open={open} onClose={onClose} fullWidth maxWidth="xs">
            <DialogTitle>Change User Role</DialogTitle>

            <DialogContent>
                <Typography variant="body2" color="text.secondary" sx={{ mt: 1, mb: 2 }}>
                    Changing a user's role will immediately impact their access and permissions across the system.
                </Typography>
                <TextField
                    select
                    label="Role"
                    value={selectedRole}
                    onChange={(e) => setSelectedRole(e.target.value)}
                    fullWidth
                >
                    <MenuItem value="ROLE_CITIZEN">Citizen</MenuItem>
                    <MenuItem value="ROLE_EMPLOYEE">Employee</MenuItem>
                    <MenuItem value="ROLE_MANAGER">Manager</MenuItem>
                    <MenuItem value="ROLE_ADMINISTRATOR">Administrator</MenuItem>
                </TextField>
            </DialogContent>

            <DialogActions sx={{ px: 3, pb: 2 }}>
                <Button onClick={onClose}>
                    Cancel
                </Button>

                <Button
                    variant="contained"
                    onClick={handleSubmit}
                    disabled={!selectedRole || selectedRole.replace("ROLE_", "") === user?.role}
                >
                    Change Role
                </Button>
            </DialogActions>
        </Dialog>
    );
};

export default ChangeUserRoleDialog;
