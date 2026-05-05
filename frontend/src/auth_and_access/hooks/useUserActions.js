import { useCallback } from "react";
import { useNavigate } from "react-router";
import useSnackbar from "../../common/hooks/useSnackbar.js";
import useConfirmDialog from "../../common/hooks/useConfirmDialog.js";
import userService from "../services/userService.js";

const useUserActions = () => {
    const navigate = useNavigate();
    const { showSnackbar } = useSnackbar();
    const { confirm } = useConfirmDialog();

    const updateUser = useCallback(async (user, formData, onSuccess = null) => {
        if (!user) {
            return false;
        }

        const confirmed = await confirm({
            title: "Update user?",
            message: "Are you sure you want to update user?",
            confirmText: "Update",
            cancelText: "Cancel"
        });

        if (!confirmed) {
            return false;
        }

        try {
            const response = await userService.update(user.id, formData);

            showSnackbar("User updated successfully", "success");

            if (onSuccess) {
                onSuccess(response.data);
            }

            return true;
        } catch (error) {
            const message =
                error.response?.data?.message ||
                error.response?.data?.error ||
                error.message ||
                "Failed to update user";

            showSnackbar(message, "error");

            return false;
        }
    }, [confirm, showSnackbar]);

    const deleteUser = useCallback(async (user, onSuccess = null) => {
        if (!user) {
            return false;
        }

        const confirmed = await confirm({
            title: "Delete user?",
            message: `Are you sure you want to delete ${user.username}? This action cannot be undone.`,
            confirmText: "Delete",
            cancelText: "Cancel"
        });

        if (!confirmed) {
            return false;
        }

        try {
            await userService.deleteById(user.id);

            showSnackbar("User deleted successfully", "success");

            if (onSuccess) {
                onSuccess(user);
            } else {
                navigate("/users");
            }

            return true;
        } catch (error) {
            const message =
                error.response?.data?.message ||
                error.response?.data?.error ||
                error.message ||
                "Failed to delete user";

            showSnackbar(message, "error");

            return false;
        }
    }, [confirm, navigate, showSnackbar]);

    const changeRole = useCallback(async (user, role, onSuccess = null) => {
        if (!user) {
            return false;
        }

        const confirmed = await confirm({
            title: "Change user role?",
            message: `Are you sure you want to change the role of ${user.username}?`,
            confirmText: "Change Role",
            cancelText: "Cancel"
        });

        if (!confirmed) {
            return false;
        }

        try {
            const response = await userService.changeRole(user.id, role);

            showSnackbar("User role changed successfully", "success");

            if (onSuccess) {
                onSuccess(response.data);
            }

            return true;
        } catch (error) {
            const message =
                error.response?.data?.message ||
                error.response?.data?.error ||
                error.message ||
                "Failed to change user role";

            showSnackbar(message, "error");

            return false;
        }
    }, [confirm, showSnackbar]);

    const lockUser = useCallback(async (user, lockedUntil, onSuccess = null) => {
        if (!user) {
            return false;
        }

        const confirmed = await confirm({
            title: "Lock user account?",
            message: `Are you sure you want to lock ${user.username} until ${lockedUntil}?`,
            confirmText: "Lock",
            cancelText: "Cancel"
        });

        if (!confirmed) {
            return false;
        }

        try {
            const response = await userService.lock(user.id, lockedUntil);

            showSnackbar("User locked successfully", "success");

            if (onSuccess) {
                onSuccess(response.data);
            }

            return true;
        } catch (error) {
            const message =
                error.response?.data?.message ||
                error.response?.data?.error ||
                error.message ||
                "Failed to lock user";

            showSnackbar(message, "error");

            return false;
        }
    }, [confirm, showSnackbar]);

    const unlockUser = useCallback(async (user, onSuccess = null) => {
        if (!user) {
            return false;
        }

        const confirmed = await confirm({
            title: "Unlock user account?",
            message: `Are you sure you want to unlock ${user.username}?`,
            confirmText: "Unlock",
            cancelText: "Cancel"
        });

        if (!confirmed) {
            return false;
        }

        try {
            const response = await userService.unlock(user.id);

            showSnackbar("User unlocked successfully", "success");

            if (onSuccess) {
                onSuccess(response.data);
            }

            return true;
        } catch (error) {
            const message =
                error.response?.data?.message ||
                error.response?.data?.error ||
                error.message ||
                "Failed to unlock user";

            showSnackbar(message, "error");

            return false;
        }
    }, [confirm, showSnackbar]);

    return {
        updateUser,
        deleteUser,
        changeRole,
        lockUser,
        unlockUser
    };
};

export default useUserActions;