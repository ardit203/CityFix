import { useState, useEffect, useCallback } from "react";
import useAsyncState from "../../../common/hooks/useAsyncState.js";

// Make sure this points to your correct staffService file!
import staffService from "../../services/staffService.js";

const useAvailableUsersForStaff = () => {
    const { loading, error, startAsync, finishAsync, handleError } = useAsyncState();
    
    // We will store the List<DisplayUserPageableDto> here
    const [availableUsers, setAvailableUsers] = useState([]);

    const fetchAvailableUsers = useCallback(async () => {
        startAsync();
        
        try {
            const response = await staffService.findUsersAvailableForStaff();
            setAvailableUsers(response.data);
            
        } catch (err) {
            handleError(err);
        } finally {
            finishAsync();
        }
    }, [startAsync, finishAsync, handleError]);

    // Automatically fetch when the component using this hook mounts!
    useEffect(() => {
        void fetchAvailableUsers();
    }, [fetchAvailableUsers]);

    return {
        availableUsers,
        loading,
        error,
        fetchAvailableUsers
    };
};

export default useAvailableUsersForStaff;
