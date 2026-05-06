import {useCallback, useEffect, useState} from "react";
import useAsyncState from "../../common/hooks/useAsyncState.js";
import municipalityService from "../service/municipalityService.js";

const useMunicipalityDetails = (id) => {
    const [municipality, setMunicipality] = useState(null);
    const {loading, error, handleError, startAsync, finishAsync} = useAsyncState();

    const fetchMunicipality = useCallback(async () => {
        if (!id) return;
        startAsync();
        try {
            const response = await municipalityService.findById(id);
            setMunicipality(response.data);
        } catch (error) {
            handleError(error);
        } finally {
            finishAsync();
        }
    }, [id, startAsync, finishAsync, handleError]);

    useEffect(() => {
        void fetchMunicipality();
    }, [fetchMunicipality]);

    return {municipality, loading, error, fetchMunicipality};
};

export default useMunicipalityDetails;