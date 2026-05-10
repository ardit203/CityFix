import {useCallback, useEffect, useState} from "react";
import usePaginationState from "../../../common/hooks/usePaginationState.js";
import useAsyncState from "../../../common/hooks/useAsyncState.js";
import municipalityService from "../../services/municipalityService.js";
import { mapToCleanQueryParams } from "../../../common/dtos/filterDto.js";


const useMunicipalities = ({paged = true, fetchOnMount = true} = {}) => {
    const [municipalities, setMunicipalities] = useState([]);
    const {loading, error, handleError, startAsync, finishAsync} = useAsyncState();
    const {pagination, updatePagination} = usePaginationState();

    const fetchMunicipalitiesPaged = useCallback(async (filters = {}) => {
        startAsync();
        try {
            const safeFilters = mapToCleanQueryParams(filters);
            const response = await municipalityService.findAllPaged(safeFilters);
            setMunicipalities(response.data.content);
            updatePagination(response.data);
        } catch (error) {
            handleError(error);
        } finally {
            finishAsync();
        }
    }, [startAsync, finishAsync, handleError, updatePagination]);

    const fetchMunicipalitiesAll = useCallback(async () => {
        startAsync();
        try {
            const response = await municipalityService.findAll();
            setMunicipalities(response.data);
        } catch (error) {
            handleError(error);
        } finally {
            finishAsync();
        }
    }, [startAsync, finishAsync, handleError]);

    useEffect(() => {
        if (fetchOnMount) {
            if (paged) {
                void fetchMunicipalitiesPaged();
            } else {
                void fetchMunicipalitiesAll();
            }
        }
    }, [fetchOnMount, paged, fetchMunicipalitiesPaged, fetchMunicipalitiesAll]);

    return {
        municipalities,
        pagination,
        loading,
        error,
        fetchMunicipalitiesPaged,
        fetchMunicipalitiesAll
    };
};

export default useMunicipalities;
