import {useCallback, useEffect, useState} from "react";
import useAsyncState from "../../../common/hooks/useAsyncState.js";
import userService from "../../services/userService.js";

const useProfile = () => {
    const [user, setUser] = useState(null);
    const {loading, error, handleError, startAsync, finishAsync} = useAsyncState();


    const fetchProfile = useCallback(async () => {
        startAsync();

        try {
            const response = await userService.findMe()
            setUser(response.data)
        } catch (error) {
            handleError(error);
        } finally {
            finishAsync();
        }
    }, [finishAsync, handleError, startAsync]);

    useEffect(() => {
        void fetchProfile();
    }, [fetchProfile]);

    return {
        user,
        loading,
        error,
        fetchProfile
    };
};

export default useProfile;