import {useCallback, useEffect, useState} from "react";
import useAsyncState from "../../../common/hooks/useAsyncState.js";
import usePaginationState from "../../../common/hooks/usePaginationState.js";
import {mapToCleanQueryParams} from "../../../common/dtos/filterDto.js";
import requestService from "../../services/requestService.js";

const useRequests = ({paged = true, fetchOnMount = true} = {}) => {
    const [requests, setRequests] = useState([]);
    const {loading, error, handleError, startAsync, finishAsync} = useAsyncState();
    const {pagination, updatePagination} = usePaginationState();

    const fetchRequestsPaged = useCallback(async (filters = {}) => {
        startAsync();
        try {
            const safeFilters = mapToCleanQueryParams(filters);
            const response = await requestService.findAllPaged(safeFilters);
            setRequests(response.data.content);
            updatePagination(response.data);
        } catch (error) {
            handleError(error);
        } finally {
            finishAsync();
        }
    }, [startAsync, finishAsync, handleError, updatePagination]);

    const fetchRequestsAll = useCallback(async () => {
        startAsync();
        try {
            const response = await requestService.findAll();
            setRequests(response.data);
        } catch (error) {
            handleError(error);
        } finally {
            finishAsync();
        }
    }, [startAsync, finishAsync, handleError]);

    useEffect(() => {
        if (fetchOnMount) {
            if (paged) {
                void fetchRequestsPaged();
            } else {
                void fetchRequestsAll();
            }
        }
    }, [fetchOnMount, paged, fetchRequestsPaged, fetchRequestsAll]);

    return {
        requests,
        pagination,
        loading,
        error,
        fetchRequestsPaged,
        fetchRequestsAll
    };
};

export default useRequests;