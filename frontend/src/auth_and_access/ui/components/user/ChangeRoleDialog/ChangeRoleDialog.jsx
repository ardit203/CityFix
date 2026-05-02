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
import useConfirmDialog from "../../../../../common/hooks/useConfirmDialog.js";
import useSnackbar from "../../../../../common/hooks/useSnackbar.js";

// import userApi from "../../../service/userApi.js";

const ChangeUserRoleDialog = ({ open, onClose, user, onSuccess }) => {
    const [selectedRole, setSelectedRole] = useState("");

    const { confirm } = useConfirmDialog();
    const { showSnackbar } = useSnackbar();

    useEffect(() => {
        if (user) {
            setSelectedRole(user.role || "");
        }
    }, [user]);

    const handleSubmit = async () => {
        const confirmed = await confirm({
            title: "Change user role?",
            message: `Are you sure you want to change the role of ${user.username}?`,
            confirmText: "Change Role",
            cancelText: "Cancel"
        });

        if (!confirmed) {
            return;
        }

        try {
            console.log("Change role", user.id, selectedRole);

            // later:
            // await userApi.changeRole(user.id, selectedRole);

            showSnackbar("User role changed successfully", "success");

            if (onSuccess) {
                onSuccess();
            }

            onClose();
        } catch (error) {
            const message =
                error.response?.data?.message ||
                error.response?.data?.error ||
                error.message ||
                "Failed to change user role";

            showSnackbar(message, "error");
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