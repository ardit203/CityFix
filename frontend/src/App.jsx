import React from 'react';
import {BrowserRouter, Routes, Route} from "react-router";
import Layout from "./common/ui/components/layout/Layout/Layout.jsx";
import HomePage from "./common/ui/pages/HomePage/HomePage.jsx";

// Modular Routes Imports
import { departmentRoutes } from "./administration/routes/departmentRoutes.jsx";
import { municipalityRoutes } from "./administration/routes/municipalityRoutes.jsx";
import { categoryRoutes } from "./administration/routes/categoryRoutes.jsx";
import RegisterPage from "./auth_and_access/ui/pages/RegistrationPage.jsx";
import LoginPage from "./auth_and_access/ui/pages/LoginPage.jsx";
import ForgotPasswordPage from "./auth_and_access/ui/pages/ForgotPasswordPage.jsx";
import ResetPasswordPage from "./auth_and_access/ui/pages/ResetPasswordPage.jsx";
import UnauthorizedPage from "./auth_and_access/ui/pages/UnauthorizedPage.jsx";
import {userRoutes} from "./auth_and_access/routes/userRoutes.jsx";
import {profileRoutes} from "./auth_and_access/routes/profileRoutes.jsx";
import {staffRoutes} from "./administration/routes/staffRoutes.jsx";
import {requestRoutes} from "./request_management/routes/requestRoutes.jsx";
import {reportRoutes} from "./reporting/routes/reportRoutes.jsx";


function App() {
    return (
        <BrowserRouter>
            <Routes>
                <Route path="/register" element={<RegisterPage/>}/>
                <Route path="/login" element={<LoginPage/>}/>
                <Route path="/forgot-password" element={<ForgotPasswordPage/>}/>
                <Route path="/reset-password" element={<ResetPasswordPage/>}/>
                <Route path="/unauthorized" element={<UnauthorizedPage/>}/>
                <Route path='/' element={<Layout/>}>
                    <Route index element={<HomePage/>}/>
                    {departmentRoutes}
                    {municipalityRoutes}
                    {categoryRoutes}
                    {userRoutes}
                    {profileRoutes}
                    {staffRoutes}
                    {requestRoutes}
                    {reportRoutes}
                </Route>
            </Routes>
        </BrowserRouter>
    );
}

export default App;
