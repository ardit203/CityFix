import { Box, Container, Typography } from '@mui/material';
import LocationPicker from "../../../../request_management/ui/component/request/LocationPicker.jsx";

const HomePage = () => {
    return (
        <Box sx={{ m: 0, p: 0 }}>
            <Container maxWidth='xl' sx={{ mt: 3, py: 3 }}>
                <Typography variant='h4' gutterBottom>
                    Welcome to CityFix App! 👋
                </Typography>
                <Typography variant='body1' sx={{ mb: 4 }}>
                    This is the home page.
                </Typography>
            </Container>
            <LocationPicker
                onLocationSelect={(location) => {
                    console.log(location.latitude);
                    console.log(location.longitude);
                    console.log(location.address);
                }}
            />
        </Box>
    );
};

export default HomePage;