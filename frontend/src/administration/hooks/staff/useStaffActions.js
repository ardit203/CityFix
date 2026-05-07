import {useCallback} from "react";
import useActionHelper from "../../../common/hooks/useActionHelper.js";
import staffService from "../../service/staffService.js";

const useStaffActions = () => {
    const {executeAction, isExecuting} = useActionHelper();

    const createStaff = useCallback((data, onSuccess = null) => {
        return executeAction({
            successMessage: "Staff created successfully!",
            actionFn: () => staffService.create(data),
            onSuccess: onSuccess
        });
    }, [executeAction]);

    const deleteStaff = useCallback((id, onSuccess) => {
        return executeAction({
            confirmDialog: {title: "Delete Staff?", message: "Are you sure you want to delete this?"},
            successMessage: "Staff deleted successfully!",
            actionFn: () => staffService.deleteById(id),
            onSuccess: onSuccess
        });
    }, [executeAction]);

    const updateStaff = useCallback((id, data, onSuccess) => {
        return executeAction({
            confirmDialog: {title: "Update Staff?", message: "Are you sure you want to update this?"},
            successMessage: "Staff updated successfully!",
            actionFn: () => staffService.update(id, data),
            onSuccess: onSuccess
        });
    }, [executeAction]);

    return {createStaff, deleteStaff, updateStaff, isExecuting};
};

export default useStaffActions;
