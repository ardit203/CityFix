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
import PageHeader from "../../../../common/ui/components/PageHeader.jsx";
import SortControls from "../../../../common/ui/components/SortControls.jsx";
import {
    categoryColumns,
    categorySortOptions
} from "../../components/category/CategoryConfig.jsx";
import { emptyCategoryFilter } from "../../../dtos/filterDto.js";
import useCategories from "../../../hooks/category/useCategories.js";
import useCategoryActions from "../../../hooks/category/useCategoryActions.js";
import CategoryGrid from "../../components/category/CategoryGrid.jsx";
import useDepartments from "../../../hooks/department/useDepartments.js";

const CategoriesPage = () => {
    const navigate = useNavigate();
    const {categories, loading, pagination, fetchCategoriesPaged} = useCategories();
    const { departments, loading: loadingDepartments } = useDepartments({ paged: false });
    const {deleteCategory, deleteCategoriesBulk} = useCategoryActions();
    const [viewMode, setViewMode] = useState('table');

    const {
        filters,
        handleFilterChange,
        handleSearch,
        handleClearFilters,
        handlePageChange,
        handleSizeChange,
        handleSortChange
    } = useFilters(emptyCategoryFilter, fetchCategoriesPaged);

    const handleViewChange = (event, nextView) => {
        if (nextView !== null) {
            setViewMode(nextView);
        }
    };

    return (
        <Box>
            <PageHeader
                title="Categories"
                subtitle="Manage request categories and their department routing."
            />

            <FilterBar
                onSearch={handleSearch}
                onClear={handleClearFilters}
                onAdd={() => navigate("/categories/create")}
                addLabel="Add Category"
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
                <SortControls
                    sortByValue={filters.sortBy}
                    sortDirValue={filters.sortDir}
                    onSortChange={handleSortChange}
                    options={categorySortOptions}
                />
            </FilterBar>

            <LoadingBar loading={loading}/>

            <PaginatedDataView
                loading={loading}
                data={categories}
                pagination={pagination}
                onPageChange={handlePageChange}
                onSizeChange={handleSizeChange}
                emptyMessage="No categories found."
                emptySubMessage="Try clearing your filters or creating a new category."
            >
                {viewMode === 'table' ? (
                    <AdminTable
                        data={categories}
                        columns={categoryColumns}
                        onInfo={(category) => navigate(`/categories/${category.id}`)}
                        onEdit={(category) => navigate(`/categories/${category.id}/edit`)}
                        onDelete={async (category) => {
                            await deleteCategory(category.id, () => fetchCategoriesPaged(filters));
                        }}
                        onBulkDelete={async (selectedIds) => {
                            await deleteCategoriesBulk(selectedIds, () => fetchCategoriesPaged(filters));
                        }}
                    />
                ) : (
                    <CategoryGrid
                        categories={categories}
                        onDelete={() => fetchCategoriesPaged(filters)}
                    />
                )}
            </PaginatedDataView>
        </Box>
    );


};

export default CategoriesPage;
