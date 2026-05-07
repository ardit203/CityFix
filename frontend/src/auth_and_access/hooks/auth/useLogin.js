import useAsyncState from "../../../common/hooks/useAsyncState.js";
import {useCallback} from "react";
import authService from "../../services/authService.js";
import {useNavigate} from "react-router";
import useSnackbar from "../../../common/hooks/useSnackbar.js";
import useAuth from "./useAuth.js";

const useLogin = () => {
    const {loading, error, handleError, startAsync, finishAsync} = useAsyncState(false);
    const navigate = useNavigate();
    const {showSnackbar} = useSnackbar();
    const {login: authLogin} = useAuth();

    const login = useCallback(async (data) => {
        startAsync();

        try {
            const response = await authService.login(data)
            authLogin(response.data.token)
            showSnackbar("You have logged-in successfully!", "success")
            navigate("/")
        } catch (error) {
            handleError(error);
        } finally {
            finishAsync();
        }
    }, [startAsync, authLogin, showSnackbar, navigate, handleError, finishAsync]);
    return {
        login,
        loading,
        error
    }
};

export default useLogin;