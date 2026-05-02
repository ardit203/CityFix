import React from 'react';
import {Link} from "react-router";
import {AppBar, Box, Button, IconButton, Toolbar, Typography} from "@mui/material";
import MenuIcon from '@mui/icons-material/Menu';
import "./Header.css";
import AuthenticationToggle
    from "../../../../../auth_and_access/ui/components/auth/AuthenticationToggle/AuthenticationToggle.jsx";

const pages = [
    {"path": "/", "name": "Home"},
    {"path": "/users", "name": "Users"},
];

const Header = () => {
    return (
        <Box sx={{ px: 2, pt: 2 }}>
            <AppBar position="static" className="floating-header animate-fade-in-up">
                <Toolbar sx={{ px: { xs: 2, md: 4 } }}>
                    <IconButton
                        size="large"
                        edge="start"
                        color="inherit"
                        aria-label="menu"
                        sx={{mr: 2, display: { md: 'none' }}}
                    >
                        <MenuIcon/>
                    </IconButton>
                    <Typography variant="h6" component="div" sx={{mr: 4}} className="logo-text">
                        CITYFIX
                    </Typography>
                    <Box sx={{flexGrow: 1, display: {xs: "none", md: "flex"}, gap: 1}}>
                        {pages.map((page) => (
                            <Link key={page.name} to={page.path}>
                                <Button
                                    className="nav-link"
                                    sx={{my: 2, display: "block"}}
                                >
                                    {page.name}
                                </Button>
                            </Link>
                        ))}
                    </Box>
                    <AuthenticationToggle/>
                </Toolbar>
            </AppBar>
        </Box>
    );
};

export default Header;