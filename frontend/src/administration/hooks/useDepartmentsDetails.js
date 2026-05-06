import {useCallback, useEffect, useState} from "react";
import useAsyncState from "../../common/hooks/useAsyncState.js";
import departmentService from "../service/departmentService.js";

const useDepartmentDetails = (id) => {
    const [department, setDepartment] = useState(null);
    const {loading, error, handleError, startAsync, finishAsync} = useAsyncState();

    const fetchDepartment = useCallback(async () => {
        if (!id) return;
        startAsync();
        try {
            const response = await departmentService.findById(id);
            setDepartment(response.data);
        } catch (error) {
            handleError(error);
        } finally {
            finishAsync();
        }
    }, [id, startAsync, finishAsync, handleError]);

    useEffect(() => {
        void fetchDepartment();
    }, [fetchDepartment]);

    return {department, loading, error, fetchDepartment};
};

export default useDepartmentDetails;