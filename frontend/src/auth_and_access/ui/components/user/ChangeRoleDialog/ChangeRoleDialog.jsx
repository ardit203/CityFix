import {
    Button,
    Dialog,
    DialogActions,
    DialogContent,
    DialogTitle,
    MenuItem,
    TextField
} from "@mui/material";
import { useEffect, useState } from "react";

const ChangeUserRoleDialog = ({ open, onClose, user, onSubmit}) => {
    const [selectedRole, setSelectedRole] = useState("");

    useEffect(() => {
        if (user) {
            setSelectedRole(user.role || "");
        }
    }, [user]);

    const handleSubmit = async () => {
        const success = await onSubmit(selectedRole);
        if (success) {
            onClose();
        }
    };

    return (
        <Dialog open={open} onClose={onClose} fullWidth maxWidth="xs">
            <DialogTitle>Change User Role</DialogTitle>

            <DialogContent>
                <TextField
                    select
                    label="Role"
                    value={selectedRole}
                    onChange={(e) => setSelectedRole(e.target.value)}
                    fullWidth
                    sx={{ mt: 2 }}
                >
                    <MenuItem value="ROLE_CITIZEN">Citizen</MenuItem>
                    <MenuItem value="ROLE_EMPLOYEE">Employee</MenuItem>
                    <MenuItem value="ROLE_MANAGER">Manager</MenuItem>
                    <MenuItem value="ROLE_ADMINISTRATOR">Administrator</MenuItem>
                </TextField>
            </DialogContent>

            <DialogActions>
                <Button onClick={onClose}>
                    Cancel
                </Button>

                <Button
                    variant="contained"
                    onClick={handleSubmit}
                    disabled={!selectedRole || selectedRole === user?.role}
                >
                    Change Role
                </Button>
            </DialogActions>
        </Dialog>
    );
};

export default ChangeUserRoleDialog;