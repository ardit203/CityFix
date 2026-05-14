import React from 'react';
import {
    Box,
    Button,
    Divider,
    Paper,
    Stack,
    ToggleButton,
    ToggleButtonGroup,
    Typography
} from "@mui/material";
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
                       actions,
                       children
                   }) => {
    const hasActions = Boolean(actions || onAdd || (viewMode && onViewChange));

    return (
        <Paper
            elevation={0}
            className="filter-side-panel"
            sx={{
                display: "flex",
                flexDirection: "column",
                gap: 1.5,
                p: {xs: 1.5, md: 2},
                position: {lg: "sticky"},
                top: {lg: 18},
                maxHeight: {lg: "calc(100svh - 36px)"},
                overflowY: {lg: "auto"},
                "& .MuiButton-root": {
                    minHeight: 44
                },
                "& .MuiToggleButtonGroup-root": {
                    height: 44
                },
                "& .MuiTextField-root": {
                    width: "100%"
                },
                "& .MuiFormControl-root": {
                    width: "100%"
                },
                "& .MuiButton-root:not(.MuiToggleButton-root)": {
                    width: "100%"
                }
            }}
        >
            <Box>
                <Typography variant="overline" color="text.secondary">
                    Filters
                </Typography>

                <Typography variant="h6" fontWeight={700}>
                    Refine Results
                </Typography>
            </Box>

            <Divider/>

            {hasActions && (
                <>
                    <Stack spacing={1}>
                        {actions}

                        {viewMode && onViewChange && (
                            <ToggleButtonGroup
                                value={viewMode}
                                exclusive
                                onChange={onViewChange}
                                size="small"
                                color="primary"
                                fullWidth
                                sx={{
                                    "& .MuiToggleButton-root": {
                                        flex: 1,
                                        minWidth: 0
                                    }
                                }}
                            >
                                <ToggleButton value="table" aria-label="table view">
                                    <ViewListIcon/>
                                </ToggleButton>
                                <ToggleButton value="grid" aria-label="card view">
                                    <ViewModuleIcon/>
                                </ToggleButton>
                            </ToggleButtonGroup>
                        )}

                        {onAdd && (
                            <Button
                                variant="contained"
                                color="secondary"
                                startIcon={<AddIcon/>}
                                onClick={onAdd}
                            >
                                {addLabel}
                            </Button>
                        )}
                    </Stack>

                    <Divider/>
                </>
            )}

            <Stack spacing={1}>
                <Button variant="contained" startIcon={<SearchIcon />} onClick={onSearch}>
                    Search
                </Button>

                <Button variant="outlined" startIcon={<ClearIcon />} onClick={onClear}>
                    Clear
                </Button>
            </Stack>

            <Divider/>

            <Stack spacing={1.5}>
                {children}
            </Stack>
        </Paper>
    );
};

export default FilterBar;
