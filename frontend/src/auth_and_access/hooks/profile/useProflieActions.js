import {useCallback} from "react";
import useActionHelper from "../../../common/hooks/useActionHelper.js";
import userService from "../../services/userService.js";

const useProfileActions = () => {
    const {executeAction, isExecuting} = useActionHelper();

    const updateMyAccount = useCallback(async (data, onSuccess) => {
        return executeAction({
            confirmDialog: {
                title: "Update Account?",
                message: "Are you sure you want to update your account information?"
            },
            successMessage: "Account updated successfully!",
            actionFn: () => userService.updateMyAccount(data),
            onSuccess: onSuccess
        });
    }, [executeAction]);

    const updateMyProfile = useCallback(async (data, onSuccess) => {
        return executeAction({
            confirmDialog: {
                title: "Update Profile?",
                message: "Are you sure you want to update your profile information?"
            },
            successMessage: "Profile updated successfully!",
            actionFn: () => userService.updateMyProfile(data),
            onSuccess: onSuccess
        });
    }, [executeAction]);

    const changeMyPassword = useCallback(async (data, onSuccess = null) => {
        return executeAction({
            confirmDialog: {
                title: "Change password?",
                message: "Are you sure you want to change your password?"
            },
            successMessage: "Password changed successfully",
            actionFn: () => userService.changeMyPassword(data),
            onSuccess: onSuccess
        });
    }, [executeAction]);

    const updateMyProfilePicture = useCallback(async (file, onSuccess) => {
        return executeAction({
            confirmDialog: {
                title: "Update profile picture?",
                message: "Are you sure you want to update your profile picture?"
            },
            successMessage: "Profile picture updated successfully",
            actionFn: () => userService.updateMyProfilePicture(file),
            onSuccess: onSuccess
        });
    }, [executeAction]);

    const deleteMyProfilePicture = useCallback(async (onSuccess) => {
        return executeAction({
            confirmDialog: {
                title: "Delete profile picture?",
                message: "Are you sure you want to delete your profile picture?"
            },
            successMessage: "Profile picture updated successfully",
            actionFn: () => userService.deleteMyProfilePicture(),
            onSuccess: onSuccess
        });
    }, [executeAction]);

    return {
        updateMyAccount,
        updateMyProfile,
        changeMyPassword,
        updateMyProfilePicture,
        deleteMyProfilePicture,
        isExecuting
    };
};


export default useProfileActions;