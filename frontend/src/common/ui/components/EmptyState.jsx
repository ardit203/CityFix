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
                textAlign: "center",
                backgroundColor: 'background.paper',
                borderRadius: 2,
                border: '1px dashed',
                borderColor: 'divider',
                mt: 2
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
