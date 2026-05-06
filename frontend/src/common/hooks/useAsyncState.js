import {useState, useCallback} from "react";
import useSnackbar from "./useSnackbar.js";

const useAsyncState = (initialLoading = true) => {
    const [loading, setLoading] = useState(initialLoading);
    const [error, setError] = useState(null);
    const {showSnackbar} = useSnackbar();

    const handleError = useCallback((error) => {
        const message = error.response?.data?.message || error.response?.data?.error || error.message || "Something went wrong";
        showSnackbar(message, "error");
        setError(message);
    }, [showSnackbar]);

    const startAsync = useCallback(() => {
        setLoading(true);
        setError(null);
    }, []);
    const finishAsync = useCallback(() => {
        setLoading(false);
    }, []);

    return {loading, error, handleError, startAsync, finishAsync};
};
export default useAsyncState;
