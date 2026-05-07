import useAuth from "../../../hooks/auth/useAuth.js";

// Helper to handle Spring Boot's authority serialization
const hasRequiredRole = (userRoles, allowedRoles) => {
    if (!userRoles || !Array.isArray(userRoles)) return false;
    
    return userRoles.some(roleObj => {
        const roleName = typeof roleObj === 'string' ? roleObj : roleObj.authority;
        const cleanRole = roleName?.replace('ROLE_', '');
        return allowedRoles.includes(cleanRole) || allowedRoles.includes(roleName);
    });
};

const RequireRole = ({ allowedRoles, children }) => {
    const { user, isLoggedIn, loading } = useAuth();

    if (loading) {
        return null;
    }

    if (!isLoggedIn || !hasRequiredRole(user?.roles, allowedRoles)) {
        return null; 
    }

    return children;
};

export default RequireRole;
