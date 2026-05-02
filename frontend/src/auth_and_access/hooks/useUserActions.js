import {useNavigate} from "react-router";
import useConfirmDialog from "../../common/hooks/useConfirmDialog.js";
import useSnackbar from "../../common/hooks/useSnackbar.js";
import userApi from "../service/userApi.js";

const useUserActions = () => {
    const navigate = useNavigate();
    const {confirm} = useConfirmDialog();
    const {showSnackbar} = useSnackbar();

    const updateUser = async (user, formData, onSuccess) => {
        const confirmed = await confirm({
            title: "Update user?",
            message: `Are you sure you want to update ${user.username}?`,
            confirmText: "Update",
            cancelText: "Cancel"
        });

        if (!confirmed) {
            return false;
        }

        try {
            console.log("Update user", user.id, formData);

            await userApi.update(user.id, formData);

            showSnackbar("User updated successfully", "success");

            if (onSuccess) {
                onSuccess();
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
    };

    const deleteUser = async (user) => {
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
            console.log("Delete user", user.id);

            await userApi.deleteById(user.id);

            showSnackbar("User deleted successfully", "success");

            navigate("/users");

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
    };

    const changeRole = async (user, role, onSuccess) => {
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
            console.log("Change role", user.id, role);

            await userApi.changeRole(user.id, role);

            showSnackbar("User role changed successfully", "success");

            if (onSuccess) {
                onSuccess();
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
    };

    const lockUser = async (user, lockedUntil, onSuccess) => {
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
            console.log("Lock user", user.id, lockedUntil);

            await userApi.lock(user.id, lockedUntil);

            showSnackbar("User locked successfully", "success");

            if (onSuccess) {
                onSuccess();
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
    };

    const unlockUser = async (user, onSuccess) => {
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
            console.log("Unlock user", user.id);

            await userApi.unlock(user.id);

            showSnackbar("User unlocked successfully", "success");

            if (onSuccess) {
                onSuccess();
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
    };

    const goToEdit = (userId) => {
        navigate(`/users/${userId}/edit`);
    };

    const goToDetails = (userId) => {
        navigate(`/users/${userId}`);
    };

    return {
        updateUser,
        deleteUser,
        changeRole,
        lockUser,
        unlockUser,
        goToEdit,
        goToDetails
    };
};

export default useUserActions;