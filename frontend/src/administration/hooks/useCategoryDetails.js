import {useCallback, useEffect, useState} from "react";
import useSnackbar from "../../common/hooks/useSnackbar.js";
import categoryApi from "../service/categoryApi.js";
import useConfirmDialog from "../../common/hooks/useConfirmDialog.js";

const useCategoryDetails = (id, fetch = true) => {
    const [category, setCategory] = useState(null);
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState(null);

    const {confirm} = useConfirmDialog();
    const {showSnackbar} = useSnackbar();

    const handleError = useCallback((error) => {
        const message =
            error.response?.data?.message ||
            error.response?.data?.error ||
            error.message ||
            "Something went wrong";

        setError(message);
        showSnackbar(message, "error");
    }, [showSnackbar]);

    const fetchCategory = useCallback(() => {
        if (!id) {
            setLoading(false);
            return;
        }

        setLoading(true);
        setError(null);

        categoryApi
            .findById(id)
            .then((response) => {
                setCategory(response.data);
            })
            .catch(handleError)
            .finally(() => {
                setLoading(false);
            });
    }, [id, handleError]);

    const updateCategory = useCallback(async (data) => {
        const confirmed = await confirm({
            title: "Update category?",
            message: `Are you sure you want to update ${data.name}?`,
            confirmText: "Update",
            cancelText: "Cancel"
        });

        if (!confirmed) {
            return null;
        }

        setLoading(true);
        setError(null);

        try {
            const response = await categoryApi.update(id, data);

            setCategory(response.data);
            showSnackbar("Category updated successfully", "success");

            return response.data;
        } catch (error) {
            handleError(error);
            return null;
        } finally {
            setLoading(false);
        }
    }, [confirm, id, showSnackbar, handleError]);

    useEffect(() => {
        if (fetch) {
            fetchCategory();
        }
    }, [fetch, fetchCategory]);

    return {
        category,
        loading,
        error,
        fetchCategory,
        updateCategory
    };
};

export default useCategoryDetails;