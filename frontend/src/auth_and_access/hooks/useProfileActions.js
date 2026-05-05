import { useCallback } from "react";
import useSnackbar from "../../common/hooks/useSnackbar.js";
import useConfirmDialog from "../../common/hooks/useConfirmDialog.js";
import userService from "../services/userService.js";

const useProfileActions = () => {
    const { showSnackbar } = useSnackbar();
    const { confirm } = useConfirmDialog();

    const updateMyAccount = useCallback(async (data, onSuccess = null) => {
        const confirmed = await confirm({
            title: "Update account?",
            message: "Are you sure you want to update your account information?",
            confirmText: "Update",
            cancelText: "Cancel"
        });

        if (!confirmed) {
            return false;
        }

        try {
            const response = await userService.updateMyAccount(data);

            showSnackbar("Account updated successfully", "success");

            if (onSuccess) {
                onSuccess(response.data);
            }

            return true;
        } catch (error) {
            const message =
                error.response?.data?.message ||
                error.response?.data?.error ||
                error.message ||
                "Failed to update account";

            showSnackbar(message, "error");
            return false;
        }
    }, [confirm, showSnackbar]);

    const updateMyProfile = useCallback(async (data, onSuccess = null) => {
        const confirmed = await confirm({
            title: "Update profile?",
            message: "Are you sure you want to update your profile information?",
            confirmText: "Update",
            cancelText: "Cancel"
        });

        if (!confirmed) {
            return false;
        }

        try {
            const response = await userService.updateMyProfile(data);

            showSnackbar("Profile updated successfully", "success");

            if (onSuccess) {
                onSuccess(response.data);
            }

            return true;
        } catch (error) {
            const message =
                error.response?.data?.message ||
                error.response?.data?.error ||
                error.message ||
                "Failed to update profile";

            showSnackbar(message, "error");
            return false;
        }
    }, [confirm, showSnackbar]);

    const changeMyPassword = useCallback(async (data) => {
        if (data.newPassword !== data.confirmNewPassword) {
            showSnackbar("New passwords do not match", "error");
            return false;
        }

        const confirmed = await confirm({
            title: "Change password?",
            message: "Are you sure you want to change your password?",
            confirmText: "Change Password",
            cancelText: "Cancel"
        });

        if (!confirmed) {
            return false;
        }

        try {
            await userService.changeMyPassword(data);

            showSnackbar("Password changed successfully", "success");

            return true;
        } catch (error) {
            const message =
                error.response?.data?.message ||
                error.response?.data?.error ||
                error.message ||
                "Failed to change password";

            showSnackbar(message, "error");
            return false;
        }
    }, [confirm, showSnackbar]);

    const updateMyProfilePicture = useCallback(async (file, onSuccess = null) => {
        if (!file) {
            showSnackbar("Please select a file first", "error");
            return false;
        }

        const confirmed = await confirm({
            title: "Update profile picture?",
            message: "Are you sure you want to update your profile picture?",
            confirmText: "Upload",
            cancelText: "Cancel"
        });

        if (!confirmed) {
            return false;
        }

        try {
            const response = await userService.updateMyProfilePicture(file);

            showSnackbar("Profile picture updated successfully", "success");

            if (onSuccess) {
                onSuccess(response.data);
            }

            return true;
        } catch (error) {
            const message =
                error.response?.data?.message ||
                error.response?.data?.error ||
                error.message ||
                "Failed to update profile picture";

            showSnackbar(message, "error");
            return false;
        }
    }, [confirm, showSnackbar]);

    const deleteMyProfilePicture = useCallback(async (onSuccess = null) => {
        const confirmed = await confirm({
            title: "Delete profile picture?",
            message: "Are you sure you want to delete your profile picture?",
            confirmText: "Delete",
            cancelText: "Cancel"
        });

        if (!confirmed) {
            return false;
        }

        try {
            const response = await userService.deleteMyProfilePicture();

            showSnackbar("Profile picture deleted successfully", "success");

            if (onSuccess) {
                onSuccess(response.data);
            }

            return true;
        } catch (error) {
            const message =
                error.response?.data?.message ||
                error.response?.data?.error ||
                error.message ||
                "Failed to delete profile picture";

            showSnackbar(message, "error");
            return false;
        }
    }, [confirm, showSnackbar]);

    return {
        updateMyAccount,
        updateMyProfile,
        changeMyPassword,
        updateMyProfilePicture,
        deleteMyProfilePicture
    };
};

export default useProfileActions;