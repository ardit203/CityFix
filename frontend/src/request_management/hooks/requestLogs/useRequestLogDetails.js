import {useCallback, useEffect, useState} from "react";
import useAsyncState from "../../../common/hooks/useAsyncState.js";
import requestLogService from "../../services/requestLogService.js";

const useRequestLogDetails = (id, requestId) => {
    const [requestLog, setRequestLog] = useState(null);
    const {loading, error, handleError, startAsync, finishAsync} = useAsyncState();

    const fetchRequestLog = useCallback(async () => {
        if (!id) return;
        startAsync();
        try {
            const response = await requestLogService.findById(id, requestId);
            setRequestLog(response.data);
        } catch (error) {
            handleError(error);
        } finally {
            finishAsync();
        }
    }, [id, startAsync, requestId, handleError, finishAsync]);

    useEffect(() => {
        void fetchRequestLog();
    }, [fetchRequestLog]);

    return {requestLog, loading, error, fetchRequestLog};
};

export default useRequestLogDetails;