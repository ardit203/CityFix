import React from 'react';
import {Box, Typography} from "@mui/material";
import SearchOffIcon from '@mui/icons-material/SearchOff'; // Or InboxIcon, or any modern icon!

const EmptyState = ({
                        message = "No records found.",
                        subMessage = "Try adjusting your filters or search terms.",
                        icon = <SearchOffIcon sx={{fontSize: 64, color: 'text.secondary', opacity: 0.5}}/>
                    }) => {
    return (
        <Box
            sx={{
                display: "flex",
                flexDirection: "column",
                alignItems: "center",
                justifyContent: "center",
                py: 8,
                px: 3,
                textAlign: "center",
                background: 'linear-gradient(180deg, rgba(255, 255, 255, 0.94), rgba(248, 251, 253, 0.92))',
                borderRadius: 4,
                border: '1px dashed',
                borderColor: 'divider',
                mt: 2,
                boxShadow: "0 16px 40px rgba(23, 32, 51, 0.07)"
            }}
        >
            {icon}
            <Typography variant="h6" color="text.secondary" sx={{mt: 2, fontWeight: 500}}>
                {message}
            </Typography>
            {subMessage && (
                <Typography variant="body2" color="text.secondary" sx={{mt: 1}}>
                    {subMessage}
                </Typography>
            )}
        </Box>
    );
};

export default EmptyState;
