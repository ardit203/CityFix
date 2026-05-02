import {useState} from "react";
import {
    Button,
    Dialog,
    DialogActions,
    DialogContent,
    DialogContentText,
    DialogTitle
} from "@mui/material";
import ConfirmDialogContext from "../contexts/confirmDialogContext.js";

const ConfirmDialogProvider = ({children}) => {
    const [dialog, setDialog] = useState({
        open: false,
        title: "",
        message: "",
        confirmText: "Confirm",
        cancelText: "Cancel",
        resolve: null
    });

    const confirm = ({
                         title = "Are you sure?",
                         message = "This action cannot be undone.",
                         confirmText = "Confirm",
                         cancelText = "Cancel"
                     }) => {
        return new Promise((resolve) => {
            setDialog({
                open: true,
                title,
                message,
                confirmText,
                cancelText,
                resolve
            });
        });
    };

    const handleCancel = () => {
        dialog.resolve(false);

        setDialog((prev) => ({
            ...prev,
            open: false
        }));
    };

    const handleConfirm = () => {
        dialog.resolve(true);

        setDialog((prev) => ({
            ...prev,
            open: false
        }));
    };

    return (
        <ConfirmDialogContext.Provider value={{confirm}}>
            {children}

            <Dialog
                open={dialog.open}
                onClose={handleCancel}
            >
                <DialogTitle>
                    {dialog.title}
                </DialogTitle>

                <DialogContent>
                    <DialogContentText>
                        {dialog.message}
                    </DialogContentText>
                </DialogContent>

                <DialogActions>
                    <Button onClick={handleCancel}>
                        {dialog.cancelText}
                    </Button>

                    <Button
                        onClick={handleConfirm}
                        color="error"
                        variant="contained"
                    >
                        {dialog.confirmText}
                    </Button>
                </DialogActions>
            </Dialog>
        </ConfirmDialogContext.Provider>
    );
};

export default ConfirmDialogProvider;