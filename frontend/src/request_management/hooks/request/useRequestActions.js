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

    const changeStatus = useCallback(async (id, data, onSuccess) => {
        return executeAction({
            successMessage: "Request status updated successfully!",
            actionFn: () => requestService.changeStatus(id, data),
            onSuccess: onSuccess
        });
    }, [executeAction]);

    const exportRequestPdf = useCallback(async (id) => {
        return executeAction({
            successMessage: "Request PDF exported successfully!",
            actionFn: async () => {
                const response = await requestService.exportPdf(id);
                const file = new Blob([response.data], {type: "application/pdf"});
                const fileUrl = window.URL.createObjectURL(file);
                const link = document.createElement("a");

                link.href = fileUrl;
                link.download = `request-${id}.pdf`;
                link.click();

                window.URL.revokeObjectURL(fileUrl);
                return true;
            }
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

    const confirmRouting = useCallback(async (id, onSuccess) => {
        return executeAction({
            successMessage: "Routing confirmed successfully!",
            actionFn: () => requestService.confirmRouting(id),
            onSuccess: onSuccess
        });
    }, [executeAction]);

    const rejectRouting = useCallback(async (id, data, onSuccess) => {
        return executeAction({
            successMessage: "Routing rejected successfully!",
            actionFn: () => requestService.rejectRouting(id, data),
            onSuccess: onSuccess
        });
    }, [executeAction]);

    const updateRouting = useCallback(async (id, data, onSuccess) => {
        return executeAction({
            successMessage: "Routing updated successfully!",
            actionFn: () => requestService.updateRouting(id, data),
            onSuccess: onSuccess
        });
    }, [executeAction]);

    const reopenRouting = useCallback(async (id, onSuccess) => {
        return executeAction({
            successMessage: "Routing reopened successfully!",
            actionFn: () => requestService.reopenRouting(id),
            onSuccess: onSuccess
        });
    }, [executeAction]);

    return {
        createRequest, 
        deleteRequest, 
        cancelRequest, 
        changeStatus,
        exportRequestPdf,
        deleteRequestsBulk, 
        confirmRouting,
        rejectRouting,
        updateRouting,
        reopenRouting,
        isExecuting
    };
};

export default useRequestActions;
