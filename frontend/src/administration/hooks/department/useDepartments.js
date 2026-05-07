import {useCallback, useEffect, useState} from "react";
import usePaginationState from "../../../common/hooks/usePaginationState.js";
import useAsyncState from "../../../common/hooks/useAsyncState.js";
import departmentService from "../../service/departmentService.js";
import {cleanObject} from "../../../common/utils/objectUtils.js";


const useDepartments = ({paged = true, fetchOnMount = true} = {}) => {
    const [departments, setDepartments] = useState([]);
    const {loading, error, handleError, startAsync, finishAsync} = useAsyncState();
    const {pagination, updatePagination} = usePaginationState();

    const fetchDepartmentsPaged = useCallback(async (filters = {}) => {
        startAsync();
        try {
            const response = await departmentService.findAllPaged(cleanObject(filters));
            setDepartments(response.data.content);
            updatePagination(response.data);
        } catch (error) {
            handleError(error);
        } finally {
            finishAsync();
        }
    }, [startAsync, finishAsync, handleError, updatePagination]);

    const fetchDepartmentsAll = useCallback(async () => {
        startAsync();
        try {
            const response = await departmentService.findAll();
            setDepartments(response.data);
        } catch (error) {
            handleError(error);
        } finally {
            finishAsync();
        }
    }, [startAsync, finishAsync, handleError]);

    useEffect(() => {
        if (fetchOnMount) {
            if (paged) {
                console.log("paged")
                void fetchDepartmentsPaged();
            } else {
                void fetchDepartmentsAll();
            }
        }
    }, [fetchOnMount, paged, fetchDepartmentsPaged, fetchDepartmentsAll]);

    return {
        departments,
        pagination,
        loading,
        error,
        fetchDepartmentsPaged,
        fetchDepartmentsAll
    };
};

export default useDepartments;
