import {useCallback, useEffect, useState} from "react";
import userService from "../services/userService.js";
import useSnackbar from "../../common/hooks/useSnackbar.js";

const useUsers = () => {
    const [users, setUsers] = useState([]);

    const {showSnackbar} = useSnackbar();

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

    const fetchUsers = useCallback(async (filters = {}) => {
        setLoading(true);
        setError(null);

        try {
            const response = await userService.findAll(filters)
            setUsers(response.data.content);

            setPagination({
                totalPages: response.data.totalPages,
                totalElements: response.data.totalElements,
                page: response.data.number,
                size: response.data.size,
                first: response.data.first,
                last: response.data.last
            });
        } catch (error) {
            const message =
                error.response?.data?.message ||
                error.response?.data?.error ||
                error.message ||
                "Something went wrong";

            showSnackbar(message, "error");
            setError(message);
        } finally {
            setLoading(false);
        }
    }, [showSnackbar]);

    const removeUserFromState = useCallback((id) => {
        setUsers((prev) => prev.filter((user) => user.id !== id));
    }, []);

    useEffect(() => {
        void fetchUsers();
    }, [fetchUsers]);

    return {
        users,
        pagination,
        loading,
        error,
        fetchUsers,
        removeUserFromState
    };
};

export default useUsers;