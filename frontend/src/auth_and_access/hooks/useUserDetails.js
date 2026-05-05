import { useCallback, useEffect, useState } from "react";
import useSnackbar from "../../common/hooks/useSnackbar.js";
import userService from "../services/userService.js";

const useUserDetails = (id) => {
    const [user, setUser] = useState(null);
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState(null);

    const { showSnackbar } = useSnackbar();

    const fetchUser = useCallback(async () => {
        if (!id) {
            setLoading(false);
            return;
        }

        setLoading(true);
        setError(null);

        try {
            const response = await userService.findById(id);
            setUser(response.data);
        } catch (error) {
            const message =
                error.response?.data?.message ||
                error.response?.data?.error ||
                error.message ||
                "Something went wrong";

            setError(message);
            showSnackbar(message, "error");
        } finally {
            setLoading(false);
        }
    }, [id, showSnackbar]);

    const updateUserInState = useCallback((updatedUser) => {
        setUser(updatedUser);
    }, []);

    useEffect(() => {
        void fetchUser();
    }, [fetchUser]);

    return {
        user,
        loading,
        error,
        fetchUser,
        setUser,
        updateUserInState
    };
};

export default useUserDetails;