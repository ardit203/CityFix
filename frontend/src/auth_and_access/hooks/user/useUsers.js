import {useCallback, useEffect, useState} from "react";
import usePaginationState from "../../../common/hooks/usePaginationState.js";
import useAsyncState from "../../../common/hooks/useAsyncState.js";
import { mapToCleanQueryParams } from "../../../common/dtos/filterDto.js";
import userService from "../../services/userService.js";


const useUsers = ({paged = true, fetchOnMount = true} = {}) => {
    const [users, setUsers] = useState([]);
    const {loading, error, handleError, startAsync, finishAsync} = useAsyncState();
    const {pagination, updatePagination} = usePaginationState();

    const fetchUsersPaged = useCallback(async (filters = {}) => {
        startAsync();
        try {
            const safeFilters = mapToCleanQueryParams(filters);
            const response = await userService.findAllPaged(safeFilters);
            setUsers(response.data.content);
            updatePagination(response.data);
        } catch (error) {
            handleError(error);
        } finally {
            finishAsync();
        }
    }, [startAsync, finishAsync, handleError, updatePagination]);

    const fetchUsersAll = useCallback(async () => {
        startAsync();
        try {
            const response = await userService.findAll();
            setUsers(response.data);
        } catch (error) {
            handleError(error);
        } finally {
            finishAsync();
        }
    }, [startAsync, finishAsync, handleError]);

    useEffect(() => {
        if (fetchOnMount) {
            if (paged) {
                void fetchUsersPaged();
            } else {
                void fetchUsersAll();
            }
        }
    }, [fetchOnMount, paged, fetchUsersPaged, fetchUsersAll]);

    return {
        users,
        pagination,
        loading,
        error,
        fetchUsersPaged,
        fetchUsersAll
    };
};

export default useUsers;
