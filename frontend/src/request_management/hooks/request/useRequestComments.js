import {useCallback, useEffect, useState} from "react";
import useAsyncState from "../../../common/hooks/useAsyncState.js";
import requestCommentService from "../../services/requestCommentService.js";

const useRequestComments = (requestId) => {
    const [comments, setComments] = useState([]);
    const {loading, error, handleError, startAsync, finishAsync} = useAsyncState();

    const fetchComments = useCallback(async () => {
        if (!requestId) return;

        startAsync();
        try {
            const response = await requestCommentService.findAllByRequest(requestId);
            setComments(response.data);
        } catch (error) {
            handleError(error);
        } finally {
            finishAsync();
        }
    }, [requestId, startAsync, finishAsync, handleError]);

    useEffect(() => {
        void fetchComments();
    }, [fetchComments]);

    return {comments, loading, error, fetchComments};
};

export default useRequestComments;
