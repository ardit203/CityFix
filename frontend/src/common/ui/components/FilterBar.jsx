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
                gap: 1.1,
                p: 1.75,
                position: {lg: "sticky"},
                top: {lg: 14},
                maxHeight: {lg: "calc(100svh - 28px)"},
                overflowY: {lg: "auto"},
                "& .MuiButton-root": {
                    minHeight: 38,
                    borderRadius: 1.25,
                    fontSize: "0.8rem",
                    fontWeight: 750,
                    py: 0.6
                },
                "& .MuiToggleButtonGroup-root": {
                    height: 36
                },
                "& .MuiToggleButton-root": {
                    minHeight: 36,
                    py: 0,
                    borderRadius: 1.25,
                    "& svg": {
                        fontSize: "1.08rem"
                    }
                },
                "& .MuiTextField-root": {
                    width: "100%"
                },
                "& .MuiFormControl-root": {
                    width: "100%"
                },
                "& .MuiInputBase-root": {
                    minHeight: 38,
                    borderRadius: 1.25,
                    fontSize: "0.84rem"
                },
                "& .MuiInputBase-input": {
                    py: 0.9
                },
                "& .MuiSelect-select": {
                    py: "8px !important"
                },
                "& .MuiInputLabel-root": {
                    fontSize: "0.82rem"
                },
                "& .MuiInputLabel-sizeSmall:not(.MuiInputLabel-shrink)": {
                    transform: "translate(14px, 8px) scale(1)"
                },
                "& .MuiButton-root:not(.MuiToggleButton-root)": {
                    width: "100%"
                },
                "& .MuiDivider-root": {
                    borderColor: "rgba(23, 32, 51, 0.08)"
                }
            }}
        >
            <Box>
                <Typography
                    variant="overline"
                    color="text.secondary"
                    sx={{
                        display: "block",
                        fontSize: "0.64rem",
                        fontWeight: 750,
                        lineHeight: 1.15,
                        letterSpacing: 0.4,
                        opacity: 0.78
                    }}
                >
                    Filters
                </Typography>

                <Typography variant="subtitle1" fontWeight={800} sx={{fontSize: "1.05rem", lineHeight: 1.25, mt: 0.25}}>
                    Refine Results
                </Typography>
            </Box>

            <Divider/>

            {hasActions && (
                <>
                    <Stack spacing={0.75}>
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

            <Stack spacing={0.65}>
                <Button variant="contained" startIcon={<SearchIcon />} onClick={onSearch}>
                    Search
                </Button>

                <Button variant="outlined" startIcon={<ClearIcon />} onClick={onClear}>
                    Clear
                </Button>
            </Stack>

            <Divider/>

            <Stack spacing={1}>
                {children}
            </Stack>
        </Paper>
    );
};

export default FilterBar;
