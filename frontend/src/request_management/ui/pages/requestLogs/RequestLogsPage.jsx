import React, {useState} from 'react';
import {
    Box, CircularProgress, FormControl, InputLabel, MenuItem, Select,
    TextField
} from "@mui/material";
import {useNavigate, useParams} from "react-router";
import useFilters from "../../../../common/hooks/useFilters.js";
import FilterBar from "../../../../common/ui/components/FilterBar.jsx";
import LoadingBar from "../../../../common/ui/components/LoadingBar.jsx";
import AdminTable from "../../../../common/ui/components/AdminTable.jsx";
import PaginatedDataView from "../../../../common/ui/components/PaginatedDataView.jsx";
import PageHeader from "../../../../common/ui/components/PageHeader.jsx";
import SortControls from "../../../../common/ui/components/SortControls.jsx";
import {emptyRequestLogFilters} from "../../../dtos/filterDto.js";
import {requestSortOptions} from "../../component/request/RequestConfig.jsx";
import useRequestLogs from "../../../hooks/requestLogs/useRequestLogs.js";
import RequestLogGrid from "../../component/requestLogs/RequestLogGrid.jsx";
import {requestLogColumns} from "../../component/requestLogs/RequestConfig.jsx";

const actionOptions = [
    "REQUEST_CREATED",
    "STATUS_CHANGED",
    "PRIORITY_CHANGED",
    "ROUTING_CONFIRMED",
    "ROUTING_REJECTED",
    "ROUTING_REOPENED",
    "CATEGORY_CHANGED",
    "DEPARTMENT_CHANGED",
    "EMPLOYEE_ASSIGNED",
    "EMPLOYEE_REMOVED",
    "COMMENT_ADDED",
    "COMMENT_DELETED",
    "COMMENT_EDITED",
    "FILE_UPLOADED",
    "FILE_REMOVED",
    "REQUEST_RESOLVED",
    "REQUEST_REJECTED",
    "REQUEST_CANCELED",
    "AI_SUGGESTION_CREATED",
    "AI_SUGGESTION_APPROVED",
    "AI_SUGGESTION_REJECTED"
];

const formatLabel = (value) => {
    return value
        .replaceAll("_", " ")
        .toLowerCase()
        .replace(/\b\w/g, char => char.toUpperCase());
};

const RequestLogsPage = () => {
    const {requestId} = useParams();
    const navigate = useNavigate();
    const {requestLogs, loading, pagination, fetchRequestsLogsPaged} = useRequestLogs(requestId);
    const [viewMode, setViewMode] = useState('table');

    const {
        filters,
        handleFilterChange,
        handleSearch,
        handleClearFilters,
        handlePageChange,
        handleSizeChange,
        handleSortChange
    } = useFilters(emptyRequestLogFilters, fetchRequestsLogsPaged);

    const handleViewChange = (event, nextView) => {
        if (nextView !== null) {
            setViewMode(nextView);
        }
    };

    return (
        <Box className="filtered-page">
            <Box className="filtered-page-header">
                <PageHeader
                    title="Request Timeline"
                    subtitle={`Review changes and activity for request #${requestId}.`}
                />
            </Box>

            <FilterBar
                onSearch={handleSearch}
                onClear={handleClearFilters}
                viewMode={viewMode}
                onViewChange={handleViewChange}
            >
                <TextField label="ID" name="id" value={filters.id} onChange={handleFilterChange} size="small"/>
                <TextField select label="Action" name="action" value={filters.action} onChange={handleFilterChange} size="small" sx={{minWidth: 140}}>
                    <MenuItem value="">All statuses</MenuItem>

                    {actionOptions.map(action => (
                        <MenuItem key={action} value={action}>
                            {formatLabel(action)}
                        </MenuItem>
                    ))}
                </TextField>

                <TextField
                    label="Created From"
                    name="createdFrom"
                    type="datetime-local"
                    value={filters.createdFrom}
                    onChange={handleFilterChange}
                    size="small"
                    sx={{minWidth: 130}}
                    slotProps={{
                        inputLabel: { shrink: true }
                    }}
                />
                <TextField
                    label="Created To"
                    name="createdTo"
                    type="datetime-local"
                    value={filters.createdTo}
                    onChange={handleFilterChange}
                    size="small"
                    sx={{minWidth: 130}}
                    slotProps={{
                        inputLabel: { shrink: true }
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
                    data={requestLogs}
                    pagination={pagination}
                    onPageChange={handlePageChange}
                    onSizeChange={handleSizeChange}
                    emptyMessage="No requests found."
                    emptySubMessage="Try clearing your filters or creating a new request."
                >
                    {viewMode === 'table' ? (
                        <AdminTable
                            data={requestLogs}
                            columns={requestLogColumns}
                            onInfo={(requestLog) => navigate(`/requests/${requestId}/logs/${requestLog.id}`)}
                        />
                    ) : (
                        <RequestLogGrid
                            requestId={requestId}
                            requestLogs={requestLogs}
                        />
                    )}
                </PaginatedDataView>
            </Box>
        </Box>
    );


};

export default RequestLogsPage;
