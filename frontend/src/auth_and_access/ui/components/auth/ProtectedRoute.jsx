import React from "react";
import { Navigate, Outlet } from "react-router";
import useAuth from "../../../hooks/auth/useAuth.js";

const hasRequiredRole = (userRoles, allowedRoles) => {
    if (!allowedRoles || allowedRoles.length === 0) {
        return true;
    }

    if (!userRoles || !Array.isArray(userRoles)) {
        return false;
    }

    return userRoles.some(roleObj => {
        const roleName = typeof roleObj === "string" ? roleObj : roleObj.authority;
        const cleanRole = roleName?.replace("ROLE_", "");

        return allowedRoles.includes(cleanRole) || allowedRoles.includes(roleName);
    });
};

const ProtectedRoute = ({ allowedRoles = [] }) => {
    const { user, isLoggedIn, loading } = useAuth();

    if (loading) {
        return null;
    }

    if (!isLoggedIn) {
        return <Navigate to="/login" replace />;
    }

    if (!hasRequiredRole(user?.roles, allowedRoles)) {
        return <Navigate to="/unauthorized" replace />;
    }

    return <Outlet />;
};

export default ProtectedRoute;