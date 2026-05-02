import {
    Button,
    Dialog,
    DialogActions,
    DialogContent,
    DialogTitle,
    TextField
} from "@mui/material";
import { useState } from "react";
import useConfirmDialog from "../../../../../common/hooks/useConfirmDialog.js";
import useSnackbar from "../../../../../common/hooks/useSnackbar.js";


// import userApi from "../../../service/userApi.js";

const LockUserDialog = ({ open, onClose, user, onSuccess }) => {
    const [lockedUntil, setLockedUntil] = useState("");

    const { confirm } = useConfirmDialog();
    const { showSnackbar } = useSnackbar();

    const handleSubmit = async () => {
        const confirmed = await confirm({
            title: "Lock user account?",
            message: `Are you sure you want to lock ${user.username} until ${lockedUntil}?`,
            confirmText: "Lock",
            cancelText: "Cancel"
        });

        if (!confirmed) {
            return;
        }

        try {
            const requestBody = {
                lockedUntil: `${lockedUntil}:00`
            };

            console.log("Lock user", user.id, requestBody);

            // later:
            // await userApi.lock(user.id, requestBody);

            showSnackbar("User locked successfully", "success");

            if (onSuccess) {
                onSuccess();
            }

            setLockedUntil("");
            onClose();
        } catch (error) {
            const message =
                error.response?.data?.message ||
                error.response?.data?.error ||
                error.message ||
                "Failed to lock user";

            showSnackbar(message, "error");
        }
    };

    const handleClose = () => {
        setLockedUntil("");
        onClose();
    };

    return (
        <Dialog open={open} onClose={handleClose} fullWidth maxWidth="xs">
            <DialogTitle>Lock User Account</DialogTitle>

            <DialogContent>
                <TextField
                    label="Locked until"
                    type="datetime-local"
                    value={lockedUntil}
                    onChange={(e) => setLockedUntil(e.target.value)}
                    fullWidth
                    sx={{ mt: 2 }}
                    slotProps={{
                        inputLabel: {
                            shrink: true
                        }
                    }}
                />
            </DialogContent>

            <DialogActions>
                <Button onClick={handleClose}>
                    Cancel
                </Button>

                <Button
                    variant="contained"
                    color="warning"
                    onClick={handleSubmit}
                    disabled={!lockedUntil}
                >
                    Lock
                </Button>
            </DialogActions>
        </Dialog>
    );
};

export default LockUserDialog;