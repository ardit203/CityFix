/**
 * Initial empty state for creating or editing a Category in frontend forms.
 * Matches the backend: CreateCategoryDto
 */
export const emptyCreateCategoryDto = {
    name: "",
    description: "",
    departmentId: ""
};

/**
 * Maps raw form data into the exact structure expected by the backend.
 * Ensures data is cleanly formatted (e.g., trimming whitespace, converting empty strings to null).
 * Matches the backend: CreateCategoryDto
 */
export const mapToCreateCategoryDto = (formData) => {
    return {
        name: formData.name?.trim() || "",
        description: formData.description?.trim() || "",
        departmentId: formData.departmentId || null
    };
};

/**
 * Maps the basic backend response to a clean UI object.
 * Matches the backend: DisplayBasicCategoryDto
 */
export const mapToDisplayBasicCategoryDto = (backendData) => {
    if (!backendData) return null;

    return {
        id: backendData.id,
        name: backendData.name,
        description: backendData.description || "No description provided",
        departmentId: backendData.departmentId
    };
};

/**
 * Maps the highly detailed backend response to a clean UI object.
 * Matches the backend: DisplayCategoryDto
 */
export const mapToDisplayCategoryDto = (backendData) => {
    if (!backendData) return null;

    return {
        id: backendData.id,
        name: backendData.name,
        description: backendData.description || "No description provided",
        
        // Keep the nested object but ensure it's safe to access
        department: backendData.department ? {
            id: backendData.department.id,
            name: backendData.department.name,
            description: backendData.department.description
        } : null,
        
        // Convenience property for UI mapping so you don't have to check if department exists every time
        departmentNameFallback: backendData.department?.name || "No Department Assigned"
    };
};

/**
 * Maps the paginated backend response (used in your Grids/Tables).
 * Matches the backend: DisplayCategoryPageableDto
 */
export const mapToDisplayCategoryPageableDto = (backendData) => {
    if (!backendData) return null;

    return {
        id: backendData.id,
        name: backendData.name,
        departmentId: backendData.departmentId,
        departmentName: backendData.departmentName || "Unassigned"
    };
};

/**
 * Utility function to map an entire array of paginated results for the grid in one go.
 */
export const mapCategoryPageableList = (contentArray) => {
    if (!Array.isArray(contentArray)) return [];
    return contentArray.map(mapToDisplayCategoryPageableDto);
};

/**
 * Maps a DisplayCategoryDto back into the form structure so it can be edited.
 * This prevents React components from having to manually un-nest objects (like department).
 */
export const mapDisplayToFormCategoryDto = (displayDto) => {
    if (!displayDto) return emptyCreateCategoryDto;

    return {
        name: displayDto.name || "",
        description: displayDto.description || "",
        departmentId: displayDto.department?.id || displayDto.departmentId || ""
    };
};
