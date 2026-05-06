import React, {useState} from 'react';
import {Box, TextField} from "@mui/material";
import {useNavigate} from "react-router";
import useDepartments from "../../../hooks/useDepartments.js";
import DepartmentGrid from "../../components/department/DepartmentGrid.jsx";
import useFilters from "../../../../common/hooks/useFilters.js";
import FilterBar from "../../../../common/ui/components/FilterBar.jsx";
import LoadingBar from "../../../../common/ui/components/LoadingBar.jsx";
import AdminTable from "../../../../common/ui/components/AdminTable.jsx";
import useDepartmentActions from "../../../hooks/useDepartmentActions.js";
import PaginatedDataView from "../../../../common/ui/components/PaginatedDataView.jsx";
import SortControls from "../../../../common/ui/components/SortControls.jsx";
import {
    initialDepartmentFilters,
    departmentColumns,
    departmentSortOptions
} from "../../components/department/DepartmentConfig.jsx";

const DepartmentsPage = () => {
    const navigate = useNavigate();
    const {departments, loading, pagination, fetchDepartmentsPaged} = useDepartments();
    const {deleteDepartment} = useDepartmentActions();

    // The state to track Table vs Grid
    const [viewMode, setViewMode] = useState('table');

    const {
        filters,
        handleFilterChange,
        handleSearch,
        handleClearFilters,
        handlePageChange,
        handleSizeChange,
        handleSortChange
    } = useFilters(initialDepartmentFilters, fetchDepartmentsPaged);

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
                onAdd={() => navigate("/departments/create")}
                addLabel="Add Department"
                viewMode={viewMode}
                onViewChange={handleViewChange}
            >
                <TextField label="ID" name="id" value={filters.id} onChange={handleFilterChange} size="small"/>
                <TextField label="Search" name="text" value={filters.text} onChange={handleFilterChange} size="small"/>
                <SortControls
                    sortByValue={filters.sortBy}
                    // sortDirValue={filters.sortDir}
                    onSortChange={handleSortChange}
                    options={departmentSortOptions}
                />
            </FilterBar>

            <LoadingBar loading={loading}/>

            {/* Look at how incredibly clean this is! */}
            <PaginatedDataView
                loading={loading}
                data={departments}
                pagination={pagination}
                onPageChange={handlePageChange}
                onSizeChange={handleSizeChange}
                emptyMessage="No departments found."
                emptySubMessage="Try clearing your filters or creating a new department."
            >
                {/* We just drop the Table or Grid right here */}
                {viewMode === 'table' ? (
                    <AdminTable
                        data={departments}
                        columns={departmentColumns}
                        onInfo={(department) => navigate(`/departments/${department.id}`)}
                        onEdit={(department) => navigate(`/departments/${department.id}/edit`)}
                        onDelete={async (department) => {
                            await deleteDepartment(department.id, () => fetchDepartmentsPaged(filters));
                        }}
                        onBulkDelete={async (selectedIds) => {
                            console.log("Deleting multiple IDs:", selectedIds);
                        }}
                    />
                ) : (
                    <DepartmentGrid
                        departments={departments}
                        onDelete={() => fetchDepartmentsPaged(filters)}
                    />
                )}
            </PaginatedDataView>
        </Box>
    );


};

export default DepartmentsPage;
