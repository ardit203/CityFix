import React from 'react';
import { Box, Button, Paper, ToggleButton, ToggleButtonGroup } from "@mui/material";
import SearchIcon from '@mui/icons-material/Search';
import ClearIcon from '@mui/icons-material/Clear';
import AddIcon from '@mui/icons-material/Add';
import ViewListIcon from '@mui/icons-material/ViewList';
import ViewModuleIcon from '@mui/icons-material/ViewModule';

const FilterBar = ({
                       onSearch,
                       onClear,
                       onAdd,
                       addLabel = "Add",
                       viewMode,
                       onViewChange,
                       children
                   }) => {
    return (
        <Paper
            elevation={0}
            className="section-card"
            sx={{
                display: "flex",
                columnGap: 1.25,
                rowGap: 1.25,
                mb: 3,
                p: {xs: 1.5, md: 2},
                flexWrap: "wrap",
                alignItems: "center",
                alignContent: "flex-start",
                "& .MuiButton-root": {
                    minHeight: 46
                },
                "& .MuiToggleButtonGroup-root": {
                    height: 46
                },
                "& .MuiTextField-root": {
                    minWidth: {xs: "100%", sm: 190, xl: 210}
                },
                "& .MuiFormControl-root": {
                    minWidth: {xs: "100%", sm: 142, xl: 152}
                },
                "& > .MuiBox-root": {
                    marginLeft: {xs: 0, md: "auto"}
                }
            }}
        >
            {/* 1. Custom TextFields injected here */}
            {children}

            {/* 2. Standard Search & Clear Buttons */}
            <Button variant="contained" startIcon={<SearchIcon />} onClick={onSearch}>
                Search
            </Button>

            <Button variant="outlined" startIcon={<ClearIcon />} onClick={onClear}>
                Clear
            </Button>

            {/* 3. The Toggle Switch and Add Button (Pushed to the right using ml: auto) */}
            <Box
                sx={{
                    display: 'flex',
                    gap: 1.25,
                    ml: 'auto',
                    alignItems: 'center',
                    justifyContent: 'flex-end',
                    flex: {xs: '1 1 100%', md: '0 0 auto'},
                    flexWrap: 'nowrap',
                    minWidth: 'max-content',
                    "& .MuiToggleButton-root": {
                        minWidth: 52
                    }
                }}
            >

                {/* Only render the toggle if viewMode props are provided */}
                {viewMode && onViewChange && (
                    <ToggleButtonGroup
                        value={viewMode}
                        exclusive
                        onChange={onViewChange}
                        size="small"
                        color="primary"
                    >
                        <ToggleButton value="table" aria-label="table view" sx={{ px: 1.5 }}>
                            <ViewListIcon />
                        </ToggleButton>
                        <ToggleButton value="grid" aria-label="card view" sx={{ px: 1.5 }}>
                            <ViewModuleIcon />
                        </ToggleButton>
                    </ToggleButtonGroup>
                )}

                {onAdd && (
                    <Button
                        variant="contained"
                        color="secondary"
                        startIcon={<AddIcon />}
                        onClick={onAdd}
                    >
                        {addLabel}
                    </Button>
                )}
            </Box>
        </Paper>
    );
};

export default FilterBar;
