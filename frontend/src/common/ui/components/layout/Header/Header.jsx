import './Header.css';
import {
    AppBar, Box, Button, Drawer, IconButton, List, ListItem, ListItemButton, ListItemText, Toolbar, Typography
} from '@mui/material';
import MenuIcon from '@mui/icons-material/Menu';
import {Link} from 'react-router';
import {useState} from 'react';
import AuthenticationToggle from "../../../../../auth_and_access/ui/components/auth/AuthenticationToggle.jsx";
import useAuth from "../../../../../auth_and_access/hooks/auth/useAuth.js";

const pages = [
    { path: '/', name: 'home' },
    { path: '/users', name: 'users', allowedRoles: ['ROLE_ADMINISTRATOR'] },
    { path: '/profile', name: 'profile' },
    { path: '/departments', name: 'departments', allowedRoles: ['ROLE_ADMINISTRATOR'] },
    { path: '/municipalities', name: 'municipalities', allowedRoles: ['ROLE_ADMINISTRATOR'] },
    { path: '/categories', name: 'categories', allowedRoles: ['ROLE_ADMINISTRATOR'] },
    { path: '/staff', name: 'staff', allowedRoles: ['ROLE_ADMINISTRATOR', 'ROLE_MANAGER'] },
    { path: '/requests', name: 'requests' },
    { path: '/requests/assignments', name: 'assignments', allowedRoles: ['ROLE_ADMINISTRATOR', 'ROLE_MANAGER'] },
    { path: '/reports', name: 'reports', allowedRoles: ['ROLE_ADMINISTRATOR', 'ROLE_MANAGER'] }
];

console.log("test")

const Header = () => {
    const [drawerOpen, setDrawerOpen] = useState(false);
    const { user } = useAuth();

    const visiblePages = pages.filter((page) => {
        if (!page.allowedRoles) {
            return true;
        }

        return page.allowedRoles.some((role) => user?.roles?.includes(role));
    });

    return (
        <Box>
            <AppBar position='static'>
                <Toolbar>
                    <IconButton
                        size='large'
                        edge='start'
                        color='inherit'
                        aria-label='menu'
                        sx={{ mr: 2, display: { md: 'none' } }}
                        onClick={() => setDrawerOpen(true)}
                    >
                        <MenuIcon />
                    </IconButton>

                    <Typography variant='h6' component='div' sx={{ mr: 3 }}>
                        CityFix
                    </Typography>

                    <Box sx={{ flexGrow: 1, display: { xs: 'none', md: 'flex' } }}>
                        {visiblePages.map((page) => (
                            <Link key={page.name} to={page.path}>
                                <Button sx={{ my: 2, color: 'white', display: 'block' }}>
                                    {page.name}
                                </Button>
                            </Link>
                        ))}
                    </Box>

                    <AuthenticationToggle />
                </Toolbar>
            </AppBar>

            <Drawer anchor='left' open={drawerOpen} onClose={() => setDrawerOpen(false)}>
                <Box sx={{ width: 240 }} role='presentation' onClick={() => setDrawerOpen(false)}>
                    <List>
                        {visiblePages.map((page) => (
                            <ListItem key={page.name} disablePadding>
                                <ListItemButton component={Link} to={page.path}>
                                    <ListItemText
                                        primary={page.name}
                                        sx={{ textTransform: 'capitalize' }}
                                    />
                                </ListItemButton>
                            </ListItem>
                        ))}
                    </List>
                </Box>
            </Drawer>
        </Box>
    );
};

export default Header;
