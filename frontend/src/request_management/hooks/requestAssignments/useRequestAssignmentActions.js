import {useCallback} from "react";
import useActionHelper from "../../../common/hooks/useActionHelper.js";
import requestAssignmentService from "../../services/requestAssignmentService.js";

const useRequestAssignmentActions = () => {
    const {executeAction, isExecuting} = useActionHelper();

    const assignEmployee = useCallback(async (requestId, data, onSuccess = null) => {
        return executeAction({
            successMessage: "Request assigned successfully!",
            actionFn: () => requestAssignmentService.assignEmployee(requestId, data),
            onSuccess
        });
    }, [executeAction]);

    const removeAssignment = useCallback(async (requestId, assignmentId, onSuccess = null) => {
        return executeAction({
            confirmDialog: {
                title: "Remove Assignee?",
                message: "This will remove the employee from this request."
            },
            successMessage: "Assignee removed successfully!",
            actionFn: () => requestAssignmentService.removeAssignment(requestId, assignmentId),
            onSuccess
        });
    }, [executeAction]);

    const removeAllAssignments = useCallback(async (requestId, onSuccess = null) => {
        return executeAction({
            confirmDialog: {
                title: "Remove All Assignees?",
                message: "This will remove every employee assigned to this request."
            },
            successMessage: "Assignees removed successfully!",
            actionFn: () => requestAssignmentService.removeAllAssignments(requestId),
            onSuccess
        });
    }, [executeAction]);

    return {
        assignEmployee,
        removeAssignment,
        removeAllAssignments,
        isExecuting
    };
};

export default useRequestAssignmentActions;
