/**
 * Initial empty state for creating or editing a Municipality in frontend forms.
 * Matches the backend: CreateMunicipalityDto
 */
export const emptyCreateMunicipalityDto = {
    name: "",
    code: ""
};

/**
 * Maps raw form data into the exact structure expected by the backend.
 * Matches the backend: CreateMunicipalityDto
 */
export const mapToCreateMunicipalityDto = (formData) => {
    return {
        name: formData.name?.trim() || "",
        code: formData.code?.trim() || ""
    };
};

/**
 * Maps the backend response to a clean UI object.
 * Matches the backend: DisplayMunicipalityDto
 */
export const mapToDisplayMunicipalityDto = (backendData) => {
    if (!backendData) return null;

    return {
        id: backendData.id,
        name: backendData.name,
        code: backendData.code
    };
};

/**
 * Utility function to map an entire array of results for the grid or dropdowns in one go.
 */
export const mapMunicipalityList = (contentArray) => {
    if (!Array.isArray(contentArray)) return [];
    return contentArray.map(mapToDisplayMunicipalityDto);
};

export const mapDisplayToFormMunicipalityDto = (displayDto) => {
    if (!displayDto) return emptyCreateMunicipalityDto;
    return {
        name: displayDto.name || "",
        code: displayDto.code || ""
    };
};
