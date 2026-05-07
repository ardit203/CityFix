import useAsyncState from "../../../common/hooks/useAsyncState.js";
import { useCallback } from "react";
import authService from "../../services/authService.js";
import useSnackbar from "../../../common/hooks/useSnackbar.js";
import { useNavigate } from "react-router";

const usePasswordReset = () => {
    const { loading, error, handleError, startAsync, finishAsync } = useAsyncState(false);
    const { showSnackbar } = useSnackbar();
    const navigate = useNavigate();

    const requestPasswordReset = useCallback(async (email) => {
        startAsync();
        try {
            await authService.requestPasswordReset(email);
            showSnackbar("Password reset instructions were sent to your email.", "success");
            return true; // Return true so the component knows it succeeded
        } catch (error) {
            handleError(error);
            return false;
        } finally {
            finishAsync();
        }
    }, [startAsync, showSnackbar, handleError, finishAsync]);

    const resetPassword = useCallback(async (data) => {
        startAsync();
        try {
            await authService.resetPassword(data);
            showSnackbar("Password reset successfully", "success");
            navigate("/login");
            return true;
        } catch (error) {
            handleError(error);
            return false;
        } finally {
            finishAsync();
        }
    }, [startAsync, showSnackbar, navigate, handleError, finishAsync]);

    return {
        requestPasswordReset,
        resetPassword,
        loading,
        error
    };
};

export default usePasswordReset;
