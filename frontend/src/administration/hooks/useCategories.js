import {useCallback, useEffect, useState} from "react";
import useSnackbar from "../../common/hooks/useSnackbar.js";
import categoryApi from "../service/categoryApi.js";

const useCategories = ({paged = true} = {}) => {
    const {showSnackbar} = useSnackbar();

    const [categories, setCategories] = useState([]);
    const [pagination, setPagination] = useState({
        totalPages: 0,
        totalElements: 0,
        page: 0,
        size: 10,
        first: true,
        last: true
    });

    const [loading, setLoading] = useState(true);
    const [error, setError] = useState(null);

    const handleError = useCallback((error) => {
        const message =
            error.response?.data?.message ||
            error.response?.data?.error ||
            error.message ||
            "Something went wrong";

        showSnackbar(message, "error");
        setError(message);
    }, [showSnackbar]);

    const fetchCategoriesPaged = useCallback(async (filters = {}) => {
        setLoading(true);
        setError(null);

        try {
            const response = await categoryApi.findAllPaged(filters)
            setCategories(response.data.content);
            setPagination({
                totalPages: response.data.totalPages,
                totalElements: response.data.totalElements,
                page: response.data.number,
                size: response.data.size,
                first: response.data.first,
                last: response.data.last
            });
        } catch (error) {
            handleError(error)
        } finally {
            setLoading(false);
        }
    }, [handleError]);

    const fetchCategories = useCallback(async () => {
        setLoading(true);
        setError(null);

        try {
            const response = await categoryApi.findAll()
            setCategories(response.data);
        } catch (error) {
            handleError(error)
        } finally {
            setLoading(false);
        }
    }, [handleError]);

    useEffect(() => {
        if (paged) {
            void fetchCategoriesPaged();
        } else {
            void fetchCategories();
        }
    }, [paged, fetchCategoriesPaged, fetchCategories]);

    return {
        categories,
        pagination,
        loading,
        error,
        fetchCategoriesPaged,
        fetchCategories
    };
};

export default useCategories;