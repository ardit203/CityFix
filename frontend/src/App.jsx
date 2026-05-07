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
import {userRoutes} from "./auth_and_access/routes/userRoutes.jsx";
import {profileRoutes} from "./auth_and_access/routes/profileRoutes.jsx";
import {staffRoutes} from "./administration/routes/staffRoutes.jsx";


function App() {
    return (
        <BrowserRouter>
            <Routes>
                <Route path="/register" element={<RegisterPage/>}/>
                <Route path="/login" element={<LoginPage/>}/>
                <Route path="/forgot-password" element={<ForgotPasswordPage/>}/>
                <Route path="/reset-password" element={<ResetPasswordPage/>}/>
                <Route path='/' element={<Layout/>}>
                    <Route index element={<HomePage/>}/>
                    
                    {/*<Route path='users' element={<UsersPage/>}/>*/}
                    {/*<Route path='users/:id' element={<UserDetailsPage/>}/>*/}
                    {/*<Route path='users/:id/edit' element={<UserEditPage/>}/>*/}
                    {/*<Route path="/profile" element={<UserProfilePage />} />*/}
                    {/*<Route path="/profile/edit" element={<ProfileSettingsPage />} />*/}
                    
                    {/* --- Modular Admin Routes --- */}
                    {departmentRoutes}
                    {municipalityRoutes}
                    {categoryRoutes}
                    {userRoutes}
                    {profileRoutes}
                    {staffRoutes}
                </Route>
            </Routes>
        </BrowserRouter>
    );
}

export default App;
