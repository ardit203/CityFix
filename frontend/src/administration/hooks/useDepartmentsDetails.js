import {useCallback, useEffect, useState} from "react";
import useSnackbar from "../../common/hooks/useSnackbar.js";
import useConfirmDialog from "../../common/hooks/useConfirmDialog.js";
import {useNavigate} from "react-router";
import departmentApi from "../service/departmentApi.js";

const useDepartmentDetails = (id, fetch = true) => {
    const navigate = useNavigate()
    const [department, setDepartment] = useState(null);
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState(null);

    const {confirm} = useConfirmDialog();
    const {showSnackbar} = useSnackbar();

    const handleError = useCallback((error) => {
        const message =
            error.response?.data?.message ||
            error.response?.data?.error ||
            error.message ||
            "Something went wrong";

        setError(message);
        showSnackbar(message, "error");
    }, [showSnackbar]);

    const fetchDepartment = useCallback(() => {
        if (!id) {
            setLoading(false);
            return;
        }

        setLoading(true);
        setError(null);

        departmentApi
            .findById(id)
            .then((response) => {
                setDepartment(response.data);
            })
            .catch(handleError)
            .finally(() => {
                setLoading(false);
            });
    }, [id, handleError]);

    const updateDepartment = useCallback(async (data) => {
        const confirmed = await confirm({
            title: "Update Department?",
            message: `Are you sure you want to update ${data.name}?`,
            confirmText: "Update",
            cancelText: "Cancel"
        });

        if (!confirmed) {
            return null;
        }

        setLoading(true);
        setError(null);

        try {
            const response = await departmentApi.update(id, data);

            setDepartment(response.data);
            showSnackbar("Department updated successfully", "success");

            return response.data;
        } catch (error) {
            handleError(error);
            return null;
        } finally {
            setLoading(false);
        }
    }, [confirm, id, showSnackbar, handleError]);

    const deleteDepartment = useCallback(async (onSuccess = null) => {
        const confirmed = await confirm({
            title: "Delete department?",
            message: `Are you sure you want to delete the department?`,
            confirmText: "Delete",
            cancelText: "Cancel"
        });

        if (!confirmed) {
            return null;
        }

        setLoading(true);
        setError(null);

        try {
            await departmentApi.deleteById(id);
            showSnackbar("Department deleted successfully", "success");

            if (onSuccess) {
                console.log("success-func", onSuccess)
                onSuccess()
            } else {
                navigate("/departments")
            }
            return true;
        } catch (error) {
            handleError(error);
            return null;
        } finally {
            setLoading(false);
        }
    }, [confirm, id, showSnackbar, navigate, handleError]);

    useEffect(() => {
        if (fetch) {
            fetchDepartment();
        }
    }, [fetch, fetchDepartment]);

    return {
        department,
        loading,
        error,
        fetchDepartment,
        updateDepartment,
        deleteDepartment
    };
};

export default useDepartmentDetails;