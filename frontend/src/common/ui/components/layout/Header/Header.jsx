import './Header.css';
import {
    AppBar, Box, Button, Drawer, IconButton, List, ListItem, ListItemButton, ListItemText, Toolbar, Typography
} from '@mui/material';
import MenuIcon from '@mui/icons-material/Menu';
import {Link, useLocation} from 'react-router';
import {useState} from 'react';
import AuthenticationToggle from "../../../../../auth_and_access/ui/components/auth/AuthenticationToggle.jsx";
import useAuth from "../../../../../auth_and_access/hooks/auth/useAuth.js";

const pages = [
    { path: '/', name: 'home' },
    { path: '/users', name: 'users', allowedRoles: ['ROLE_ADMINISTRATOR'] },
    { path: '/departments', name: 'departments', allowedRoles: ['ROLE_ADMINISTRATOR'] },
    { path: '/municipalities', name: 'municipalities', allowedRoles: ['ROLE_ADMINISTRATOR'] },
    { path: '/categories', name: 'categories', allowedRoles: ['ROLE_ADMINISTRATOR'] },
    { path: '/staff', name: 'staff', allowedRoles: ['ROLE_ADMINISTRATOR', 'ROLE_MANAGER'] },
    { path: '/requests', name: 'requests' },
    { path: '/requests/assignments', name: 'assignments', allowedRoles: ['ROLE_ADMINISTRATOR', 'ROLE_MANAGER'] },
    { path: '/reports', name: 'reports', allowedRoles: ['ROLE_ADMINISTRATOR', 'ROLE_MANAGER'] }
];

const Header = () => {
    const [drawerOpen, setDrawerOpen] = useState(false);
    const { user } = useAuth();
    const { pathname } = useLocation();

    const visiblePages = pages.filter((page) => {
        if (!page.allowedRoles) {
            return true;
        }

        return page.allowedRoles.some((role) => user?.roles?.includes(role));
    });

    return (
        <Box className='site-header-shell'>
            <AppBar position='sticky' elevation={0} className='site-header'>
                <Toolbar className='site-toolbar'>
                    <IconButton
                        size='large'
                        edge='start'
                        color='inherit'
                        aria-label='menu'
                        className='mobile-menu-button'
                        sx={{ display: { md: 'none' } }}
                        onClick={() => setDrawerOpen(true)}
                    >
                        <MenuIcon />
                    </IconButton>

                    <Typography variant='h6' component={Link} to='/' className='brand-logo'>
                        <Box component='span' className='brand-mark'>C</Box>
                        CityFix
                    </Typography>

                    <Box className='desktop-nav' sx={{ display: { xs: 'none', md: 'flex' } }}>
                        {visiblePages.map((page) => {
                            const isActive = page.path === '/'
                                ? pathname === '/'
                                : pathname === page.path || pathname.startsWith(`${page.path}/`);

                            return (
                                <Link key={page.name} to={page.path} className='nav-link-wrapper'>
                                    <Button className={`nav-link ${isActive ? 'active' : ''}`}>
                                        {page.name}
                                    </Button>
                                </Link>
                            );
                        })}
                    </Box>

                    <Box className='header-actions'>
                        <AuthenticationToggle />
                    </Box>
                </Toolbar>
            </AppBar>

            <Drawer
                anchor='left'
                open={drawerOpen}
                onClose={() => setDrawerOpen(false)}
                PaperProps={{ className: 'mobile-drawer' }}
            >
                <Box role='presentation' onClick={() => setDrawerOpen(false)}>
                    <Box className='drawer-brand'>
                        <Box component='span' className='brand-mark'>C</Box>
                        CityFix
                    </Box>

                    <List className='drawer-list'>
                        {visiblePages.map((page) => {
                            const isActive = page.path === '/'
                                ? pathname === '/'
                                : pathname === page.path || pathname.startsWith(`${page.path}/`);

                            return (
                                <ListItem key={page.name} disablePadding>
                                    <ListItemButton
                                        component={Link}
                                        to={page.path}
                                        className={`drawer-link ${isActive ? 'active' : ''}`}
                                    >
                                        <ListItemText
                                            primary={page.name}
                                            sx={{ textTransform: 'capitalize' }}
                                        />
                                    </ListItemButton>
                                </ListItem>
                            );
                        })}
                    </List>
                </Box>
            </Drawer>
        </Box>
    );
};

export default Header;
