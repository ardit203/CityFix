import {useCallback, useEffect, useState} from "react";
import useAsyncState from "../../../common/hooks/useAsyncState.js";
import usePaginationState from "../../../common/hooks/usePaginationState.js";
import {mapToCleanQueryParams} from "../../../common/dtos/filterDto.js";
import requestLogService from "../../services/requestLogService.js";

const useRequestLogs = (requestId, {paged = true, fetchOnMount = true} = {}) => {
    const [requestLogs, setRequestLogs] = useState([]);
    const {loading, error, handleError, startAsync, finishAsync} = useAsyncState();
    const {pagination, updatePagination} = usePaginationState();

    const fetchRequestsLogsPaged = useCallback(async (filters = {}) => {
        startAsync();
        try {
            const safeFilters = mapToCleanQueryParams(filters);
            const response = await requestLogService.findAllPaged(safeFilters, requestId);
            setRequestLogs(response.data.content);
            updatePagination(response.data);
        } catch (error) {
            handleError(error);
        } finally {
            finishAsync();
        }
    }, [startAsync, requestId, updatePagination, handleError, finishAsync]);

    const fetchRequestsLogsByRequestId = useCallback(async () => {
        startAsync();
        try {
            const response = await requestLogService.findAllByRequestId(requestId);
            setRequestLogs(response.data);
        } catch (error) {
            handleError(error);
        } finally {
            finishAsync();
        }
    }, [startAsync, requestId, handleError, finishAsync]);

    useEffect(() => {
        if (fetchOnMount) {
            if (paged) {
                void fetchRequestsLogsPaged();
            } else {
                void fetchRequestsLogsByRequestId();
            }
        }
    }, [fetchOnMount, paged, fetchRequestsLogsPaged, fetchRequestsLogsByRequestId]);

    return {
        requestLogs,
        pagination,
        loading,
        error,
        fetchRequestsLogsPaged,
        fetchRequestsLogsByRequestId
    };
};

export default useRequestLogs;