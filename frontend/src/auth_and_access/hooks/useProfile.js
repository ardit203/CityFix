import { useCallback, useEffect, useState } from "react";
import userApi from "../service/userApi.js";
import useSnackbar from "../../common/hooks/useSnackbar.js";

const useProfile = () => {
    const [profileUser, setProfileUser] = useState(null);
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState(null);
    const { showSnackbar } = useSnackbar();

    const fetchProfile = useCallback(() => {
        setLoading(true);
        setError(null);

        userApi
            .findMe()
            .then((response) => {
                setProfileUser(response.data);
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

    useEffect(() => {
        fetchProfile();
    }, [fetchProfile]);

    return {
        profileUser,
        loading,
        error,
        fetchProfile
    };
};

export default useProfile;