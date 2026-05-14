import React from 'react';
import { Box, LinearProgress } from "@mui/material";

const LoadingBar = ({ loading }) => {
    return (
        <Box className="loading-rail">
            {loading && <LinearProgress color="primary" />}
        </Box>
    );
};

export default LoadingBar;
