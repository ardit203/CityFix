import {useCallback} from "react";
import departmentService from "../../service/departmentService.js";
import useActionHelper from "../../../common/hooks/useActionHelper.js";

const useDepartmentActions = () => {
    const {executeAction, isExecuting} = useActionHelper();

    const createDepartment = useCallback(async (data, onSuccess = null) => {
        return executeAction({
            successMessage: "Department created successfully!",
            actionFn: () => departmentService.create(data),
            onSuccess: onSuccess
        });
    }, [executeAction]);

    const deleteDepartment = useCallback(async (id, onSuccess) => {
        return executeAction({
            confirmDialog: {title: "Delete Department?", message: "Are you sure you want to delete this?"},
            successMessage: "Department deleted successfully!",
            actionFn: () => departmentService.deleteById(id),
            onSuccess: onSuccess
        });
    }, [executeAction]);

    const updateDepartment = useCallback(async (id, data, onSuccess) => {
        return executeAction({
            confirmDialog: {title: "Update Department?", message: "Are you sure you want to update this?"},
            successMessage: "Department updated successfully!",
            actionFn: () => departmentService.update(id, data),
            onSuccess: onSuccess
        });
    }, [executeAction]);

    const deleteDepartmentsBulk = useCallback(async (ids, onSuccess) => {
        return executeAction({
            confirmDialog: { title: "Delete Multiple Departments?", message: `Are you sure you want to delete these ${ids.length} departments? This action cannot be undone.` },
            successMessage: "Departments deleted successfully!",
            actionFn: () => departmentService.bulkDelete(ids),
            onSuccess: onSuccess
        });
    }, [executeAction]);

    return {createDepartment, deleteDepartment, updateDepartment, deleteDepartmentsBulk, isExecuting};
};

export default useDepartmentActions;
