import {useCallback} from "react";

import useActionHelper from "../../../common/hooks/useActionHelper.js";
import municipalityService from "../../services/municipalityService.js";

const useMunicipalityActions = () => {
    const {executeAction, isExecuting} = useActionHelper();

    const createMunicipality = useCallback(async (data, onSuccess = null) => {
        return executeAction({
            successMessage: "Municipality created successfully!",
            actionFn: () => municipalityService.create(data),
            onSuccess: onSuccess
        });
    }, [executeAction]);

    const deleteMunicipality = useCallback(async (id, onSuccess) => {
        return executeAction({
            confirmDialog: {title: "Delete Municipality?", message: "Are you sure you want to delete this?"},
            successMessage: "Municipality deleted successfully!",
            actionFn: () => municipalityService.deleteById(id),
            onSuccess: onSuccess
        });
    }, [executeAction]);

    const updateMunicipality = useCallback(async (id, data, onSuccess) => {
        return executeAction({
            confirmDialog: {title: "Update Municipality?", message: "Are you sure you want to update this?"},
            successMessage: "Municipality updated successfully!",
            actionFn: () => municipalityService.update(id, data),
            onSuccess: onSuccess
        });
    }, [executeAction]);

    const deleteMunicipalitiesBulk = useCallback(async (ids, onSuccess) => {
        return executeAction({
            confirmDialog: { title: "Delete Multiple Municipalities?", message: `Are you sure you want to delete these ${ids.length} municipalities? This action cannot be undone.` },
            successMessage: "Municipalities deleted successfully!",
            actionFn: () => municipalityService.bulkDelete(ids),
            onSuccess: onSuccess
        });
    }, [executeAction]);

    return {createMunicipality, deleteMunicipality, updateMunicipality, deleteMunicipalitiesBulk, isExecuting};
};

export default useMunicipalityActions;
