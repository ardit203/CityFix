import { StrictMode } from 'react'
import { createRoot } from 'react-dom/client'
import './index.css'
import App from './App.jsx'
import AuthProvider from "./auth_and_access/providers/authProvider.jsx";
import SnackbarProvider from "./common/providers/snackbarProvider.jsx";
import ConfirmDialogProvider from "./common/providers/confirmDialogProvider.jsx";

createRoot(document.getElementById('root')).render(
    // <StrictMode>
    <AuthProvider>
        <SnackbarProvider>
            <ConfirmDialogProvider>
                <App />
            </ConfirmDialogProvider>
        </SnackbarProvider>
    </AuthProvider>
    // </StrictMode>,
)
