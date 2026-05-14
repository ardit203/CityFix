import React from 'react';
import { Stack, Button } from "@mui/material";
import { ArrowBack, Edit, Delete } from "@mui/icons-material";

const ActionBar = ({
                       onBack,
                       backLabel = "Back",
                       onSecondaryBack, // <-- New prop!
                       secondaryBackLabel = "Back", // <-- New prop!
                       onEdit,
                       editLabel = "Edit",
                       onDelete,
                       deleteLabel = "Delete",
                       children
                   }) => {
    return (
        <Stack
            direction={{xs: "column", sm: "row"}}
            spacing={1.5}
            className="section-card"
            sx={{ mb: 3, p: 1.5, justifyContent: 'space-between', alignItems: {xs: 'stretch', sm: 'center'} }}
        >
            {/* 1. The Left Side (Back Buttons) */}
            <Stack direction="row" spacing={1.25} flexWrap="wrap" useFlexGap>
                {onBack && (
                    <Button
                        variant="outlined"
                        startIcon={<ArrowBack />}
                        onClick={onBack}
                    >
                        {backLabel}
                    </Button>
                )}

                {onSecondaryBack && (
                    <Button
                        variant="text" // Using 'text' variant so it doesn't compete too much with the primary back button
                        color="inherit"
                        startIcon={<ArrowBack />}
                        onClick={onSecondaryBack}
                    >
                        {secondaryBackLabel}
                    </Button>
                )}
            </Stack>

            {/* 2. The Right Side (Action Buttons) */}
            <Stack direction="row" spacing={1.25} flexWrap="wrap" useFlexGap sx={{ alignItems: 'center', justifyContent: {xs: "flex-start", sm: "flex-end"} }}>
                {children}

                {onEdit && (
                    <Button
                        variant="contained"
                        color="primary"
                        startIcon={<Edit />}
                        onClick={onEdit}
                    >
                        {editLabel}
                    </Button>
                )}

                {onDelete && (
                    <Button
                        variant="outlined"
                        color="error"
                        startIcon={<Delete />}
                        onClick={onDelete}
                    >
                        {deleteLabel}
                    </Button>
                )}
            </Stack>
        </Stack>
    );
};

export default ActionBar;
