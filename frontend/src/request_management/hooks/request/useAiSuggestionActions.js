import {useCallback} from "react";
import useActionHelper from "../../../common/hooks/useActionHelper.js";
import requestAiSuggestionService from "../../services/requestAiSuggestionService.js";

const useAiSuggestionActions = () => {
    const {executeAction, isExecuting} = useActionHelper();

    const processSuggestion = useCallback(async (requestId, data, onSuccess) => {
        return executeAction({
            successMessage: "Suggestion processed and request routed successfully!",
            actionFn: () => requestAiSuggestionService.processSuggestion(requestId, data),
            onSuccess: onSuccess
        });
    }, [executeAction]);

    const rejectSuggestion = useCallback(async (requestId, data, onSuccess) => {
        return executeAction({
            successMessage: "AI Suggestion rejected successfully!",
            actionFn: () => requestAiSuggestionService.rejectSuggestion(requestId, data),
            onSuccess: onSuccess
        });
    }, [executeAction]);

    return {
        processSuggestion,
        rejectSuggestion,
        isExecuting
    };
};

export default useAiSuggestionActions;
