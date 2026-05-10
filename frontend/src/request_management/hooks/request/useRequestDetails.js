import {useCallback, useEffect, useState} from "react";
import useAsyncState from "../../../common/hooks/useAsyncState.js";
import requestService from "../../services/requestService.js";

const useRequestDetails = (id) => {
    const [request, setRequest] = useState(null);
    const {loading, error, handleError, startAsync, finishAsync} = useAsyncState();

    const fetchRequest = useCallback(async () => {
        if (!id) return;
        startAsync();
        try {
            const response = await requestService.findById(id);
            setRequest(response.data);
        } catch (error) {
            handleError(error);
        } finally {
            finishAsync();
        }
    }, [id, startAsync, finishAsync, handleError]);

    useEffect(() => {
        void fetchRequest();
    }, [fetchRequest]);

    return {request, loading, error, fetchRequest};
};

export default useRequestDetails;