import './Layout.css';
import { Box, Container } from '@mui/material';
import { Outlet } from 'react-router';
import Header from "../Header/Header.jsx";


const Layout = () => {
    return (
        <Box className='layout-box'>
            <Header/>
            <Container className='outlet-container page-surface' maxWidth='xl'>
                <Outlet/>
            </Container>
        </Box>
    );
};

export default Layout;
