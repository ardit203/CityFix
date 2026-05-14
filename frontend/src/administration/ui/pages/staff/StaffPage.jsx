import React, {useRef, useState} from 'react';
import {
    Box,
    Button,
    CircularProgress,
    Dialog,
    DialogActions,
    DialogContent,
    DialogTitle,
    FormControl,
    InputLabel,
    MenuItem,
    Select,
    Stack,
    Typography,
    TextField
} from "@mui/material";
import InfoOutlinedIcon from "@mui/icons-material/InfoOutlined";
import UploadFileIcon from "@mui/icons-material/UploadFile";
import {useNavigate} from "react-router";
import useFilters from "../../../../common/hooks/useFilters.js";
import FilterBar from "../../../../common/ui/components/FilterBar.jsx";
import LoadingBar from "../../../../common/ui/components/LoadingBar.jsx";
import AdminTable from "../../../../common/ui/components/AdminTable.jsx";
import PaginatedDataView from "../../../../common/ui/components/PaginatedDataView.jsx";
import PageHeader from "../../../../common/ui/components/PageHeader.jsx";
import SortControls from "../../../../common/ui/components/SortControls.jsx";
import {
    staffColumns,
    staffSortOptions
} from "../../components/staff/StaffConfig.jsx";
import { emptyStaffFilter } from "../../../dtos/filterDto.js";

import useStaff from "../../../hooks/staff/useStaff.js";
import useStaffActions from "../../../hooks/staff/useStaffActions.js";
import StaffGrid from "../../components/staff/StaffGrid.jsx";
import useDepartments from "../../../hooks/department/useDepartments.js";
import useMunicipalities from "../../../hooks/municipality/useMunicipalities.js";

const StaffPage = () => {
    const navigate = useNavigate();
    const {staff, loading, pagination, fetchStaffPaged} = useStaff();
    const { departments, loading: loadingDepartments } = useDepartments({ paged: false });
    const { municipalities, loading: loadingMunicipalities } = useMunicipalities({ paged: false });
    const {deleteStaff, deleteStaffBulk, importStaffExcel, isExecuting} = useStaffActions();
    const [viewMode, setViewMode] = useState('table');
    const [rulesOpen, setRulesOpen] = useState(false);
    const fileInputRef = useRef(null);

    const {
        filters,
        handleFilterChange,
        handleSearch,
        handleClearFilters,
        handlePageChange,
        handleSizeChange,
        handleSortChange
    } = useFilters(emptyStaffFilter, fetchStaffPaged);

    const handleViewChange = (event, nextView) => {
        if (nextView !== null) {
            setViewMode(nextView);
        }
    };

    const handleImportClick = () => {
        fileInputRef.current?.click();
    };

    const handleImportFileChange = async (event) => {
        const file = event.target.files?.[0];
        event.target.value = "";

        if (!file) return;

        await importStaffExcel(file, () => fetchStaffPaged(filters));
    };

    return (
        <Box className="filtered-page">
            <Box className="filtered-page-header">
                <PageHeader
                    title="Staff"
                    subtitle="Manage staff assignments to departments and municipalities."
                />
            </Box>

            <FilterBar
                onSearch={handleSearch}
                onClear={handleClearFilters}
                onAdd={() => navigate("/staff/create")}
                addLabel="Add Staff"
                viewMode={viewMode}
                onViewChange={handleViewChange}
                actions={(
                    <>
                        <Button
                            variant="outlined"
                            startIcon={<InfoOutlinedIcon />}
                            onClick={() => setRulesOpen(true)}
                        >
                            Import Rules
                        </Button>

                        <Button
                            variant="outlined"
                            startIcon={<UploadFileIcon />}
                            onClick={handleImportClick}
                            disabled={isExecuting}
                        >
                            Import Excel
                        </Button>
                        <input
                            ref={fileInputRef}
                            type="file"
                            hidden
                            accept=".xlsx,.xls,application/vnd.openxmlformats-officedocument.spreadsheetml.sheet,application/vnd.ms-excel"
                            onChange={handleImportFileChange}
                        />
                    </>
                )}
            >
                <TextField label="ID" name="id" value={filters.id} onChange={handleFilterChange} size="small"/>
                <TextField label="Search" name="text" value={filters.text} onChange={handleFilterChange} size="small"/>
                <FormControl size="small" sx={{ minWidth: 140 }}>
                    <InputLabel>Department</InputLabel>
                    <Select
                        name="departmentId"
                        value={filters.departmentId}
                        label="Department"
                        onChange={handleFilterChange}
                        disabled={loadingDepartments}
                    >
                        {/* 1. Put the default empty option OUTSIDE the conditional logic */}
                        <MenuItem value="">
                            <em>All Departments</em>
                        </MenuItem>
                        {/* 2. Then do your conditional mapping without Fragments */}
                        {loadingDepartments ? (
                            <MenuItem disabled value="loading">
                                <CircularProgress size={20} sx={{ mr: 2 }} /> Loading...
                            </MenuItem>
                        ) : departments.length === 0 ? (
                            <MenuItem disabled value="empty">
                                No departments available
                            </MenuItem>
                        ) : (
                            departments.map((dep) => (
                                <MenuItem key={dep.id} value={dep.id}>
                                    {dep.name}
                                </MenuItem>
                            ))
                        )}
                    </Select>
                </FormControl>
                <FormControl size="small" sx={{ minWidth: 140 }}>
                    <InputLabel>Municipalities</InputLabel>
                    <Select
                        name="municipalityId"
                        value={filters.municipalityId}
                        label="Municipality"
                        onChange={handleFilterChange}
                        disabled={loadingMunicipalities}
                    >
                        {/* 1. Put the default empty option OUTSIDE the conditional logic */}
                        <MenuItem value="">
                            <em>All Municipalities</em>
                        </MenuItem>
                        {/* 2. Then do your conditional mapping without Fragments */}
                        {loadingMunicipalities ? (
                            <MenuItem disabled value="loading">
                                <CircularProgress size={20} sx={{ mr: 2 }} /> Loading...
                            </MenuItem>
                        ) : municipalities.length === 0 ? (
                            <MenuItem disabled value="empty">
                                No municipalities available
                            </MenuItem>
                        ) : (
                            municipalities.map((mun) => (
                                <MenuItem key={mun.id} value={mun.id}>
                                    {mun.code}
                                </MenuItem>
                            ))
                        )}
                    </Select>
                </FormControl>
                <TextField label="Username" name="username" value={filters.username} onChange={handleFilterChange} size="small"/>
                <TextField label="Mun. Code" name="municipalityCode" value={filters.municipalityCode} onChange={handleFilterChange} size="small"/>
                <TextField label="Mun. Name" name="municipalityName" value={filters.municipalityName} onChange={handleFilterChange} size="small"/>
                <SortControls
                    sortByValue={filters.sortBy}
                    sortDirValue={filters.sortDir}
                    onSortChange={handleSortChange}
                    options={staffSortOptions}
                />

            </FilterBar>

            <Dialog open={rulesOpen} onClose={() => setRulesOpen(false)} fullWidth maxWidth="sm">
                <DialogTitle>Staff Import Rules</DialogTitle>
                <DialogContent>
                    <Stack spacing={2}>
                        <Box>
                            <Typography variant="subtitle2" fontWeight={700}>
                                Required headers
                            </Typography>
                            <Typography variant="body2" color="text.secondary">
                                The first row must contain: userId, departmentId, municipalityId
                            </Typography>
                        </Box>

                        <Box>
                            <Typography variant="subtitle2" fontWeight={700}>
                                Validation
                            </Typography>
                            <Typography variant="body2" color="text.secondary">
                                Each value must be a whole number. Users, departments, and municipalities must already exist.
                            </Typography>
                            <Typography variant="body2" color="text.secondary">
                                A user cannot appear twice in the same file and cannot already be assigned as staff.
                            </Typography>
                            <Typography variant="body2" color="text.secondary">
                                Citizen users cannot be imported as staff.
                            </Typography>
                        </Box>

                        <Box>
                            <Typography variant="subtitle2" fontWeight={700}>
                                Example
                            </Typography>
                            <Typography component="pre" variant="body2" sx={{m: 0, p: 1.5, bgcolor: "background.default", borderRadius: 1, overflowX: "auto"}}>
{`userId | departmentId | municipalityId
1      | 2            | 1
5      | 3            | 1`}
                            </Typography>
                        </Box>
                    </Stack>
                </DialogContent>
                <DialogActions>
                    <Button onClick={() => setRulesOpen(false)}>Close</Button>
                </DialogActions>
            </Dialog>

            <Box className="filtered-page-content">
                <LoadingBar loading={loading}/>

                <PaginatedDataView
                    loading={loading}
                    data={staff}
                    pagination={pagination}
                    onPageChange={handlePageChange}
                    onSizeChange={handleSizeChange}
                    emptyMessage="No staff found."
                    emptySubMessage="Try clearing your filters or creating a new staff."
                >
                    {viewMode === 'table' ? (
                        <AdminTable
                            data={staff}
                            columns={staffColumns}
                            onInfo={(staff) => navigate(`/staff/${staff.id}`)}
                            onEdit={(staff) => navigate(`/staff/${staff.id}/edit`)}
                            onDelete={async (staff) => {
                                await deleteStaff(staff.id, () => fetchStaffPaged(filters));
                            }}
                            onBulkDelete={async (selectedIds) => {
                                await deleteStaffBulk(selectedIds, () => fetchStaffPaged(filters));
                            }}
                        />
                    ) : (
                        <StaffGrid
                            staff={staff}
                            onDelete={() => fetchStaffPaged(filters)}
                        />
                    )}
                </PaginatedDataView>
            </Box>
        </Box>
    );


};

export default StaffPage;
