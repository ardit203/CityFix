import {useCallback} from "react";

import useActionHelper from "../../../common/hooks/useActionHelper.js";
import requestService from "../../services/requestService.js";

const useRequestActions = () => {
    const {executeAction, isExecuting} = useActionHelper();

    const createRequest = useCallback(async (data, onSuccess = null) => {
        return executeAction({
            successMessage: "Request created successfully!",
            actionFn: () => requestService.create(data),
            onSuccess: onSuccess
        });
    }, [executeAction]);

    const deleteRequest = useCallback(async (id, onSuccess) => {
        return executeAction({
            confirmDialog: {title: "Delete Request?", message: "Are you sure you want to delete this?"},
            successMessage: "Request deleted successfully!",
            actionFn: () => requestService.deleteById(id),
            onSuccess: onSuccess
        });
    }, [executeAction]);

    const cancelRequest = useCallback(async (id, data, onSuccess) => {
        return executeAction({
            confirmDialog: {title: "Cancel Request?", message: "Are you sure you want to cancel this?"},
            successMessage: "Request cancelled successfully!",
            actionFn: () => requestService.cancel(id),
            onSuccess: onSuccess
        });
    }, [executeAction]);

    const deleteRequestsBulk = useCallback(async (ids, onSuccess) => {
        return executeAction({
            confirmDialog: {
                title: "Delete Multiple Requests?",
                message: `Are you sure you want to delete these ${ids.length} requests? This action cannot be undone.`
            },
            successMessage: "Requests deleted successfully!",
            actionFn: () => requestService.bulkDelete(ids),
            onSuccess: onSuccess
        });
    }, [executeAction]);

    return {createRequest, deleteRequest, cancelRequest, deleteRequestsBulk, isExecuting};
};

export default useRequestActions;
