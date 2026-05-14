import { StrictMode } from 'react'
import { createRoot } from 'react-dom/client'
import { CssBaseline, ThemeProvider } from "@mui/material";
import './index.css'
import App from './App.jsx'
import AuthProvider from "./auth_and_access/providers/authProvider.jsx";
import SnackbarProvider from "./common/providers/snackbarProvider.jsx";
import ConfirmDialogProvider from "./common/providers/confirmDialogProvider.jsx";
import theme from "./common/theme/theme.js";

createRoot(document.getElementById('root')).render(
    // <StrictMode>
    <ThemeProvider theme={theme}>
        <CssBaseline />
        <AuthProvider>
            <SnackbarProvider>
                <ConfirmDialogProvider>
                    <App />
                </ConfirmDialogProvider>
            </SnackbarProvider>
        </AuthProvider>
    </ThemeProvider>
    // </StrictMode>,
)
