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
            color="inherit"
            variant={!isLoggedIn ? "text" : "outlined"}
            onClick={!isLoggedIn ? handleLogin : handleLogout}
        >
            {!isLoggedIn ? "Login" : "Logout"}
        </Button>
    );
};

export default AuthenticationToggle;
