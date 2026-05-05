import {
    Button,
    Dialog,
    DialogActions,
    DialogContent,
    DialogTitle,
    TextField
} from "@mui/material";
import { useState } from "react";

const LockUserDialog = ({ open, onClose, onSubmit}) => {
    const [lockedUntil, setLockedUntil] = useState("");

    const handleSubmit = async () => {
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