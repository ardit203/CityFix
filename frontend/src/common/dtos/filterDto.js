/**
 * Base pagination and sorting structure that matches the backend `FilterDto`.
 * All other filter objects inherit from this.
 */
export const emptyPaginationFilter = {
    page: 0,
    size: 10,
    sortBy: "id",
    sortDir: "asc"
};

/**
 * Utility mapper to clean filters before sending them to the backend via Axios params.
 * This removes any empty strings, nulls, or undefined values so your API URL 
 * stays clean (e.g., prevents sending `?departmentId=&municipalityId=1`).
 */
export const mapToCleanQueryParams = (filterObject) => {
    if (!filterObject) return {};

    const cleanParams = {};
    
    Object.entries(filterObject).forEach(([key, value]) => {
        // Only attach the parameter if it actually has a value
        // We allow 0 because 0 is a valid page number!
        if (value !== null && value !== undefined && value !== "") {
            cleanParams[key] = value;
        }
    });

    return cleanParams;
};
