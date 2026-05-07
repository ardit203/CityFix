import {useCallback, useEffect, useState} from "react";
import useAsyncState from "../../../common/hooks/useAsyncState.js";
import userService from "../../services/userService.js";

const useUserDetails = (id) => {
    const [user, setUser] = useState(null);
    const {loading, error, handleError, startAsync, finishAsync} = useAsyncState();

    const fetchUser = useCallback(async () => {
        if (!id) return;
        startAsync();
        try {
            const response = await userService.findById(id);
            setUser(response.data);
        } catch (error) {
            handleError(error);
        } finally {
            finishAsync();
        }
    }, [id, startAsync, finishAsync, handleError]);

    useEffect(() => {
        void fetchUser();
    }, [fetchUser]);

    return {user, loading, error, fetchUser};
};

export default useUserDetails;