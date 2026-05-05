import {useCallback, useEffect, useState} from "react";
import useSnackbar from "../../common/hooks/useSnackbar.js";
import departmentApi from "../service/departmentApi.js";
import useConfirmDialog from "../../common/hooks/useConfirmDialog.js";


const useDepartments = (paged = true) => {
    const {showSnackbar} = useSnackbar();

    const [departments, setDepartments] = useState([]);
    const [pagination, setPagination] = useState({
        totalPages: 0,
        totalElements: 0,
        page: 0,
        size: 10,
        first: true,
        last: true
    });
    const {confirm} = useConfirmDialog()

    const [loading, setLoading] = useState(true);
    const [error, setError] = useState(null);

    const handleError = useCallback((error) => {
        const message =
            error.response?.data?.message ||
            error.response?.data?.error ||
            error.message ||
            "Something went wrong";

        showSnackbar(message, "error");
        setError(message);
    }, [showSnackbar]);

    const fetchDepartmentsPaged = useCallback(async (filters = {}) => {
        setLoading(true);
        setError(null);

        try {
            const response = await departmentApi.findAllPaged(filters)
            setDepartments(response.data.content);
            setPagination({
                totalPages: response.data.totalPages,
                totalElements: response.data.totalElements,
                page: response.data.number,
                size: response.data.size,
                first: response.data.first,
                last: response.data.last
            });
        } catch (error) {
            handleError(error)
        } finally {
            setLoading(false);
        }
    }, [handleError]);

    const fetchDepartments = useCallback(async () => {
        setLoading(true);
        setError(null);

        try {
            const response = await departmentApi.findAll()
            setDepartments(response.data);
        } catch (error) {
            handleError(error)
        } finally {
            setLoading(false);
        }
    }, [handleError]);
    
    const createDepartment = useCallback(async (data) => {
        const confirmed = await confirm({
            title: "Update Department?",
            message: `Are you sure you want to create ${data.name}?`,
            confirmText: "Update",
            cancelText: "Cancel"
        });

        if (!confirmed) {
            return null;
        }

        setLoading(true);
        setError(null);

        try {
            const response = await departmentApi.create(data);
            showSnackbar("Department updated successfully", "success");
            return response.data;
        } catch (error) {
            handleError(error);
            return null;
        } finally {
            setLoading(false);
        }
    },[handleError, showSnackbar])

    useEffect(() => {
        if (paged) {
            void fetchDepartmentsPaged();
        } else {
            void fetchDepartments();
        }
    }, [paged, fetchDepartmentsPaged, fetchDepartments]);

    return {
        departments,
        pagination,
        loading,
        error,
        fetchDepartmentsPaged,
        fetchDepartments,
        createDepartment
    };
};

export default useDepartments;