import {useCallback, useEffect, useState} from "react";
import useAsyncState from "../../../common/hooks/useAsyncState.js";
import categoryService from "../../service/categoryService.js";

const useCategoryDetails = (id) => {
    const [category, setCategory] = useState(null);
    const {loading, error, handleError, startAsync, finishAsync} = useAsyncState();

    const fetchCategory = useCallback(async () => {
        if (!id) return;
        startAsync();
        try {
            const response = await categoryService.findById(id);
            setCategory(response.data);
        } catch (error) {
            handleError(error);
        } finally {
            finishAsync();
        }
    }, [id, startAsync, finishAsync, handleError]);

    useEffect(() => {
        void fetchCategory();
    }, [fetchCategory]);

    return {category, loading, error, fetchCategory};
};

export default useCategoryDetails;