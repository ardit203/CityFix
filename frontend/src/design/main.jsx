import {StrictMode} from 'react'
import {createRoot} from 'react-dom/client'
import './index.css'
import App from '../../App.jsx'
import AuthProvider from "../../auth_and_access/providers/authProvider.jsx";
import SnackbarProvider from "../../common/providers/snackbarProvider.jsx";

import { ThemeProvider } from '@mui/material/styles';
import CssBaseline from '@mui/material/CssBaseline';
import theme from './common/theme/theme.js';

createRoot(document.getElementById('root')).render(
    <ThemeProvider theme={theme}>
        <CssBaseline />
        <AuthProvider>
            <SnackbarProvider>
                <App/>
            </SnackbarProvider>
        </AuthProvider>
    </ThemeProvider>
)
