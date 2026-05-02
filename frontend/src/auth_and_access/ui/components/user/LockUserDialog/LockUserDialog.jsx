import {
    Button,
    Dialog,
    DialogActions,
    DialogContent,
    DialogTitle,
    TextField
} from "@mui/material";
import { useState } from "react";
import useUserActions from "../../../../hooks/useUserActions.js";

const LockUserDialog = ({ open, onClose, user, onSuccess }) => {
    const [lockedUntil, setLockedUntil] = useState("");
    const { lockUser } = useUserActions();

    const handleSubmit = async () => {
        const success = await lockUser(user, lockedUntil, onSuccess);

        if (success) {
            setLockedUntil("");
            onClose();
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