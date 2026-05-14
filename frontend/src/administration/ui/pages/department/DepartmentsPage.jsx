import React, {useState} from 'react';
import {Box, TextField} from "@mui/material";
import {useNavigate} from "react-router";
import useDepartments from "../../../hooks/department/useDepartments.js";
import DepartmentGrid from "../../components/department/DepartmentGrid.jsx";
import useFilters from "../../../../common/hooks/useFilters.js";
import FilterBar from "../../../../common/ui/components/FilterBar.jsx";
import LoadingBar from "../../../../common/ui/components/LoadingBar.jsx";
import AdminTable from "../../../../common/ui/components/AdminTable.jsx";
import useDepartmentActions from "../../../hooks/department/useDepartmentActions.js";
import PaginatedDataView from "../../../../common/ui/components/PaginatedDataView.jsx";
import PageHeader from "../../../../common/ui/components/PageHeader.jsx";
import SortControls from "../../../../common/ui/components/SortControls.jsx";
import {
    departmentColumns,
    departmentSortOptions
} from "../../components/department/DepartmentConfig.jsx";
import { emptyDepartmentFilter } from "../../../dtos/filterDto.js";

const DepartmentsPage = () => {
    const navigate = useNavigate();
    const {departments, loading, pagination, fetchDepartmentsPaged} = useDepartments();
    const {deleteDepartment, deleteDepartmentsBulk} = useDepartmentActions();
    const [viewMode, setViewMode] = useState('table');

    const {
        filters,
        handleFilterChange,
        handleSearch,
        handleClearFilters,
        handlePageChange,
        handleSizeChange,
        handleSortChange
    } = useFilters(emptyDepartmentFilter, fetchDepartmentsPaged);

    const handleViewChange = (event, nextView) => {
        if (nextView !== null) {
            setViewMode(nextView);
        }
    };
    console.log(departments)

    return (
        <Box>
            <PageHeader
                title="Departments"
                subtitle="Manage municipal departments used for routing requests."
            />

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
                    sortDirValue={filters.sortDir}
                    onSortChange={handleSortChange}
                    options={departmentSortOptions}
                />
            </FilterBar>

            <LoadingBar loading={loading}/>

            <PaginatedDataView
                loading={loading}
                data={departments}
                pagination={pagination}
                onPageChange={handlePageChange}
                onSizeChange={handleSizeChange}
                emptyMessage="No departments found."
                emptySubMessage="Try clearing your filters or creating a new department."
            >
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
                            await deleteDepartmentsBulk(selectedIds, () => fetchDepartmentsPaged(filters));
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
