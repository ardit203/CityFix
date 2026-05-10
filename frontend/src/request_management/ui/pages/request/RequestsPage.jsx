import React, {useState} from 'react';
import {
    Box,
    CircularProgress,
    FormControl,
    FormHelperText,
    InputLabel,
    MenuItem,
    Select,
    TextField
} from "@mui/material";
import {useNavigate} from "react-router";
import useFilters from "../../../../common/hooks/useFilters.js";
import FilterBar from "../../../../common/ui/components/FilterBar.jsx";
import LoadingBar from "../../../../common/ui/components/LoadingBar.jsx";
import AdminTable from "../../../../common/ui/components/AdminTable.jsx";
import PaginatedDataView from "../../../../common/ui/components/PaginatedDataView.jsx";
import SortControls from "../../../../common/ui/components/SortControls.jsx";
import useRequests from "../../../hooks/request/useRequests.js";
import useMunicipalities from "../../../../administration/hooks/municipality/useMunicipalities.js";
import useRequestActions from "../../../hooks/request/useRequestActions.js";
import {emptyRequestFilter} from "../../../dtos/filterDto.js";
import {requestColumns, requestSortOptions} from "../../component/request/RequestConfig.jsx";
import RequestGrid from "../../component/request/RequestGrid.jsx";


const RequestsPage = () => {
    const navigate = useNavigate();
    const {requests, loading, pagination, fetchRequestsPaged} = useRequests();
    const {municipalities, loading: loadingMunicipalities} = useMunicipalities({paged: false});
    const {deleteRequest, deleteRequestsBulk} = useRequestActions();
    const [viewMode, setViewMode] = useState('table');

    console.log(requests)

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
        <Box>
            <FilterBar
                onSearch={handleSearch}
                onClear={handleClearFilters}
                onAdd={() => navigate("/requests/create")}
                addLabel="Add Request"
                viewMode={viewMode}
                onViewChange={handleViewChange}
            >
                <TextField label="ID" name="id" value={filters.id} onChange={handleFilterChange} size="small"/>
                <TextField label="Search" name="text" value={filters.text} onChange={handleFilterChange} size="small"/>
                {/*<FormControl size="small" sx={{minWidth: 140}}>*/}
                {/*    <InputLabel>Department</InputLabel>*/}
                {/*    <Select*/}
                {/*        name="departmentId"*/}
                {/*        value={filters.departmentId}*/}
                {/*        label="Department"*/}
                {/*        onChange={handleFilterChange}*/}
                {/*        disabled={loadingMunicipalities}*/}
                {/*    >*/}
                {/*        /!* 1. Put the default empty option OUTSIDE the conditional logic *!/*/}
                {/*        <MenuItem value="">*/}
                {/*            <em>All Departments</em>*/}
                {/*        </MenuItem>*/}
                {/*        /!* 2. Then do your conditional mapping without Fragments *!/*/}
                {/*        {loadingMunicipalities ? (*/}
                {/*            <MenuItem disabled value="loading">*/}
                {/*                <CircularProgress size={20} sx={{mr: 2}}/> Loading...*/}
                {/*            </MenuItem>*/}
                {/*        ) : requests.length === 0 ? (*/}
                {/*            <MenuItem disabled value="empty">*/}
                {/*                No municipalities available*/}
                {/*            </MenuItem>*/}
                {/*        ) : (*/}
                {/*            municipalities.map((mun) => (*/}
                {/*                <MenuItem key={mun.id} value={mun.id}>*/}
                {/*                    {mun.name}*/}
                {/*                </MenuItem>*/}
                {/*            ))*/}
                {/*        )}*/}
                {/*    </Select>*/}
                {/*</FormControl>*/}
                <SortControls
                    sortByValue={filters.sortBy}
                    sortDirValue={filters.sortDir}
                    onSortChange={handleSortChange}
                    options={requestSortOptions}
                />
            </FilterBar>

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
    );


};

export default RequestsPage;
