/**
 * Initial empty state for creating or editing a Staff member in frontend forms.
 * Matches the backend: CreateStaffDto
 */
export const emptyCreateStaffDto = {
    userId: "",
    departmentId: "",
    municipalityId: ""
};

/**
 * Maps raw form data into the exact structure expected by the backend.
 * Matches the backend: CreateStaffDto
 */
export const mapToCreateStaffDto = (formData) => {
    return {
        userId: formData.userId || null,
        departmentId: formData.departmentId || null,
        municipalityId: formData.municipalityId || null
    };
};

/**
 * Maps the basic backend response to a clean UI object.
 * Matches the backend: DisplayBasicStaffDto
 */
export const mapToDisplayBasicStaffDto = (backendData) => {
    if (!backendData) return null;

    return {
        id: backendData.id,
        userId: backendData.userId,
        departmentId: backendData.departmentId,
        municipalityId: backendData.municipalityId
    };
};

/**
 * Maps the highly detailed backend response to a clean UI object.
 * Matches the backend: DisplayStaffDto
 */
export const mapToDisplayStaffDto = (backendData) => {
    if (!backendData) return null;

    return {
        id: backendData.id,
        
        user: backendData.user ? {
            id: backendData.user.id,
            username: backendData.user.username,
            email: backendData.user.email,
            role: backendData.user.role
        } : null,
        
        department: backendData.department ? {
            id: backendData.department.id,
            name: backendData.department.name,
            description: backendData.department.description
        } : null,
        
        municipality: backendData.municipality ? {
            id: backendData.municipality.id,
            name: backendData.municipality.name,
            code: backendData.municipality.code
        } : null
    };
};

/**
 * Maps the paginated backend response (used in Grids/Tables).
 * Matches the backend: DisplayStaffPageableDto
 */
export const mapToDisplayStaffPageableDto = (backendData) => {
    if (!backendData) return null;

    return {
        id: backendData.id,
        userId: backendData.userId,
        name: backendData.name,
        surname: backendData.surname,
        username: backendData.username,
        email: backendData.email,
        role: backendData.role,
        departmentName: backendData.departmentName || "Unassigned",
        municipalityName: backendData.municipalityName || "Unassigned",
        municipalityCode: backendData.municipalityCode || "N/A"
    };
};

/**
 * Utility function to map an entire array of paginated results for the grid in one go.
 */
export const mapStaffPageableList = (contentArray) => {
    if (!Array.isArray(contentArray)) return [];
    return contentArray.map(mapToDisplayStaffPageableDto);
};

export const mapDisplayToFormStaffDto = (displayDto) => {
    if (!displayDto) return emptyCreateStaffDto;
    return {
        userId: displayDto.user?.id || displayDto.userId || "",
        departmentId: displayDto.department?.id || displayDto.departmentId || "",
        municipalityId: displayDto.municipality?.id || displayDto.municipalityId || ""
    };
};
