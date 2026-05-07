import {useCallback, useEffect, useState} from "react";
import usePaginationState from "../../../common/hooks/usePaginationState.js";
import useAsyncState from "../../../common/hooks/useAsyncState.js";

import {cleanObject} from "../../../common/utils/objectUtils.js";
import staffService from "../../service/staffService.js";


const useStaff = ({paged = true, fetchOnMount = true} = {}) => {
    const [staff, setStaff] = useState([]);
    const {loading, error, handleError, startAsync, finishAsync} = useAsyncState();
    const {pagination, updatePagination} = usePaginationState();

    const fetchStaffPaged = useCallback(async (filters = {}) => {
        startAsync();
        try {
            const response = await staffService.findAllPaged(cleanObject(filters));
            setStaff(response.data.content);
            updatePagination(response.data);
        } catch (error) {
            handleError(error);
        } finally {
            finishAsync();
        }
    }, [startAsync, finishAsync, handleError, updatePagination]);

    const fetchStaffAll = useCallback(async () => {
        startAsync();
        try {
            const response = await staffService.findAll();
            setStaff(response.data);
        } catch (error) {
            handleError(error);
        } finally {
            finishAsync();
        }
    }, [startAsync, finishAsync, handleError]);

    useEffect(() => {
        if (fetchOnMount) {
            if (paged) {
                void fetchStaffPaged();
            } else {
                void fetchStaffPaged();
            }
        }
    }, [fetchOnMount, paged, fetchStaffPaged, fetchStaffAll]);

    return {
        staff,
        pagination,
        loading,
        error,
        fetchStaffPaged,
        fetchStaffAll
    };
};

export default useStaff;
