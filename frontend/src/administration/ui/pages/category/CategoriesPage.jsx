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
    initialCategoryFilters,
    categoryColumns,
    categorySortOptions
} from "../../components/category/CategoryConfig.jsx";
import useCategories from "../../../hooks/category/useCategories.js";
import useCategoryActions from "../../../hooks/category/useCategoryActions.js";
import CategoryGrid from "../../components/category/CategoryGrid.jsx";

const CategoriesPage = () => {
    const navigate = useNavigate();
    const {categories, loading, pagination, fetchCategoriesPaged} = useCategories();
    const {deleteCategory} = useCategoryActions();
    const [viewMode, setViewMode] = useState('table');

    const {
        filters,
        handleFilterChange,
        handleSearch,
        handleClearFilters,
        handlePageChange,
        handleSizeChange,
        handleSortChange
    } = useFilters(initialCategoryFilters, fetchCategoriesPaged);

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
                onAdd={() => navigate("/categories/create")}
                addLabel="Add Category"
                viewMode={viewMode}
                onViewChange={handleViewChange}
            >
                <TextField label="ID" name="id" value={filters.id} onChange={handleFilterChange} size="small"/>
                <TextField label="Search" name="text" value={filters.text} onChange={handleFilterChange} size="small"/>
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
                            console.log("Deleting multiple IDs:", selectedIds);
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
