import {useCallback} from "react";
import departmentService from "../service/departmentService.js";
import useActionHelper from "../../common/hooks/useActionHelper.js";

const useDepartmentActions = () => {
    const {executeAction, isExecuting} = useActionHelper();

    const createDepartment = useCallback((data, onSuccess = null) => {
        return executeAction({
            successMessage: "Department created successfully!",
            actionFn: () => departmentService.create(data),
            onSuccess: onSuccess
        });
    }, [executeAction]);

    const deleteDepartment = useCallback((id, onSuccess) => {
        return executeAction({
            confirmDialog: {title: "Delete Department?", message: "Are you sure you want to delete this?"},
            successMessage: "Department deleted successfully!",
            actionFn: () => departmentService.deleteById(id),
            onSuccess: onSuccess
        });
    }, [executeAction]);

    const updateDepartment = useCallback((id, data, onSuccess) => {
        return executeAction({
            confirmDialog: {title: "Update Department?", message: "Are you sure you want to update this?"},
            successMessage: "Department updated successfully!",
            actionFn: () => departmentService.update(id, data),
            onSuccess: onSuccess
        });
    }, [executeAction]);

    return {createDepartment, deleteDepartment, updateDepartment, isExecuting};
};

export default useDepartmentActions;
