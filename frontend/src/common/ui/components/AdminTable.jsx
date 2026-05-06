import React, { useState } from 'react';
import {
    Table, TableBody, TableCell, TableContainer, TableHead, TableRow,
    Paper, Checkbox, IconButton, Menu, MenuItem, Toolbar, Typography, Button, Box, alpha
} from '@mui/material';
import MoreVertIcon from '@mui/icons-material/MoreVert';
import DeleteIcon from '@mui/icons-material/Delete';
import EditIcon from '@mui/icons-material/Edit';
import InfoIcon from '@mui/icons-material/Info';

const AdminTable = ({ data, columns, onInfo, onEdit, onDelete, onBulkDelete }) => {
    const [selectedIds, setSelectedIds] = useState([]);
    const [anchorEl, setAnchorEl] = useState(null);
    const [activeRow, setActiveRow] = useState(null);

    // --- Checkbox Logic ---
    const handleSelectAll = (event) => {
        if (event.target.checked) setSelectedIds(data.map(item => item.id));
        else setSelectedIds([]);
    };

    const handleSelectOne = (event, id) => {
        if (event.target.checked) setSelectedIds(prev => [...prev, id]);
        else setSelectedIds(prev => prev.filter(selectedId => selectedId !== id));
    };

    const isSelected = (id) => selectedIds.indexOf(id) !== -1;

    // --- Action Menu Logic ---
    const handleMenuOpen = (event, item) => {
        setAnchorEl(event.currentTarget);
        setActiveRow(item);
    };

    const handleMenuClose = () => {
        setAnchorEl(null);
        setActiveRow(null);
    };

    const handleAction = (actionFn) => {
        if (actionFn && activeRow) actionFn(activeRow);
        handleMenuClose();
    };

    return (
        <Paper
            sx={{
                width: '100%',
                overflow: 'hidden',
                borderRadius: 2,
                border: '1px solid',
                borderColor: 'divider',
                boxShadow: 2 // Premium shadow
            }}
        >
            {/* The Toolbar that appears when checkboxes are selected! */}
            {selectedIds.length > 0 && (
                <Toolbar
                    sx={{
                        backgroundColor: (theme) => alpha(theme.palette.primary.main, 0.1),
                        display: 'flex',
                        justifyContent: 'space-between'
                    }}
                >
                    <Typography color="primary" variant="subtitle1" component="div">
                        {selectedIds.length} selected
                    </Typography>

                    {onBulkDelete && (
                        <Button
                            variant="contained"
                            color="error"
                            startIcon={<DeleteIcon />}
                            onClick={() => {
                                onBulkDelete(selectedIds);
                                setSelectedIds([]); // Clear selection after delete
                            }}
                        >
                            Delete Selected
                        </Button>
                    )}
                </Toolbar>
            )}

            <TableContainer>
                <Table size="medium">
                    <TableHead sx={{ backgroundColor: 'background.default' }}>
                        <TableRow>
                            {/* Select All Checkbox */}
                            <TableCell padding="checkbox">
                                <Checkbox
                                    color="primary"
                                    indeterminate={selectedIds.length > 0 && selectedIds.length < data.length}
                                    checked={data.length > 0 && selectedIds.length === data.length}
                                    onChange={handleSelectAll}
                                />
                            </TableCell>

                            {/* Dynamic Columns */}
                            {columns.map((col) => (
                                <TableCell key={col.id} sx={{ fontWeight: 600 }}>
                                    {col.label}
                                </TableCell>
                            ))}

                            {/* Actions Column */}
                            <TableCell align="right" sx={{ fontWeight: 600 }}>Actions</TableCell>
                        </TableRow>
                    </TableHead>

                    <TableBody>
                        {data.map((item) => {
                            const isItemSelected = isSelected(item.id);

                            return (
                                <TableRow
                                    hover
                                    key={item.id}
                                    selected={isItemSelected}
                                    sx={{ '&:last-child td, &:last-child th': { border: 0 } }} // Clean bottom border
                                >
                                    <TableCell padding="checkbox">
                                        <Checkbox
                                            color="primary"
                                            checked={isItemSelected}
                                            onChange={(e) => handleSelectOne(e, item.id)}
                                        />
                                    </TableCell>

                                    {/* Render dynamic columns */}
                                    {columns.map((col) => (
                                        <TableCell key={col.id}>
                                            {/* Support custom render functions if needed, otherwise raw value */}
                                            {col.render ? col.render(item) : item[col.id]}
                                        </TableCell>
                                    ))}

                                    <TableCell align="right">
                                        <IconButton size="small" onClick={(e) => handleMenuOpen(e, item)}>
                                            <MoreVertIcon />
                                        </IconButton>
                                    </TableCell>
                                </TableRow>
                            );
                        })}
                    </TableBody>
                </Table>
            </TableContainer>

            {/* The Floating Action Menu */}
            <Menu
                anchorEl={anchorEl}
                open={Boolean(anchorEl)}
                onClose={handleMenuClose}
                transformOrigin={{ horizontal: 'right', vertical: 'top' }}
                anchorOrigin={{ horizontal: 'right', vertical: 'bottom' }}
            >
                {onInfo && (
                    <MenuItem onClick={() => handleAction(onInfo)}>
                        <InfoIcon fontSize="small" sx={{ mr: 1, color: 'text.secondary' }} /> Info
                    </MenuItem>
                )}
                {onEdit && (
                    <MenuItem onClick={() => handleAction(onEdit)}>
                        <EditIcon fontSize="small" sx={{ mr: 1, color: 'warning.main' }} /> Edit
                    </MenuItem>
                )}
                {onDelete && (
                    <MenuItem onClick={() => handleAction(onDelete)} sx={{ color: 'error.main' }}>
                        <DeleteIcon fontSize="small" sx={{ mr: 1, color: 'error.main' }} /> Delete
                    </MenuItem>
                )}
            </Menu>
        </Paper>
    );
};

export default AdminTable;
