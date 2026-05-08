import {useCallback} from "react";
import useActionHelper from "../../../common/hooks/useActionHelper.js";
import userService from "../../services/userService.js";

const useUserActions = () => {
    const {executeAction, isExecuting} = useActionHelper();

    const deleteUser = useCallback(async (id, onSuccess) => {
        return executeAction({
            confirmDialog: {title: "Delete User?", message: "Are you sure you want to delete this?"},
            successMessage: "User deleted successfully!",
            actionFn: () => userService.deleteById(id),
            onSuccess: onSuccess
        });
    }, [executeAction]);

    const updateUser = useCallback(async (id, data, onSuccess) => {
        return executeAction({
            confirmDialog: {title: "Update User?", message: "Are you sure you want to update this?"},
            successMessage: "User updated successfully!",
            actionFn: () => userService.update(id, data),
            onSuccess: onSuccess
        });
    }, [executeAction]);

    const lockUser = useCallback(async (id, until, onSuccess) => {
        return executeAction({
            confirmDialog: {title: "Lock User?", message: "Are you sure you want to lock this?"},
            successMessage: "User locked successfully!",
            actionFn: () => userService.lock(id, until),
            onSuccess: onSuccess
        });
    }, [executeAction]);

    const unlockUser = useCallback(async (id, onSuccess) => {
        return executeAction({
            confirmDialog: {title: "Unlock User?", message: "Are you sure you want to unlock this?"},
            successMessage: "User unlocked successfully!",
            actionFn: () => userService.unlock(id),
            onSuccess: onSuccess
        });
    }, [executeAction]);

    const changeRole = useCallback(async (id, role, onSuccess) => {
        return executeAction({
            confirmDialog: {
                title: "Change User Role?",
                message: "Are you sure you want to change the role of the user?"
            },
            successMessage: "User role changed successfully!",
            actionFn: () => userService.changeRole(id, role),
            onSuccess: onSuccess
        });
    }, [executeAction]);

    const deleteUsersBulk = useCallback(async (ids, onSuccess) => {
        return executeAction({
            confirmDialog: { title: "Delete Multiple Users?", message: `Are you sure you want to delete these ${ids.length} users? This action cannot be undone.` },
            successMessage: "Users deleted successfully!",
            actionFn: () => userService.bulkDelete(ids),
            onSuccess: onSuccess
        });
    }, [executeAction]);

    return {deleteUser, updateUser, lockUser, unlockUser, changeRole, deleteUsersBulk, isExecuting};
};

export default useUserActions;
