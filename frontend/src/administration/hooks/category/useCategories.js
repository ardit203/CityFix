import {useCallback, useEffect, useState} from "react";
import usePaginationState from "../../../common/hooks/usePaginationState.js";
import useAsyncState from "../../../common/hooks/useAsyncState.js";
import { mapToCleanQueryParams } from "../../../common/dtos/filterDto.js";
import categoryService from "../../service/categoryService.js";


const useCategories = ({paged = true, fetchOnMount = true} = {}) => {
    const [categories, setCategories] = useState([]);
    const {loading, error, handleError, startAsync, finishAsync} = useAsyncState();
    const {pagination, updatePagination} = usePaginationState();

    const fetchCategoriesPaged = useCallback(async (filters = {}) => {
        startAsync();
        try {
            const safeFilters = mapToCleanQueryParams(filters);
            const response = await categoryService.findAllPaged(safeFilters);
            setCategories(response.data.content);
            updatePagination(response.data);
        } catch (error) {
            handleError(error);
        } finally {
            finishAsync();
        }
    }, [startAsync, finishAsync, handleError, updatePagination]);

    const fetchCategoriesAll = useCallback(async () => {
        startAsync();
        try {
            const response = await categoryService.findAll();
            setCategories(response.data);
        } catch (error) {
            handleError(error);
        } finally {
            finishAsync();
        }
    }, [startAsync, finishAsync, handleError]);

    useEffect(() => {
        if (fetchOnMount) {
            if (paged) {
                void fetchCategoriesPaged();
            } else {
                void fetchCategoriesAll();
            }
        }
    }, [fetchOnMount, paged, fetchCategoriesPaged, fetchCategoriesAll]);

    return {
        categories,
        pagination,
        loading,
        error,
        fetchCategoriesPaged,
        fetchCategoriesAll
    };
};

export default useCategories;
