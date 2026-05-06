import React from 'react';
import { Box, LinearProgress } from "@mui/material";

const LoadingBar = ({ loading }) => {
    return (
        <Box sx={{ height: 4, width: '100%', mb: 1 }}>
            {loading && <LinearProgress color="primary" />}
        </Box>
    );
};

export default LoadingBar;
