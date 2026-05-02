import { Box, Container, Typography, Button } from '@mui/material';
import ArrowForwardIcon from '@mui/icons-material/ArrowForward';

const HomePage = () => {
    return (
        <Box sx={{ m: 0, p: 0, flex: 1, display: 'flex', flexDirection: 'column', justifyContent: 'center' }}>
            <Container maxWidth='lg' sx={{ textAlign: 'center', py: 10 }}>
                <Typography 
                    variant='h1' 
                    className='text-gradient animate-fade-in-up'
                    sx={{ 
                        fontSize: { xs: '3rem', md: '5rem' },
                        mb: 3,
                        lineHeight: 1.1
                    }}
                >
                    Fix Your City.<br/>
                    Build the Future.
                </Typography>
                
                <Typography 
                    variant='h5' 
                    color="text.secondary"
                    className='animate-fade-in-up delay-100'
                    sx={{ mb: 6, maxWidth: '800px', mx: 'auto', fontWeight: 400 }}
                >
                    Welcome to the CityFix platform. Report issues, track progress, and contribute to making our urban spaces better for everyone.
                </Typography>

                <Box className='animate-fade-in-up delay-200'>
                    <Button 
                        variant="contained" 
                        color="primary" 
                        size="large"
                        endIcon={<ArrowForwardIcon />}
                        sx={{ mr: 2, py: 1.5, px: 4, fontSize: '1.1rem' }}
                    >
                        Get Started
                    </Button>
                    <Button 
                        variant="outlined" 
                        color="secondary" 
                        size="large"
                        sx={{ 
                            py: 1.5, px: 4, fontSize: '1.1rem',
                            borderWidth: '2px',
                            '&:hover': { borderWidth: '2px' }
                        }}
                    >
                        View Reports
                    </Button>
                </Box>
            </Container>
        </Box>
    );
};

export default HomePage;