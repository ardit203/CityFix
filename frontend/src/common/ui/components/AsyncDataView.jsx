import React from 'react';
import { Box, CircularProgress, Button } from "@mui/material";
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
            <Box sx={{ display: "flex", justifyContent: "center", alignItems: "center", minHeight: "60vh" }}>
                <CircularProgress />
            </Box>
        );
    }

    // 2. The Error or "Not Found" State
    if (error || !data) {
        return (
            <Box sx={{ pt: 4 }}>
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
