/**
 * Initial empty state for creating or editing a Request in frontend forms.
 * Matches the backend: CreateRequestDto
 */
export const emptyCreateRequestDto = {
    title: "",
    description: "",
    municipalityId: "",
    requestLocationDto: {
        latitude: "",
        longitude: ""
    },
    files: []
};

/**
 * Maps raw form data into the exact structure expected by the backend.
 * Ensures data is cleanly formatted (e.g., trimming whitespace, converting empty strings to null).
 * Matches the backend: CreateRequestDto
 */
export const mapToCreateRequestDto = (formData) => {
    return {
        title: formData.title?.trim() || "",
        description: formData.description?.trim() || "",
        municipalityId: formData.municipalityId || null,
        requestLocationDto: {
            latitude: formData.requestLocationDto?.latitude || null,
            longitude: formData.requestLocationDto?.longitude || null,
        }
    };
};

/**
 * Maps the basic backend response to a clean UI object.
 * Matches the backend: DisplayBasicRequestDto
 */
export const mapToDisplayBasicRequestDto = (backendData) => {
    if (!backendData) return null;

    return {
        id: backendData.id,
        title: backendData.title,
        status: backendData.status || null,
        userId: backendData.userId || null,
        categoryId: backendData.categoryId || null,
        municipalityId: backendData.municipalityId || null,
        departmentId: backendData.departmentId || null,
    };
};

/**
 * Maps the highly detailed backend response to a clean UI object.
 * Matches the backend: DisplayRequestDto
 */
export const mapToDisplayRequestDto = (backendData) => {
    if (!backendData) return null;

    return {
        id: backendData.id,
        title: backendData.title,
        description: backendData.description || "No description provided",
        location: backendData.location ? {
            latitude: backendData.location.latitude,
            longitude: backendData.location.longitude,
        } : null,
        summary: backendData.summary || "",
        priority: backendData.priority || null,
        status: backendData.status || null,
        routingStatus: backendData.routingStatus || null,
        userId: backendData.userId || null,
        categoryId: backendData.categoryId || null,
        departmentId: backendData.departmentId || null,
        municipalityId: backendData.municipalityId || null,
    };
};

/**
 * Maps the paginated backend response (used in your Grids/Tables).
 * Matches the backend: DisplayRequestPageableDto
 */
export const mapToDisplayRequestPageableDto = (backendData) => {
    if (!backendData) return null;

    return {
        id: backendData.id,
        title: backendData.title || "",
        priority: backendData.priority || null,
        status: backendData.status || null,
        routingStatus: backendData.routingStatus || null,
        municipalityCode: backendData.municipalityCode || "Unassigned",
        categoryName: backendData.categoryName || "Unassigned",
        departmentName: backendData.departmentName || "Unassigned",
    };
};

/**
 * Utility function to map an entire array of paginated results for the grid in one go.
 */
export const mapRequestPageableList = (contentArray) => {
    if (!Array.isArray(contentArray)) return [];
    return contentArray.map(mapToDisplayRequestPageableDto);
};

export const mapBasicRequestList = (contentArray) => {
    if (!Array.isArray(contentArray)) return [];
    return contentArray.map(mapToDisplayBasicRequestDto);
};

/**
 * Maps a DisplayRequestDto back into the form structure so it can be edited.
 * This prevents React components from having to manually un-nest objects.
 */
export const mapDisplayToFormRequestDto = (displayDto) => {
    if (!displayDto) return emptyCreateRequestDto;

    return {
        title: displayDto.title || "",
        description: displayDto.description || "",
        municipalityId: displayDto.municipalityId || "",
        requestLocationDto: {
            latitude: displayDto.location?.latitude || "",
            longitude: displayDto.location?.longitude || ""
        }
    };
};
