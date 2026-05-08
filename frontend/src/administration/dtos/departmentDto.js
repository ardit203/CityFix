/**
 * Initial empty state for creating or editing a Department in frontend forms.
 * Matches the backend: CreateDepartmentDto
 */
export const emptyCreateDepartmentDto = {
    name: "",
    description: ""
};

/**
 * Maps raw form data into the exact structure expected by the backend.
 * Ensures data is cleanly formatted (e.g., trimming whitespace, converting empty strings to null).
 * Matches the backend: CreateDepartmentDto
 */
export const mapToCreateDepartmentDto = (formData) => {
    return {
        name: formData.name?.trim() || "",
        description: formData.description?.trim() || null
    };
};

/**
 * Maps the detailed backend response to a clean UI object.
 * Matches the backend: DisplayDepartmentDto
 */
export const mapToDisplayDepartmentDto = (backendData) => {
    if (!backendData) return null;

    return {
        id: backendData.id,
        name: backendData.name,
        description: backendData.description || "No description provided"
    };
};

/**
 * Utility function to map an entire array of results for the grid or dropdowns in one go.
 */
export const mapDepartmentList = (contentArray) => {
    if (!Array.isArray(contentArray)) return [];
    return contentArray.map(mapToDisplayDepartmentDto);
};

/**
 * Maps a DisplayDepartmentDto back into the form structure so it can be edited.
 */
export const mapDisplayToFormDepartmentDto = (displayDto) => {
    if (!displayDto) return emptyCreateDepartmentDto;

    return {
        name: displayDto.name || "",
        description: displayDto.description || ""
    };
};
