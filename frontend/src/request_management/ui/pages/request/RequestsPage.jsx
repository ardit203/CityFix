import React, {useState} from 'react';
import {
    Box, CircularProgress, FormControl, InputLabel, MenuItem, Select,
    TextField
} from "@mui/material";
import {useNavigate} from "react-router";
import useFilters from "../../../../common/hooks/useFilters.js";
import FilterBar from "../../../../common/ui/components/FilterBar.jsx";
import LoadingBar from "../../../../common/ui/components/LoadingBar.jsx";
import AdminTable from "../../../../common/ui/components/AdminTable.jsx";
import PaginatedDataView from "../../../../common/ui/components/PaginatedDataView.jsx";
import PageHeader from "../../../../common/ui/components/PageHeader.jsx";
import SortControls from "../../../../common/ui/components/SortControls.jsx";
import useRequests from "../../../hooks/request/useRequests.js";
import useRequestActions from "../../../hooks/request/useRequestActions.js";
import {emptyRequestFilter} from "../../../dtos/filterDto.js";
import {requestColumns, requestSortOptions} from "../../component/request/RequestConfig.jsx";
import RequestGrid from "../../component/request/RequestGrid.jsx";
import RequireRole from "../../../../auth_and_access/ui/components/auth/RequireRole.jsx";
import useDepartments from "../../../../administration/hooks/department/useDepartments.js";
import useCategories from "../../../../administration/hooks/category/useCategories.js";
import useMunicipalities from "../../../../administration/hooks/municipality/useMunicipalities.js";
import useAuth from "../../../../auth_and_access/hooks/auth/useAuth.js";


const requestStatusOptions = [
    "SUBMITTED",
    "IN_REVIEW",
    "ASSIGNED",
    "IN_PROGRESS",
    "RESOLVED",
    "REJECTED",
    "CANCELED"
];

const routingStatusOptions = [
    "PENDING_REVIEW",
    "CONFIRMED",
    "REJECTED"
];

const priorityOptions = [
    "LOW",
    "MEDIUM",
    "HIGH"
];

const formatLabel = (value) => {
    return value
        .replaceAll("_", " ")
        .toLowerCase()
        .replace(/\b\w/g, char => char.toUpperCase());
};


const RequestsPage = () => {
    const navigate = useNavigate();
    const {requests, loading, pagination, fetchRequestsPaged} = useRequests();
    const {user} = useAuth();
    const canLoadFilters =
        user?.roles?.some(role =>
            ["ROLE_ADMINISTRATOR", "ROLE_CITIZEN"].includes(role)
        ) === true;

    const {departments, loading: loadingDepartments} = useDepartments({
        paged: false,
        enabled:canLoadFilters});
    const {categories, loading: loadingCategories} = useCategories({
        paged: false,
        enabled:canLoadFilters});
    const {municipalities, loading: loadingMunicipalities} = useMunicipalities({
        paged: false,
        enabled:canLoadFilters});
    const {deleteRequest, deleteRequestsBulk} = useRequestActions();
    const [viewMode, setViewMode] = useState('table');

    const {
        filters,
        handleFilterChange,
        handleSearch,
        handleClearFilters,
        handlePageChange,
        handleSizeChange,
        handleSortChange
    } = useFilters(emptyRequestFilter, fetchRequestsPaged);

    const handleViewChange = (event, nextView) => {
        if (nextView !== null) {
            setViewMode(nextView);
        }
    };

    return (
        <Box className="filtered-page">
            <Box className="filtered-page-header">
                <PageHeader
                    title="Requests"
                    subtitle="Browse, filter, and manage citizen service requests."
                />
            </Box>

            <FilterBar
                onSearch={handleSearch}
                onClear={handleClearFilters}
                onAdd={() => navigate("/requests/create")}
                addLabel="Add Request"
                viewMode={viewMode}
                onViewChange={handleViewChange}
            >
                <TextField label="ID" name="id" value={filters.id} onChange={handleFilterChange} size="small"/>
                <RequireRole allowedRoles={["ADMINISTRATOR", "MANAGER", "EMPLOYEE"]}>
                    <TextField label="Citizen ID" name="citizenId" value={filters.citizenId}
                               onChange={handleFilterChange} size="small"/>
                </RequireRole>
                <RequireRole allowedRoles={["ADMINISTRATOR", "CITIZEN"]}>
                    <FormControl size="small" sx={{minWidth: 140}}>
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
                                    <CircularProgress size={20} sx={{mr: 2}}/> Loading...
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
                </RequireRole>
                <RequireRole allowedRoles={["ADMINISTRATOR", "CITIZEN"]}>
                    <FormControl size="small" sx={{minWidth: 140}}>
                        <InputLabel>Municipality</InputLabel>
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
                                    <CircularProgress size={20} sx={{mr: 2}}/> Loading...
                                </MenuItem>
                            ) : municipalities.length === 0 ? (
                                <MenuItem disabled value="empty">
                                    No municipalities available
                                </MenuItem>
                            ) : (
                                municipalities.map((mun) => (
                                    <MenuItem key={mun.id} value={mun.id}>
                                        {mun.name}
                                    </MenuItem>
                                ))
                            )}
                        </Select>
                    </FormControl>
                </RequireRole>
                <RequireRole allowedRoles={["ADMINISTRATOR", "CITIZEN"]}>
                    <FormControl size="small" sx={{minWidth: 140}}>
                        <InputLabel>Category</InputLabel>
                        <Select
                            name="categoryId"
                            value={filters.categoryId}
                            label="Category"
                            onChange={handleFilterChange}
                            disabled={loadingCategories}
                        >
                            {/* 1. Put the default empty option OUTSIDE the conditional logic */}
                            <MenuItem value="">
                                <em>All Categories</em>
                            </MenuItem>
                            {/* 2. Then do your conditional mapping without Fragments */}
                            {loadingCategories ? (
                                <MenuItem disabled value="loading">
                                    <CircularProgress size={20} sx={{mr: 2}}/> Loading...
                                </MenuItem>
                            ) : categories.length === 0 ? (
                                <MenuItem disabled value="empty">
                                    No categories available
                                </MenuItem>
                            ) : (
                                categories.map((cat) => (
                                    <MenuItem key={cat.id} value={cat.id}>
                                        {cat.name}
                                    </MenuItem>
                                ))
                            )}
                        </Select>
                    </FormControl>
                </RequireRole>
                <RequireRole allowedRoles={["ADMINISTRATOR", "CITIZEN"]}>
                    <TextField label="Assigned Employee ID" name="assignedEmployeeUserId"
                               value={filters.assignedEmployeeUserId} onChange={handleFilterChange} size="small"/>
                </RequireRole>
                <TextField select label="Status" name="status" value={filters.status} onChange={handleFilterChange}
                           size="small" sx={{minWidth: 140}}>
                    <MenuItem value="">All statuses</MenuItem>

                    {requestStatusOptions.map(status => (
                        <MenuItem key={status} value={status}>
                            {formatLabel(status)}
                        </MenuItem>
                    ))}
                </TextField>
                <TextField select label="Routing status" name="routingStatus" value={filters.routingStatus}
                           onChange={handleFilterChange} size="small" sx={{minWidth: 160}}>
                    <MenuItem value="">All statuses</MenuItem>

                    {routingStatusOptions.map(status => (
                        <MenuItem key={status} value={status}>
                            {formatLabel(status)}
                        </MenuItem>
                    ))}
                </TextField>
                <TextField select label="Priority" name="priority" value={filters.priority}
                           onChange={handleFilterChange} size="small" sx={{minWidth: 140}}>
                    <MenuItem value="">All priorities</MenuItem>

                    {priorityOptions.map(priority => (
                        <MenuItem key={priority} value={priority}>
                            {formatLabel(priority)}
                        </MenuItem>
                    ))}
                </TextField>
                <TextField
                    label="Subimtted From"
                    name="submittedFrom"
                    type="datetime-local"
                    value={filters.submittedFrom}
                    onChange={handleFilterChange}
                    size="small"
                    sx={{minWidth: 130}}
                    slotProps={{
                        inputLabel: {shrink: true}
                    }}
                />
                <TextField
                    label="Subimtted To"
                    name="submittedTo"
                    type="datetime-local"
                    value={filters.submittedTo}
                    onChange={handleFilterChange}
                    size="small"
                    sx={{minWidth: 130}}
                    slotProps={{
                        inputLabel: {shrink: true}
                    }}
                />
                <TextField label="Search" name="text" value={filters.text} onChange={handleFilterChange} size="small"/>

                <SortControls
                    sortByValue={filters.sortBy}
                    sortDirValue={filters.sortDir}
                    onSortChange={handleSortChange}
                    options={requestSortOptions}
                />
            </FilterBar>

            <Box className="filtered-page-content">
                <LoadingBar loading={loading}/>

                <PaginatedDataView
                    loading={loading}
                    data={requests}
                    pagination={pagination}
                    onPageChange={handlePageChange}
                    onSizeChange={handleSizeChange}
                    emptyMessage="No requests found."
                    emptySubMessage="Try clearing your filters or creating a new request."
                >
                    {viewMode === 'table' ? (
                        <AdminTable
                            data={requests}
                            columns={requestColumns}
                            onInfo={(request) => navigate(`/requests/${request.id}`)}
                            onEdit={(request) => navigate(`/requests/${request.id}/edit`)}
                            onDelete={async (request) => {
                                await deleteRequest(request.id, () => fetchRequestsPaged(filters));
                            }}
                            onBulkDelete={async (selectedIds) => {
                                await deleteRequestsBulk(selectedIds, () => fetchRequestsPaged(filters));
                            }}
                        />
                    ) : (
                        <RequestGrid
                            requests={requests}
                            onDelete={() => fetchRequestsPaged(filters)}
                        />
                    )}
                </PaginatedDataView>
            </Box>
        </Box>
    );


};

export default RequestsPage;
