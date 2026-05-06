import React from 'react';
import {Box, CircularProgress, MenuItem, Pagination, Select, Typography} from "@mui/material";
import EmptyState from "./EmptyState.jsx";

const PaginatedDataView = ({
                               loading,
                               data = [],
                               emptyMessage = "No records found.",
                               emptySubMessage = "Try adjusting your filters.",
                               pagination,
                               onPageChange,
                               onSizeChange,
                               children
                           }) => {
    // 1. First Load Scenario: No data yet, show big spinner
    if (loading && data.length === 0) {
        return (
            <Box sx={{display: 'flex', justifyContent: 'center', py: 10}}>
                <CircularProgress/>
            </Box>
        );
    }

    // 2. Empty State Scenario: Finished loading, zero results
    if (data.length === 0) {
        return (
            <EmptyState
                message={emptyMessage}
                subMessage={emptySubMessage}
            />
        );
    }

    // 3. Normal Scenario: We have data! Render the children (Table/Grid) and Pagination
    return (
        <>
            <Box
                sx={{
                    opacity: loading ? 0.6 : 1,
                    pointerEvents: loading ? 'none' : 'auto',
                    transition: 'opacity 0.2s ease-in-out'
                }}
            >
                {children}
            </Box>

            {pagination && onPageChange && (

                <Box sx={{display: "flex", justifyContent: "center", mt: 3}}>
                    {/* The Page Size Dropdown */}
                    <Box sx={{display: 'flex', alignItems: 'center', gap: 1}}>
                        <Typography variant="body2" color="text.secondary">
                            Rows per page:
                        </Typography>
                        {onSizeChange && (
                            <Select
                                size="small"
                                value={pagination.size}
                                onChange={onSizeChange}
                                disabled={loading}
                                sx={{height: 32}} // keeps it compact
                            >
                                <MenuItem value={5}>5</MenuItem>
                                <MenuItem value={10}>10</MenuItem>
                                <MenuItem value={25}>25</MenuItem>
                                <MenuItem value={50}>50</MenuItem>
                            </Select>
                        )}
                    </Box>
                    <Pagination
                        count={pagination.totalPages}
                        page={pagination.page + 1}
                        onChange={onPageChange}
                        disabled={loading}
                        showFirstButton
                        showLastButton
                    />
                </Box>
            )}
        </>
    );
};

export default PaginatedDataView;
