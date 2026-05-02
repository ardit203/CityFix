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
import useUserActions from "../../../../hooks/useUserActions.js";

const ChangeUserRoleDialog = ({ open, onClose, user, onSuccess }) => {
    const [selectedRole, setSelectedRole] = useState("");
    const { changeRole } = useUserActions();

    useEffect(() => {
        if (user) {
            setSelectedRole(user.role || "");
        }
    }, [user]);

    const handleSubmit = async () => {
        const success = await changeRole(user, selectedRole, onSuccess);

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