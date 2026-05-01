import {StrictMode} from 'react'
import {createRoot} from 'react-dom/client'
import './index.css'
import App from './App.jsx'
import AuthProvider from "./auth_and_access/providers/authProvider.jsx";
import SnackbarProvider from "./common/providers/snackbarProvider.jsx";

createRoot(document.getElementById('root')).render(
    // <StrictMode>
    <AuthProvider>
        <SnackbarProvider>
            <App/>
        </SnackbarProvider>
    </AuthProvider>
    // </StrictMode>,
)
