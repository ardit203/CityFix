import {useCallback, useEffect, useState} from "react";
import useAsyncState from "../../../common/hooks/useAsyncState.js";
import usePaginationState from "../../../common/hooks/usePaginationState.js";
import {mapToCleanQueryParams} from "../../../common/dtos/filterDto.js";
import requestAssignmentService from "../../services/requestAssignmentService.js";

const useRequestAssignments = ({requestId = null, paged = false, fetchOnMount = true} = {}) => {
    const [assignments, setAssignments] = useState([]);
    const {loading, error, handleError, startAsync, finishAsync} = useAsyncState();
    const {pagination, updatePagination} = usePaginationState();

    const fetchAssignmentsByRequest = useCallback(async () => {
        if (!requestId) return;

        startAsync();
        try {
            const response = await requestAssignmentService.findAllByRequest(requestId);
            setAssignments(response.data);
        } catch (error) {
            handleError(error);
        } finally {
            finishAsync();
        }
    }, [requestId, startAsync, finishAsync, handleError]);

    const fetchAssignmentsPaged = useCallback(async (filters = {}) => {
        startAsync();
        try {
            const safeFilters = mapToCleanQueryParams(filters);
            const response = await requestAssignmentService.findAllPaged(safeFilters);
            setAssignments(response.data.content);
            updatePagination(response.data);
        } catch (error) {
            handleError(error);
        } finally {
            finishAsync();
        }
    }, [startAsync, finishAsync, handleError, updatePagination]);

    useEffect(() => {
        if (!fetchOnMount) return;

        if (paged) {
            void fetchAssignmentsPaged();
        } else {
            void fetchAssignmentsByRequest();
        }
    }, [fetchOnMount, paged, fetchAssignmentsPaged, fetchAssignmentsByRequest]);

    return {
        assignments,
        pagination,
        loading,
        error,
        fetchAssignmentsByRequest,
        fetchAssignmentsPaged
    };
};

export default useRequestAssignments;
