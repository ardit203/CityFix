import React from "react";
import { Navigate } from "react-router";
import useAuth from "../../../hooks/auth/useAuth.js";

// Helper to handle Spring Boot's authority serialization
const hasRequiredRole = (userRoles, allowedRoles) => {
    if (!userRoles || !Array.isArray(userRoles)) return false;
    
    return userRoles.some(roleObj => {
        // Handle both ["ADMIN"] and [{ authority: "ROLE_ADMIN" }] structures
        const roleName = typeof roleObj === 'string' ? roleObj : roleObj.authority;
        // Strip "ROLE_" prefix if backend sends it, to make frontend checks cleaner
        const cleanRole = roleName?.replace('ROLE_', '');
        
        return allowedRoles.includes(cleanRole) || allowedRoles.includes(roleName);
    });
};

const ProtectedRoute = ({ allowedRoles, children }) => {
    const { user, isLoggedIn, loading } = useAuth();

    if (loading) {
        return null; 
    }

    if (!isLoggedIn) {
        return <Navigate to="/login" replace />;
    }

    if (allowedRoles && !hasRequiredRole(user?.roles, allowedRoles)) {
        return <Navigate to="/unauthorized" replace />;
    }

    return children;
};

export default ProtectedRoute;
