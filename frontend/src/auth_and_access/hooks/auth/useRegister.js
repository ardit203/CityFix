import useAsyncState from "../../../common/hooks/useAsyncState.js";
import {useCallback} from "react";
import authService from "../../services/authService.js";
import {useNavigate} from "react-router";
import useSnackbar from "../../../common/hooks/useSnackbar.js";

const useRegister = () => {
    const {loading, error, handleError, startAsync, finishAsync} = useAsyncState(false);
    const navigate = useNavigate();
    const {showSnackbar} = useSnackbar();

    const register = useCallback(async (data) => {
        startAsync();

        try {
            await authService.register(data)
            showSnackbar("You have registered successfully. Login to proceed!", "success")
            navigate("/login")
        } catch (error) {
            handleError(error);
        } finally {
            finishAsync();
        }
    }, [startAsync, showSnackbar, navigate, handleError, finishAsync]);
    return {
        register,
        loading,
        error
    }
};

export default useRegister;