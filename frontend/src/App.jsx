import React from 'react';
import {BrowserRouter, Routes, Route} from "react-router";
import Layout from "./common/ui/components/layout/Layout/Layout.jsx";
import HomePage from "./common/ui/pages/HomePage/HomePage.jsx";
import UsersPage from "./auth_and_access/ui/pages/UserPage/UsersPage.jsx";
import UserDetailsPage from "./auth_and_access/ui/pages/UserDetailsPage/UserDetailsPage.jsx";
import UserEditPage from "./auth_and_access/ui/pages/UserEditPage/UserEditPage.jsx";
import LoginPage from "./auth_and_access/ui/pages/LoginPage/LoginPage.jsx";
import RegisterPage from "./auth_and_access/ui/pages/RegistrationPage/RegistrationPage.jsx";
import ForgotPasswordPage from "./auth_and_access/ui/pages/ForgotPasswordPage/ForgotPassword.jsx";
import ResetPasswordPage from "./auth_and_access/ui/pages/ResetPasswordPage/ResetPasswordPage.jsx";


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
                    <Route path='users' element={<UsersPage/>}/>
                    <Route path='users/:id' element={<UserDetailsPage/>}/>
                    <Route path='users/:id/edit' element={<UserEditPage/>}/>
                </Route>
            </Routes>
        </BrowserRouter>

    )
}

export default App
