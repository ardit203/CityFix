import {useCallback} from "react";
import useActionHelper from "../../../common/hooks/useActionHelper.js";
import requestCommentService from "../../services/requestCommentService.js";

const useRequestCommentActions = () => {
    const {executeAction, isExecuting} = useActionHelper();

    const createComment = useCallback(async (requestId, data, onSuccess = null) => {
        return executeAction({
            successMessage: "Comment added successfully!",
            actionFn: () => requestCommentService.create(requestId, data),
            onSuccess
        });
    }, [executeAction]);

    const updateComment = useCallback(async (requestId, commentId, data, onSuccess = null) => {
        return executeAction({
            successMessage: "Comment updated successfully!",
            actionFn: () => requestCommentService.update(requestId, commentId, data),
            onSuccess
        });
    }, [executeAction]);

    const deleteComment = useCallback(async (requestId, commentId, onSuccess = null) => {
        return executeAction({
            confirmDialog: {title: "Delete Comment?", message: "Are you sure you want to delete this comment?"},
            successMessage: "Comment deleted successfully!",
            actionFn: () => requestCommentService.deleteById(requestId, commentId),
            onSuccess
        });
    }, [executeAction]);

    return {
        createComment,
        updateComment,
        deleteComment,
        isExecuting
    };
};

export default useRequestCommentActions;
