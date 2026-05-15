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
                borderRadius: 3,
                border: '1px solid rgba(15, 143, 206, 0.22)',
                background: 'linear-gradient(180deg, rgba(255,255,255,0.98), rgba(248,251,253,0.95))',
                boxShadow: '0 20px 54px rgba(23, 32, 51, 0.10)',
                position: 'relative',
                '&::before': {
                    content: '""',
                    display: 'block',
                    height: 6,
                    background: 'linear-gradient(90deg, #0f8fce, #38bdf8)'
                }
            }}
        >
            <Box
                sx={{
                    display: 'flex',
                    justifyContent: 'space-between',
                    alignItems: 'center',
                    gap: 2,
                    px: {xs: 2, md: 2.75},
                    py: 2.25,
                    borderBottom: '1px solid rgba(15, 143, 206, 0.18)',
                    background: 'linear-gradient(135deg, rgba(15, 143, 206, 0.14), rgba(219, 242, 250, 0.88))'
                }}
            >
                <Stack direction="row" spacing={1.25} alignItems="center">
                    <Box
                        sx={{
                            width: 38,
                            height: 38,
                            borderRadius: 2,
                            display: 'grid',
                            placeItems: 'center',
                            color: 'primary.main',
                            bgcolor: 'rgba(255, 255, 255, 0.82)',
                            border: '1px solid rgba(15, 143, 206, 0.22)',
                            boxShadow: '0 8px 18px rgba(15, 143, 206, 0.12)'
                        }}
                    >
                        <TableRowsOutlinedIcon fontSize="small"/>
                    </Box>

                    <Box>
                        <Typography variant="subtitle1" fontWeight={800}>
                            Records
                        </Typography>
                        <Typography variant="body2" color="text.secondary">
                            Review and manage matching rows
                        </Typography>
                    </Box>
                </Stack>

                <Chip
                    size="small"
                    color="primary"
                    variant="filled"
                    label={`${data.length} shown`}
                />
            </Box>

            {selectedIds.length > 0 && (
                <Toolbar
                    sx={{
                        backgroundColor: (theme) => alpha(theme.palette.primary.main, 0.12),
                        display: 'flex',
                        justifyContent: 'space-between',
                        gap: 2,
                        borderBottom: '1px solid',
                        borderColor: 'divider',
                        px: {xs: 2, md: 2.5}
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
                    borderTop: '1px solid rgba(23, 32, 51, 0.08)'
                }}
            >
                <Table size="medium">
                    <TableHead>
                        <TableRow
                            sx={{
                                '& .MuiTableCell-head': {
                                    position: 'sticky',
                                    top: 0,
                                    zIndex: 1,
                                    bgcolor: '#e7f4fb',
                                    color: '#172033',
                                    fontSize: '0.74rem',
                                    fontWeight: 800,
                                    letterSpacing: 0,
                                    textTransform: 'uppercase',
                                    borderBottom: '2px solid rgba(15, 143, 206, 0.24)',
                                    borderTop: '1px solid rgba(255, 255, 255, 0.9)',
                                    py: 1.65
                                },
                                '& .MuiTableCell-head:first-of-type': {
                                    boxShadow: 'inset 4px 0 0 #0f8fce'
                                }
                            }}
                        >
                            <TableCell padding="checkbox" sx={{pl: 1.5}}>
                                <Checkbox
                                    color="primary"
                                    indeterminate={selectedIds.length > 0 && selectedIds.length < data.length}
                                    checked={data.length > 0 && selectedIds.length === data.length}
                                    onChange={handleSelectAll}
                                />
                            </TableCell>

                            {columns.map((col) => (
                                <TableCell key={col.id}>
                                    {col.label}
                                </TableCell>
                            ))}

                            <TableCell align="right" sx={{pr: 2}}>
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
                                            py: 1.75
                                        },
                                        '&:nth-of-type(even)': {
                                            backgroundColor: 'rgba(248, 251, 253, 0.58)'
                                        },
                                        '&:hover': {
                                            backgroundColor: 'rgba(15, 143, 206, 0.07)'
                                        },
                                        '&.Mui-selected': {
                                            backgroundColor: 'rgba(15, 143, 206, 0.12)'
                                        },
                                        '&.Mui-selected:hover': {
                                            backgroundColor: 'rgba(15, 143, 206, 0.16)'
                                        },
                                        transition: 'background-color 0.16s ease, box-shadow 0.16s ease'
                                    }}
                                >
                                    <TableCell padding="checkbox" sx={{pl: 1.5}}>
                                        <Checkbox
                                            color="primary"
                                            checked={isItemSelected}
                                            onChange={(e) => handleSelectOne(e, item.id)}
                                        />
                                    </TableCell>

                                    {columns.map((col) => (
                                        <TableCell
                                            key={col.id}
                                            sx={{
                                                color: 'text.primary',
                                                fontWeight: col.id === 'id' ? 700 : 500,
                                                whiteSpace: 'nowrap'
                                            }}
                                        >
                                            {col.render ? col.render(item) : item[col.id]}
                                        </TableCell>
                                    ))}

                                    <TableCell align="right" sx={{pr: 2}}>
                                        <IconButton
                                            size="small"
                                            onClick={(e) => handleMenuOpen(e, item)}
                                            sx={{
                                                border: '1px solid',
                                                borderColor: 'divider',
                                                borderRadius: 2,
                                                bgcolor: 'background.paper',
                                                boxShadow: '0 6px 16px rgba(23, 32, 51, 0.06)',
                                                '&:hover': {
                                                    borderColor: 'primary.light',
                                                    color: 'primary.main',
                                                    bgcolor: 'rgba(15, 143, 206, 0.06)'
                                                }
                                            }}
                                        >
                                            <MoreVertIcon/>
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
                        minWidth: 168,
                        borderRadius: 2,
                        border: '1px solid',
                        borderColor: 'divider',
                        boxShadow: '0 18px 44px rgba(23, 32, 51, 0.16)'
                    }
                }}
            >
                {onInfo && (
                    <MenuItem onClick={() => handleAction(onInfo)} sx={{gap: 1}}>
                        <InfoIcon fontSize="small" sx={{mr: 1, color: 'text.secondary'}}/> Info
                    </MenuItem>
                )}
                {onEdit && (
                    <RequireRole allowedRoles={["ADMINISTRATOR", "MANAGER"]}>
                        <MenuItem onClick={() => handleAction(onEdit)} sx={{gap: 1}}>
                            <EditIcon fontSize="small" sx={{mr: 1, color: 'warning.main'}}/> Edit
                        </MenuItem>
                    </RequireRole>

                )}
                {onDelete && (
                    <RequireRole allowedRoles={["ADMINISTRATOR"]}>
                        <MenuItem onClick={() => handleAction(onDelete)} sx={{color: 'error.main', gap: 1}}>
                            <DeleteIcon fontSize="small" sx={{mr: 1, color: 'error.main'}}/> Delete
                        </MenuItem>
                    </RequireRole>
                )}
            </Menu>
        </Paper>
    );
};

export default AdminTable;
