import {useState, useCallback} from "react";
import useSnackbar from "./useSnackbar.js";
import useConfirmDialog from "./useConfirmDialog.js";

const useActionHelper = () => {
    const {showSnackbar} = useSnackbar();
    const {confirm} = useConfirmDialog();
    const [isExecuting, setIsExecuting] = useState(false); // So you can disable buttons while saving!

    const executeAction = useCallback(async ({actionFn, confirmDialog = null, successMessage, onSuccess = null}) => {
        if (confirmDialog) {
            const confirmed = await confirm(confirmDialog);
            if (!confirmed) return false;
        }

        setIsExecuting(true);
        try {
            const response = await actionFn();
            if (successMessage) showSnackbar(successMessage, "success");
            if (onSuccess) onSuccess(response?.data);
            return response?.data || true;
        } catch (error) {
            const message = error.response?.data?.message || error.response?.data?.error || error.message || "Something went wrong";
            showSnackbar(message, "error");
            return false;
        } finally {
            setIsExecuting(false);
        }
    }, [confirm, showSnackbar]);

    return {executeAction, isExecuting};
};
export default useActionHelper;
