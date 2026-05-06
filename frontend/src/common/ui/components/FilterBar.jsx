import React from 'react';
import { Box, Button, ToggleButton, ToggleButtonGroup } from "@mui/material";
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
        <Box
            sx={{
                display: "flex",
                gap: 2,
                mb: 3,
                flexWrap: "wrap",
                alignItems: "center"
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
            <Box sx={{ display: 'flex', gap: 2, ml: 'auto', alignItems: 'center' }}>

                {/* Only render the toggle if viewMode props are provided */}
                {viewMode && onViewChange && (
                    <ToggleButtonGroup
                        value={viewMode}
                        exclusive
                        onChange={onViewChange}
                        size="small"
                        color="primary"
                    >
                        <ToggleButton value="table" aria-label="table view">
                            <ViewListIcon />
                        </ToggleButton>
                        <ToggleButton value="grid" aria-label="card view">
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
        </Box>
    );
};

export default FilterBar;
