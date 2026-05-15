import React, {useState} from 'react';
import {
    Table,
    TableBody,
    TableCell,
    TableContainer,
    TableHead,
    TableRow,
    Paper,
    Checkbox,
    IconButton,
    Menu,
    MenuItem,
    Toolbar,
    Typography,
    Button,
    Box,
    Chip,
    Stack,
    alpha
} from '@mui/material';
import MoreVertIcon from '@mui/icons-material/MoreVert';
import DeleteIcon from '@mui/icons-material/Delete';
import EditIcon from '@mui/icons-material/Edit';
import InfoIcon from '@mui/icons-material/Info';
import TableRowsOutlinedIcon from '@mui/icons-material/TableRowsOutlined';
import RequireRole from "../../../auth_and_access/ui/components/auth/RequireRole.jsx";

const formatRoleLabel = (value) => {
    if (!value || typeof value !== 'string') {
        return value;
    }

    return value.replace('ROLE_', '').replaceAll('_', ' ');
};

const getColumnSx = (col) => {
    if (col.id === 'id') {
        return {
            width: 64,
            minWidth: 64,
            maxWidth: 72
        };
    }

    if (['name', 'surname', 'username', 'role'].includes(col.id)) {
        return {
            minWidth: 132,
            width: '18%'
        };
    }

    return {
        minWidth: 128
    };
};

const AdminTable = ({data, columns, onInfo, onEdit, onDelete, onBulkDelete}) => {
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
            elevation={0}
            className="admin-table-shell"
            sx={{
                width: '100%',
                overflow: 'hidden',
                borderRadius: 2,
                border: '1px solid rgba(23, 32, 51, 0.1)',
                backgroundColor: 'background.paper',
                boxShadow: '0 10px 28px rgba(23, 32, 51, 0.06)',
                position: 'relative',
                '&::before': {
                    content: '""',
                    display: 'block',
                    height: 3,
                    background: 'rgba(15, 143, 206, 0.55)'
                }
            }}
        >
            <Box
                sx={{
                    display: 'flex',
                    justifyContent: 'space-between',
                    alignItems: 'center',
                    gap: 1.5,
                    px: {xs: 1.5, md: 2},
                    py: 1.15,
                    borderBottom: '1px solid rgba(23, 32, 51, 0.08)',
                    backgroundColor: 'rgba(248, 251, 253, 0.82)'
                }}
            >
                <Stack direction="row" spacing={1} alignItems="center" sx={{minWidth: 0}}>
                    <Box
                        sx={{
                            width: 30,
                            height: 30,
                            borderRadius: 1.5,
                            display: 'grid',
                            placeItems: 'center',
                            color: 'primary.main',
                            bgcolor: 'rgba(15, 143, 206, 0.08)',
                            border: '1px solid rgba(15, 143, 206, 0.12)'
                        }}
                    >
                        <TableRowsOutlinedIcon fontSize="small"/>
                    </Box>

                    <Box sx={{minWidth: 0}}>
                        <Typography variant="subtitle2" fontWeight={750} sx={{lineHeight: 1.15}}>
                            Records
                        </Typography>
                        <Typography variant="caption" color="text.secondary" sx={{display: 'block', lineHeight: 1.2}}>
                            Review and manage matching rows
                        </Typography>
                    </Box>
                </Stack>

                <Chip
                    size="small"
                    color="primary"
                    variant="outlined"
                    label={`${data.length} shown`}
                    sx={{
                        height: 24,
                        borderRadius: 1.25,
                        fontSize: '0.72rem',
                        fontWeight: 700,
                        bgcolor: 'background.paper'
                    }}
                />
            </Box>

            {selectedIds.length > 0 && (
                <Toolbar
                    sx={{
                        minHeight: 44,
                        backgroundColor: (theme) => alpha(theme.palette.primary.main, 0.08),
                        display: 'flex',
                        justifyContent: 'space-between',
                        gap: 1.5,
                        borderBottom: '1px solid',
                        borderColor: 'divider',
                        px: {xs: 1.5, md: 2}
                    }}
                >
                    <Stack direction="row" spacing={1} alignItems="center">
                        <Chip size="small" color="primary" label={selectedIds.length}/>
                        <Typography color="primary" variant="subtitle1" fontWeight={700} component="div">
                            selected
                        </Typography>
                    </Stack>

                    {onBulkDelete && (
                        <Button
                            variant="contained"
                            color="error"
                            startIcon={<DeleteIcon/>}
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

            <TableContainer
                sx={{
                    maxWidth: '100%',
                    overflowX: 'auto',
                    borderTop: '0'
                }}
            >
                <Table size="small" stickyHeader sx={{tableLayout: 'auto'}}>
                    <TableHead>
                        <TableRow
                            sx={{
                                '& .MuiTableCell-head': {
                                    position: 'sticky',
                                    top: 0,
                                    zIndex: 1,
                                    bgcolor: '#f4f8fb',
                                    color: '#526078',
                                    fontSize: '0.7rem',
                                    fontWeight: 750,
                                    letterSpacing: 0.2,
                                    textTransform: 'uppercase',
                                    borderBottom: '1px solid rgba(23, 32, 51, 0.1)',
                                    py: 1,
                                    px: 1.5
                                }
                            }}
                        >
                            <TableCell padding="checkbox" sx={{pl: 1.25, width: 42}}>
                                <Checkbox
                                    color="primary"
                                    size="small"
                                    indeterminate={selectedIds.length > 0 && selectedIds.length < data.length}
                                    checked={data.length > 0 && selectedIds.length === data.length}
                                    onChange={handleSelectAll}
                                />
                            </TableCell>

                            {columns.map((col) => (
                                <TableCell key={col.id} sx={getColumnSx(col)}>
                                    {col.label}
                                </TableCell>
                            ))}

                            <TableCell align="right" sx={{pr: 1.5, width: 76, minWidth: 76}}>
                                Actions
                            </TableCell>
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
                                    sx={{
                                        '&:last-child td, &:last-child th': {border: 0},
                                        '& .MuiTableCell-root': {
                                            borderBottom: '1px solid rgba(23, 32, 51, 0.07)',
                                            py: 0.85,
                                            px: 1.5,
                                            fontSize: '0.82rem',
                                            lineHeight: 1.35
                                        },
                                        '&:nth-of-type(even)': {
                                            backgroundColor: 'rgba(248, 251, 253, 0.45)'
                                        },
                                        '&:hover': {
                                            backgroundColor: 'rgba(15, 143, 206, 0.045)'
                                        },
                                        '&.Mui-selected': {
                                            backgroundColor: 'rgba(15, 143, 206, 0.08)'
                                        },
                                        '&.Mui-selected:hover': {
                                            backgroundColor: 'rgba(15, 143, 206, 0.1)'
                                        },
                                        transition: 'background-color 0.16s ease, box-shadow 0.16s ease'
                                    }}
                                >
                                    <TableCell padding="checkbox" sx={{pl: 1.25, width: 42}}>
                                        <Checkbox
                                            color="primary"
                                            size="small"
                                            checked={isItemSelected}
                                            onChange={(e) => handleSelectOne(e, item.id)}
                                        />
                                    </TableCell>

                                    {columns.map((col) => (
                                        <TableCell
                                            key={col.id}
                                            sx={{
                                                ...getColumnSx(col),
                                                color: 'text.primary',
                                                fontWeight: col.id === 'id' ? 700 : 500,
                                                whiteSpace: 'nowrap'
                                            }}
                                        >
                                            {col.render ? col.render(item) : col.id === 'role' ? (
                                                <Chip
                                                    label={formatRoleLabel(item[col.id])}
                                                    size="small"
                                                    variant="outlined"
                                                    sx={{
                                                        height: 24,
                                                        borderRadius: 1.25,
                                                        color: 'text.secondary',
                                                        borderColor: 'rgba(23, 32, 51, 0.12)',
                                                        bgcolor: 'rgba(248, 251, 253, 0.9)',
                                                        fontSize: '0.7rem',
                                                        fontWeight: 700,
                                                        textTransform: 'capitalize',
                                                        '& .MuiChip-label': {
                                                            px: 1
                                                        }
                                                    }}
                                                />
                                            ) : item[col.id]}
                                        </TableCell>
                                    ))}

                                    <TableCell align="right" sx={{pr: 1.5, width: 76}}>
                                        <IconButton
                                            size="small"
                                            onClick={(e) => handleMenuOpen(e, item)}
                                            sx={{
                                                width: 28,
                                                height: 28,
                                                border: '1px solid',
                                                borderColor: 'rgba(23, 32, 51, 0.1)',
                                                borderRadius: 1.5,
                                                color: 'text.secondary',
                                                bgcolor: 'rgba(255, 255, 255, 0.9)',
                                                '&:hover': {
                                                    borderColor: 'rgba(15, 143, 206, 0.28)',
                                                    color: 'primary.main',
                                                    bgcolor: 'rgba(15, 143, 206, 0.055)'
                                                }
                                            }}
                                        >
                                            <MoreVertIcon fontSize="small"/>
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
                transformOrigin={{horizontal: 'right', vertical: 'top'}}
                anchorOrigin={{horizontal: 'right', vertical: 'bottom'}}
                PaperProps={{
                    sx: {
                        mt: 1,
                        minWidth: 156,
                        borderRadius: 2,
                        border: '1px solid',
                        borderColor: 'divider',
                        boxShadow: '0 14px 34px rgba(23, 32, 51, 0.14)'
                    }
                }}
            >
                {onInfo && (
                    <MenuItem onClick={() => handleAction(onInfo)} sx={{gap: 1, minHeight: 38, fontSize: '0.86rem'}}>
                        <InfoIcon fontSize="small" sx={{mr: 1, color: 'text.secondary'}}/> Info
                    </MenuItem>
                )}
                {onEdit && (
                    <RequireRole allowedRoles={["ADMINISTRATOR", "MANAGER"]}>
                        <MenuItem onClick={() => handleAction(onEdit)} sx={{gap: 1, minHeight: 38, fontSize: '0.86rem'}}>
                            <EditIcon fontSize="small" sx={{mr: 1, color: 'warning.main'}}/> Edit
                        </MenuItem>
                    </RequireRole>

                )}
                {onDelete && (
                    <RequireRole allowedRoles={["ADMINISTRATOR"]}>
                        <MenuItem onClick={() => handleAction(onDelete)} sx={{color: 'error.main', gap: 1, minHeight: 38, fontSize: '0.86rem'}}>
                            <DeleteIcon fontSize="small" sx={{mr: 1, color: 'error.main'}}/> Delete
                        </MenuItem>
                    </RequireRole>
                )}
            </Menu>
        </Paper>
    );
};

export default AdminTable;
