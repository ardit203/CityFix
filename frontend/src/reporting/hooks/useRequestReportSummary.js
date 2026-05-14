import {useCallback, useEffect, useState} from "react";
import useAsyncState from "../../common/hooks/useAsyncState.js";
import reportService from "../services/reportService.js";

const emptySummary = {
    totalRequests: 0,
    unassignedRequests: 0,
    assignedRequests: 0,
    byStatus: [],
    byPriority: [],
    byRoutingStatus: [],
    byDepartment: [],
    byMunicipality: [],
    byCategory: []
};

const useRequestReportSummary = () => {
    const [summary, setSummary] = useState(emptySummary);
    const {loading, error, handleError, startAsync, finishAsync} = useAsyncState();

    const fetchSummary = useCallback(async (filters = {}) => {
        startAsync();
        try {
            const response = await reportService.getRequestSummary(filters);
            setSummary({...emptySummary, ...response.data});
        } catch (error) {
            handleError(error);
        } finally {
            finishAsync();
        }
    }, [startAsync, finishAsync, handleError]);

    useEffect(() => {
        void fetchSummary();
    }, [fetchSummary]);

    return {
        summary,
        loading,
        error,
        fetchSummary
    };
};

export default useRequestReportSummary;
