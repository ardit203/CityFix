import {useCallback} from "react";

import useActionHelper from "../../../common/hooks/useActionHelper.js";
import categoryService from "../../services/categoryService.js";

const useCategoryActions = () => {
    const {executeAction, isExecuting} = useActionHelper();

    const createCategory = useCallback(async (data, onSuccess = null) => {
        return executeAction({
            successMessage: "Category created successfully!",
            actionFn: () => categoryService.create(data),
            onSuccess: onSuccess
        });
    }, [executeAction]);

    const deleteCategory = useCallback(async (id, onSuccess) => {
        return executeAction({
            confirmDialog: {title: "Delete Category?", message: "Are you sure you want to delete this?"},
            successMessage: "Category deleted successfully!",
            actionFn: () => categoryService.deleteById(id),
            onSuccess: onSuccess
        });
    }, [executeAction]);

    const updateCategory = useCallback(async (id, data, onSuccess) => {
        return executeAction({
            confirmDialog: {title: "Update Category?", message: "Are you sure you want to update this?"},
            successMessage: "Category updated successfully!",
            actionFn: () => categoryService.update(id, data),
            onSuccess: onSuccess
        });
    }, [executeAction]);

    const deleteCategoriesBulk = useCallback(async (ids, onSuccess) => {
        return executeAction({
            confirmDialog: { title: "Delete Multiple Categories?", message: `Are you sure you want to delete these ${ids.length} categories? This action cannot be undone.` },
            successMessage: "Categories deleted successfully!",
            actionFn: () => categoryService.bulkDelete(ids),
            onSuccess: onSuccess
        });
    }, [executeAction]);

    return {createCategory, deleteCategory, updateCategory, deleteCategoriesBulk, isExecuting};
};

export default useCategoryActions;
