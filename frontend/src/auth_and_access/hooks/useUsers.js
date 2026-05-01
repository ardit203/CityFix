import { useCallback, useEffect, useState } from "react";
import userApi from "../service/userApi.js";
import useSnackbar from "../../common/hooks/useSnackbar.js";

const useUsers = () => {
    const [users, setUsers] = useState([]);

    const { showSnackbar } = useSnackbar();

    const [pagination, setPagination] = useState({
        totalPages: 0,
        totalElements: 0,
        page: 0,
        size: 10,
        first: true,
        last: true
    });

    const [loading, setLoading] = useState(true);
    const [error, setError] = useState(null);

    const fetchUsers = useCallback((filters = {}) => {
        setLoading(true);
        setError(null);

        userApi
            .findAll(filters)
            .then((response) => {
                const data = response.data;

                setUsers(data.content);

                setPagination({
                    totalPages: data.totalPages,
                    totalElements: data.totalElements,
                    page: data.number,
                    size: data.size,
                    first: data.first,
                    last: data.last
                });

                console.log("Content: ", response.data.content)
            })
            .catch((error) => {
                const message =
                    error.response?.data?.message ||
                    error.response?.data?.error ||
                    error.message ||
                    "Something went wrong";

                showSnackbar(message, "error");
                setError(message);
            })
            .finally(() => {
                setLoading(false);
            });
    }, [showSnackbar]);

    useEffect(() => {
        fetchUsers();
    }, [fetchUsers]);

    return {
        users,
        pagination,
        loading,
        error,
        fetchUsers
    };
};

export default useUsers;