import React from "react";
import {useNavigate, useParams} from "react-router";
import {Box, Typography} from "@mui/material";

import AsyncDataView from "../../../../common/ui/components/AsyncDataView.jsx";
import ActionBar from "../../../../common/ui/components/ActionBar.jsx";
import useCategoryDetails from "../../../hooks/category/useCategoryDetails.js";
import useCategoryActions from "../../../hooks/category/useCategoryActions.js";
import CategoryForm from "../../components/category/CategoryForm.jsx";


const CategoryEditPage = () => {
    const {id} = useParams();
    const navigate = useNavigate();
    const {category, loading, error} = useCategoryDetails(id);
    const {updateCategory} = useCategoryActions();

    const handleUpdate = async (data) => {
        await updateCategory(category.id, data, () => {
            navigate(`/categories/${category.id}`);
        });
    };

    if (loading || error || !category) {
        return (
            <AsyncDataView
                loading={loading}
                error={error}
                data={category}
                notFoundMessage="Category not found."
                backUrl="/categories"
            />
        );
    }

    return (
        <Box>
            <ActionBar
                onBack={() => navigate(`/categories/${category.id}`)}
                backLabel="Back to Category"
                onSecondaryBack={() => navigate("/categories")}
                secondaryBackLabel="Back to Categories"
            >

            </ActionBar>

            <Typography variant="h4" fontWeight={600} sx={{mb: 3}}>
                Edit Category
            </Typography>

            <CategoryForm
                initialValues={category}
                submitLabel="Update category"
                onSubmit={handleUpdate}
            />
        </Box>
    );
};

export default CategoryEditPage;
