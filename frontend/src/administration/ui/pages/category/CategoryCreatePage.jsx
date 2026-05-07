import React from "react";
import {useNavigate} from "react-router";
import {
    Box,
    Typography
} from "@mui/material";
import ActionBar from "../../../../common/ui/components/ActionBar.jsx";
import useCategoryActions from "../../../hooks/category/useCategoryActions.js";
import CategoryForm from "../../components/category/CategoryForm.jsx";

const CategoryCreatePage = () => {
    const navigate = useNavigate();

    const {createCategory} = useCategoryActions();

    const handleCreate = async (data) => {
        const createdCategory = await createCategory(data);

        if (createdCategory?.id) {
            navigate(`/categories/${createdCategory.id}`);
        } else {
            navigate("/categories");
        }
    };

    return (
        <Box>
            <ActionBar
                onSecondaryBack={() => navigate("/categories")}
                secondaryBackLabel="Back to Categories"
            />
            <Typography variant="h4" fontWeight={600} sx={{mb: 3}}>
                Create Category
            </Typography>

            <CategoryForm
                submitLabel="Create Category"
                onSubmit={handleCreate}
            />
        </Box>
    );
};

export default CategoryCreatePage;