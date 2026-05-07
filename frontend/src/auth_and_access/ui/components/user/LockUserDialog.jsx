import {
    Button,
    Dialog,
    DialogActions,
    DialogContent,
    DialogTitle,
    TextField,
    Typography
} from "@mui/material";
import { useState } from "react";

const LockUserDialog = ({ open, onClose, onSubmit}) => {
    const [lockedUntil, setLockedUntil] = useState("");

    const handleSubmit = async () => {
        // executeAction handles the promise, and returns true on success
        const success = await onSubmit(lockedUntil);

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
                <Typography variant="body2" color="text.secondary" sx={{ mt: 1, mb: 2 }}>
                    Specify the date and time until which the user will be locked out of their account.
                </Typography>
                <TextField
                    label="Locked until"
                    type="datetime-local"
                    value={lockedUntil}
                    onChange={(e) => setLockedUntil(e.target.value)}
                    fullWidth
                    InputLabelProps={{
                        shrink: true
                    }}
                />
            </DialogContent>

            <DialogActions sx={{ px: 3, pb: 2 }}>
                <Button onClick={handleClose}>
                    Cancel
                </Button>

                <Button
                    variant="contained"
                    color="warning"
                    onClick={handleSubmit}
                    disabled={!lockedUntil}
                >
                    Lock Account
                </Button>
            </DialogActions>
        </Dialog>
    );
};

export default LockUserDialog;
