import {useCallback, useEffect, useState} from "react";
import useAsyncState from "../../../common/hooks/useAsyncState.js";
import requestAiSuggestionService from "../../services/requestAiSuggestionService.js";

const useAiSuggestionDetails = (requestId) => {
    const [suggestion, setSuggestion] = useState(null);
    const {loading, error, handleError, startAsync, finishAsync} = useAsyncState();

    const fetchSuggestion = useCallback(async () => {
        if (!requestId) return;
        startAsync();
        try {
            const response = await requestAiSuggestionService.findSuggestion(requestId);
            setSuggestion(response); // Service returns response.data already
        } catch (error) {
            handleError(error);
        } finally {
            finishAsync();
        }
    }, [requestId, startAsync, finishAsync, handleError]);

    useEffect(() => {
        void fetchSuggestion();
    }, [fetchSuggestion]);

    return {suggestion, loading, error, fetchSuggestion};
};

export default useAiSuggestionDetails;
