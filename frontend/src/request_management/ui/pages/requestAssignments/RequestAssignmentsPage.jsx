import React from "react";
import {Box, CircularProgress, FormControl, InputLabel, MenuItem, Select, TextField} from "@mui/material";
import {useNavigate} from "react-router";

import useFilters from "../../../../common/hooks/useFilters.js";
import FilterBar from "../../../../common/ui/components/FilterBar.jsx";
import LoadingBar from "../../../../common/ui/components/LoadingBar.jsx";
import AdminTable from "../../../../common/ui/components/AdminTable.jsx";
import PaginatedDataView from "../../../../common/ui/components/PaginatedDataView.jsx";
import PageHeader from "../../../../common/ui/components/PageHeader.jsx";
import SortControls from "../../../../common/ui/components/SortControls.jsx";
import useDepartments from "../../../../administration/hooks/department/useDepartments.js";
import useMunicipalities from "../../../../administration/hooks/municipality/useMunicipalities.js";
import useRequestAssignments from "../../../hooks/requestAssignments/useRequestAssignments.js";
import {
    requestAssignmentColumns,
    requestAssignmentSortOptions
} from "../../component/requestAssignments/RequestAssignmentConfig.jsx";
import {emptyRequestAssignmentFilter} from "../../../dtos/requestAssignmentDto.js";

const requestStatuses = [
    "SUBMITTED",
    "IN_REVIEW",
    "ASSIGNED",
    "IN_PROGRESS",
    "RESOLVED",
    "REJECTED",
    "CANCELED"
];

const RequestAssignmentsPage = () => {
    const navigate = useNavigate();
    const {assignments, loading, pagination, fetchAssignmentsPaged} = useRequestAssignments({paged: true});
    const {departments, loading: loadingDepartments} = useDepartments({paged: false});
    const {municipalities, loading: loadingMunicipalities} = useMunicipalities({paged: false});

    const {
        filters,
        handleFilterChange,
        handleSearch,
        handleClearFilters,
        handlePageChange,
        handleSizeChange,
        handleSortChange
    } = useFilters(emptyRequestAssignmentFilter, fetchAssignmentsPaged);

    return (
        <Box className="filtered-page">
            <Box className="filtered-page-header">
                <PageHeader
                    title="Assignments"
                    subtitle="Review request ownership and staff workload."
                />
            </Box>

            <FilterBar
                onSearch={handleSearch}
                onClear={handleClearFilters}
            >
                <TextField label="Assignment ID" name="id" value={filters.id} onChange={handleFilterChange} size="small" />
                <TextField label="Request ID" name="requestId" value={filters.requestId} onChange={handleFilterChange} size="small" />
                <TextField label="Employee User ID" name="employeeId" value={filters.employeeId} onChange={handleFilterChange} size="small" />

                <FormControl size="small" sx={{minWidth: 150}}>
                    <InputLabel>Status</InputLabel>
                    <Select
                        name="requestStatus"
                        value={filters.requestStatus}
                        label="Status"
                        onChange={handleFilterChange}
                    >
                        <MenuItem value="">
                            <em>All Statuses</em>
                        </MenuItem>
                        {requestStatuses.map((status) => (
                            <MenuItem key={status} value={status}>
                                {status.replaceAll("_", " ")}
                            </MenuItem>
                        ))}
                    </Select>
                </FormControl>

                <FormControl size="small" sx={{minWidth: 160}}>
                    <InputLabel>Department</InputLabel>
                    <Select
                        name="departmentId"
                        value={filters.departmentId}
                        label="Department"
                        onChange={handleFilterChange}
                        disabled={loadingDepartments}
                    >
                        <MenuItem value="">
                            <em>All Departments</em>
                        </MenuItem>
                        {loadingDepartments ? (
                            <MenuItem disabled value="loading">
                                <CircularProgress size={20} sx={{mr: 2}} /> Loading...
                            </MenuItem>
                        ) : departments.map((department) => (
                            <MenuItem key={department.id} value={department.id}>
                                {department.name}
                            </MenuItem>
                        ))}
                    </Select>
                </FormControl>

                <FormControl size="small" sx={{minWidth: 160}}>
                    <InputLabel>Municipality</InputLabel>
                    <Select
                        name="municipalityId"
                        value={filters.municipalityId}
                        label="Municipality"
                        onChange={handleFilterChange}
                        disabled={loadingMunicipalities}
                    >
                        <MenuItem value="">
                            <em>All Municipalities</em>
                        </MenuItem>
                        {loadingMunicipalities ? (
                            <MenuItem disabled value="loading">
                                <CircularProgress size={20} sx={{mr: 2}} /> Loading...
                            </MenuItem>
                        ) : municipalities.map((municipality) => (
                            <MenuItem key={municipality.id} value={municipality.id}>
                                {municipality.code || municipality.name}
                            </MenuItem>
                        ))}
                    </Select>
                </FormControl>

                <SortControls
                    sortByValue={filters.sortBy}
                    sortDirValue={filters.sortDir}
                    onSortChange={handleSortChange}
                    options={requestAssignmentSortOptions}
                />
            </FilterBar>

            <Box className="filtered-page-content">
                <LoadingBar loading={loading} />

                <PaginatedDataView
                    loading={loading}
                    data={assignments}
                    pagination={pagination}
                    onPageChange={handlePageChange}
                    onSizeChange={handleSizeChange}
                    emptyMessage="No assignments found."
                    emptySubMessage="Try clearing filters or assigning a request."
                >
                    <AdminTable
                        data={assignments}
                        columns={requestAssignmentColumns}
                        onInfo={(assignment) => navigate(`/requests/${assignment.requestId}`)}
                    />
                </PaginatedDataView>
            </Box>
        </Box>
    );
};

export default RequestAssignmentsPage;
