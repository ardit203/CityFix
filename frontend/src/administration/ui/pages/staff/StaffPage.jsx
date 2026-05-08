import React, {useState} from 'react';
import {Box, TextField} from "@mui/material";
import {useNavigate} from "react-router";
import useFilters from "../../../../common/hooks/useFilters.js";
import FilterBar from "../../../../common/ui/components/FilterBar.jsx";
import LoadingBar from "../../../../common/ui/components/LoadingBar.jsx";
import AdminTable from "../../../../common/ui/components/AdminTable.jsx";
import PaginatedDataView from "../../../../common/ui/components/PaginatedDataView.jsx";
import SortControls from "../../../../common/ui/components/SortControls.jsx";
import {
    initialStaffFilters,
    staffColumns,
    staffSortOptions
} from "../../components/staff/StaffConfig.jsx";

import useStaff from "../../../hooks/staff/useStaff.js";
import useStaffActions from "../../../hooks/staff/useStaffActions.js";
import StaffGrid from "../../components/staff/StaffGrid.jsx";

const StaffPage = () => {
    const navigate = useNavigate();
    const {staff, loading, pagination, fetchStaffPaged} = useStaff();
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
    } = useFilters(initialStaffFilters, fetchStaffPaged);

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
