import useAuth from "../../../../hooks/useAuth.js";
import {useNavigate} from "react-router";
import {Button} from "@mui/material";

const AuthenticationToggle = () => {
    const {logout, isLoggedIn} = useAuth();

    const navigate = useNavigate();

    const handleLogin = () => {
        navigate("/");
    };

    const handleLogout = () => {
        logout();
        navigate("/");
    };

    return (
        <Button
            color={!isLoggedIn ? "primary" : "secondary"}
            variant={!isLoggedIn ? "contained" : "outlined"}
            onClick={!isLoggedIn ? handleLogin : handleLogout}
            sx={{ 
                borderRadius: '8px', 
                fontWeight: 600,
                boxShadow: !isLoggedIn ? '0 4px 14px 0 rgba(168, 85, 247, 0.39)' : 'none',
                '&:hover': {
                    boxShadow: !isLoggedIn ? '0 6px 20px rgba(168, 85, 247, 0.23)' : 'none',
                }
            }}
        >
            {!isLoggedIn ? "Login" : "Logout"}
        </Button>
    );
};

export default AuthenticationToggle;
