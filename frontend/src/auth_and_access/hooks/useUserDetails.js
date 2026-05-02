import { useEffect, useState } from "react";
import userApi from "../service/userApi.js";
import useSnackbar from "../../common/hooks/useSnackbar.js";

const useUserDetails = (id) => {
    const [user, setUser] = useState(null);
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState(null);

    const { showSnackbar } = useSnackbar();

    useEffect(() => {
        if (!id) {
            setLoading(false);
            return;
        }

        setLoading(true);
        setError(null);

        userApi
            .findById(id)
            .then((response) => {
                setUser(response.data);
                console.log(response.data)
            })
            .catch((error) => {
                const message =
                    error.response?.data?.message ||
                    error.response?.data?.error ||
                    error.message ||
                    "Something went wrong";

                setError(message);
                showSnackbar(message, "error");
            })
            .finally(() => {
                setLoading(false);
            });
    }, [id, showSnackbar]);

    return {
        user,
        loading,
        error
    };
};

export default useUserDetails;