import { useCallback, useEffect, useState } from "react";
import userService from "../services/userService.js";
import useSnackbar from "../../common/hooks/useSnackbar.js";

const useProfile = () => {
    const [user, setUser] = useState(null);
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState(null);

    const { showSnackbar } = useSnackbar();

    const fetchProfile = useCallback(() => {
        setLoading(true);
        setError(null);

        userService
            .findMe()
            .then((response) => {
                setUser(response.data);
            })
            .catch((error) => {
                const message =
                    error.response?.data?.message ||
                    error.response?.data?.error ||
                    error.message ||
                    "Failed to load profile";

                setError(message);
                showSnackbar(message, "error");
            })
            .finally(() => {
                setLoading(false);
            });
    }, [showSnackbar]);

    const updateProfileUserInState = useCallback((updatedUser) => {
        setUser(updatedUser);
    }, []);

    useEffect(() => {
        fetchProfile();
    }, [fetchProfile]);

    return {
        user,
        loading,
        error,
        fetchProfile,
        updateProfileUserInState
    };
};

export default useProfile;