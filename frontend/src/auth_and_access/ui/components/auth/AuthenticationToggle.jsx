
import {useNavigate} from "react-router";
import {
    Avatar,
    Box,
    Button,
    Divider,
    ListItemIcon,
    ListItemText,
    Menu,
    MenuItem,
    Typography
} from "@mui/material";
import LoginIcon from "@mui/icons-material/Login";
import LogoutIcon from "@mui/icons-material/Logout";
import ManageAccountsIcon from "@mui/icons-material/ManageAccounts";
import PersonIcon from "@mui/icons-material/Person";
import KeyboardArrowDownIcon from "@mui/icons-material/KeyboardArrowDown";
import {useState} from "react";
import useAuth from "../../../hooks/auth/useAuth.js";
import useProfile from "../../../hooks/profile/useProfile.js";

const AuthenticationToggle = () => {
    const {logout, isLoggedIn, user} = useAuth();
    const [anchorEl, setAnchorEl] = useState(null);
    const navigate = useNavigate();
    const menuOpen = Boolean(anchorEl);

    const accountName = user?.username || user?.sub || user?.email || "Account";
    const roleLabel = user?.roles?.[0]?.replace("ROLE_", "")?.toLowerCase() || "cityfix";

    const {user: userProfile} = useProfile();
    console.log(userProfile)
    const src = userProfile?.profile?.profilePictureUrl;
    const initial =accountName.charAt(0).toUpperCase()


    const handleLogin = () => {
        navigate("/login");
    };

    const handleLogout = () => {
        logout();
        navigate("/login");
    };

    const handleOpenMenu = (event) => {
        setAnchorEl(event.currentTarget);
    };

    const handleCloseMenu = () => {
        setAnchorEl(null);
    };

    const handleNavigate = (path) => {
        handleCloseMenu();
        navigate(path);
    };

    if (!isLoggedIn) {
        return (
            <Button
                className="auth-button logged-out"
                startIcon={<LoginIcon />}
                variant="contained"
                onClick={handleLogin}
            >
                <span className="auth-button-label">Login</span>
            </Button>
        );
    }

    return (
        <>
            <Button
                className="account-menu-button"
                onClick={handleOpenMenu}
                endIcon={<KeyboardArrowDownIcon />}
                aria-controls={menuOpen ? "account-menu" : undefined}
                aria-haspopup="true"
                aria-expanded={menuOpen ? "true" : undefined}
            >
                <Avatar src={src} className="account-avatar">{initial}</Avatar>
                <Box className="account-button-copy">
                    <Typography component="span" className="account-button-label">
                        {accountName}
                    </Typography>
                    <Typography component="span" className="account-button-role">
                        {roleLabel}
                    </Typography>
                </Box>
            </Button>

            <Menu
                id="account-menu"
                anchorEl={anchorEl}
                open={menuOpen}
                onClose={handleCloseMenu}
                anchorOrigin={{ vertical: "bottom", horizontal: "right" }}
                transformOrigin={{ vertical: "top", horizontal: "right" }}
                slotProps={{
                    paper: {
                        className: "account-menu-paper"
                    }
                }}
            >
                <Box className="account-menu-header">
                    <Avatar src={src} className="account-menu-avatar">{initial}</Avatar>
                    <Box>
                        <Typography className="account-menu-name">{accountName}</Typography>
                        <Typography className="account-menu-role">{roleLabel}</Typography>
                    </Box>
                </Box>

                <Divider className="account-menu-divider" />

                <MenuItem className="account-menu-item" onClick={() => handleNavigate("/profile")}>
                    <ListItemIcon>
                        <PersonIcon fontSize="small" />
                    </ListItemIcon>
                    <ListItemText primary="My Profile" secondary="View your account overview" />
                </MenuItem>

                <MenuItem className="account-menu-item" onClick={() => handleNavigate("/profile/edit")}>
                    <ListItemIcon>
                        <ManageAccountsIcon fontSize="small" />
                    </ListItemIcon>
                    <ListItemText primary="Edit Profile" secondary="Update details and password" />
                </MenuItem>

                <Divider className="account-menu-divider" />

                <MenuItem className="account-menu-item danger" onClick={handleLogout}>
                    <ListItemIcon>
                        <LogoutIcon fontSize="small" />
                    </ListItemIcon>
                    <ListItemText primary="Logout" secondary="End this session" />
                </MenuItem>
            </Menu>
        </>
    );
};

export default AuthenticationToggle;
