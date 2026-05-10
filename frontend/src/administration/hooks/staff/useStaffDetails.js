import {useCallback, useEffect, useState} from "react";
import useAsyncState from "../../../common/hooks/useAsyncState.js";
import staffService from "../../services/staffService.js";


const useStaffDetails = (id) => {
    const [staff, setStaff] = useState(null);
    const {loading, error, handleError, startAsync, finishAsync} = useAsyncState();

    const fetchStaff = useCallback(async () => {
        if (!id) return;
        startAsync();
        try {
            const response = await staffService.findById(id);
            setStaff(response.data);
        } catch (error) {
            handleError(error);
        } finally {
            finishAsync();
        }
    }, [id, startAsync, finishAsync, handleError]);

    useEffect(() => {
        void fetchStaff();
    }, [fetchStaff]);

    return {staff, loading, error, fetchStaff};
};

export default useStaffDetails;