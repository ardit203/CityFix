import React from 'react';
import { Box, Button, LinearProgress, Paper, Stack, Typography } from "@mui/material";
import { ArrowBack } from "@mui/icons-material";
import { useNavigate } from "react-router";
import EmptyState from "./EmptyState.jsx";

const AsyncDataView = ({
                           loading,
                           error,
                           data,
                           notFoundMessage = "Record not found.",
                           backUrl, // e.g., "/departments"
                           children // The actual page content!
                       }) => {
    const navigate = useNavigate();

    // 1. The Loading State
    if (loading) {
        return (
            <Box className="async-state-shell">
                <Paper elevation={0} className="async-state-card">
                    <Stack spacing={2}>
                        <Typography variant="h6">Loading workspace</Typography>
                        <Typography variant="body2" color="text.secondary">
                            Preparing the latest CityFix data for this view.
                        </Typography>
                        <LinearProgress />
                    </Stack>
                </Paper>
            </Box>
        );
    }

    // 2. The Error or "Not Found" State
    if (error || !data) {
        return (
            <Box className="async-state-shell">
                <EmptyState
                    message={error ? "An error occurred" : notFoundMessage}
                    subMessage={error || "This record may have been deleted or does not exist."}
                />

                {/* Optional "Go Back" button if a backUrl is provided! */}
                {backUrl && (
                    <Box sx={{ display: 'flex', justifyContent: 'center', mt: 3 }}>
                        <Button
                            variant="outlined"
                            startIcon={<ArrowBack />}
                            onClick={() => navigate(backUrl)}
                        >
                            Go Back
                        </Button>
                    </Box>
                )}
            </Box>
        );
    }

    // 3. The Success State! Render the actual page.
    return children;
};

export default AsyncDataView;
