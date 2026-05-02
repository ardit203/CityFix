import { Alert, Snackbar } from "@mui/material";
import { useCallback, useMemo, useState } from "react";
import SnackbarContext from "../contexts/snackbarContext.js";

const SnackbarProvider = ({ children }) => {
    const [snackbar, setSnackbar] = useState({
        open: false,
        message: "",
        severity: "error"
    });

    const showSnackbar = useCallback((message, severity = "error") => {
        setSnackbar({
            open: true,
            message,
            severity
        });
    }, []);

    const hideSnackbar = useCallback(() => {
        setSnackbar((prev) => ({
            ...prev,
            open: false
        }));
    }, []);

    const value = useMemo(() => ({
        showSnackbar
    }), [showSnackbar]);

    return (
        <SnackbarContext.Provider value={value}>
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
                    sx={{
                        width: "100%",
                        whiteSpace: "pre-line"
                    }}
                >
                    {snackbar.message}
                </Alert>
            </Snackbar>
        </SnackbarContext.Provider>
    );
};

export default SnackbarProvider;