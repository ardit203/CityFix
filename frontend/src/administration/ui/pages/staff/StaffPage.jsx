import React, {useState} from 'react';
import {Box, CircularProgress, FormControl, InputLabel, MenuItem, Select, TextField} from "@mui/material";
import {useNavigate} from "react-router";
import useFilters from "../../../../common/hooks/useFilters.js";
import FilterBar from "../../../../common/ui/components/FilterBar.jsx";
import LoadingBar from "../../../../common/ui/components/LoadingBar.jsx";
import AdminTable from "../../../../common/ui/components/AdminTable.jsx";
import PaginatedDataView from "../../../../common/ui/components/PaginatedDataView.jsx";
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
    const {deleteStaff} = useStaffActions();
    const [viewMode, setViewMode] = useState('table');

    console.log(staff)

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

    return (
        <Box>
            <FilterBar
                onSearch={handleSearch}
                onClear={handleClearFilters}
                onAdd={() => navigate("/staff/create")}
                addLabel="Add Staff"
                viewMode={viewMode}
                onViewChange={handleViewChange}
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
                            console.log("Deleting multiple IDs:", selectedIds);
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
    );


};

export default StaffPage;
