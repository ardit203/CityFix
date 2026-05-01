import { Alert, Snackbar } from "@mui/material";
import { useState } from "react";
import SnackbarContext from "../contexts/snackbarContext.js";

const SnackbarProvider = ({ children }) => {
    const [snackbar, setSnackbar] = useState({
        open: false,
        message: "",
        severity: "error"
    });

    const showSnackbar = (message, severity = "error") => {
        setSnackbar({
            open: true,
            message,
            severity
        });
    };

    const hideSnackbar = () => {
        setSnackbar((prev) => ({
            ...prev,
            open: false
        }));
    };

    return (
        <SnackbarContext.Provider value={{ showSnackbar }}>
            {children}

            <Snackbar
                open={snackbar.open}
                autoHideDuration={4000}
                onClose={hideSnackbar}
                anchorOrigin={{
                    vertical: "bottom",
                    horizontal: "right"
                }}
            >
                <Alert
                    onClose={hideSnackbar}
                    severity={snackbar.severity}
                    variant="filled"
                    sx={{ width: "100%" }}
                >
                    {snackbar.message}
                </Alert>
            </Snackbar>
        </SnackbarContext.Provider>
    );
};

export default SnackbarProvider;