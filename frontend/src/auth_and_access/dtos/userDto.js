/**
 * Initial empty state for creating or editing a User in frontend forms (Admin side).
 * Matches the backend: AdminUpdateUserDto
 */
export const emptyAdminUpdateUserDto = {
    username: "",
    email: "",
    notificationsEnabled: true,
    name: "",
    surname: "",
    dateOfBirth: "",
    gender: "",
    phoneNumber: "",
    address: {
        street: "",
        city: "",
        postalCode: ""
    }
};

/**
 * Maps raw form data into the exact structure expected by the backend for admin updates.
 * Matches the backend: AdminUpdateUserDto
 */
export const mapToAdminUpdateUserDto = (formData) => {
    return {
        username: formData.username?.trim() || "",
        email: formData.email?.trim() || "",
        notificationsEnabled: formData.notificationsEnabled ?? true,
        name: formData.name?.trim() || "",
        surname: formData.surname?.trim() || "",
        dateOfBirth: formData.dateOfBirth || null,
        gender: formData.gender || null,
        phoneNumber: formData.phoneNumber?.trim() || null,
        address: formData.address ? {
            street: formData.address.street?.trim() || "",
            city: formData.address.city?.trim() || "",
            postalCode: formData.address.postalCode?.trim() || ""
        } : null
    };
};

/**
 * Maps the basic backend response (often embedded in other DTOs) to a clean UI object.
 * Matches the backend: DisplayUserBasicDto
 */
export const mapToDisplayUserBasicDto = (backendData) => {
    if (!backendData) return null;

    return {
        id: backendData.id,
        username: backendData.username,
        email: backendData.email,
        role: backendData.role?.replace("ROLE_", "") || "USER"
    };
};

/**
 * Maps the highly detailed backend response to a clean UI object.
 * Matches the backend: DisplayUserDto
 */
export const mapToDisplayUserDto = (backendData) => {
    if (!backendData) return null;

    return {
        id: backendData.id,
        username: backendData.username,
        email: backendData.email,
        role: backendData.role || "USER",
        notificationsEnabled: backendData.notificationsEnabled ?? false,
        locked: backendData.locked ?? false,
        
        profile: backendData.profile ? {
            name: backendData.profile.name,
            surname: backendData.profile.surname,
            dateOfBirth: backendData.profile.dateOfBirth,
            gender: backendData.profile.gender,
            phoneNumber: backendData.profile.phoneNumber,
            profilePictureUrl: backendData.profile.profilePictureUrl,
            
            address: backendData.profile.address ? {
                street: backendData.profile.address.street,
                city: backendData.profile.address.city,
                postalCode: backendData.profile.address.postalCode
            } : null
        } : null
    };
};

/**
 * Maps the paginated backend response (used in Grids/Tables).
 * Matches the backend: DisplayUserPageableDto
 */
export const mapToDisplayUserPageableDto = (backendData) => {
    if (!backendData) return null;

    return {
        id: backendData.id,
        username: backendData.username,
        role: backendData.role || "USER",
        name: backendData.name,
        surname: backendData.surname,
        
        // Convenience field for grids
        fullName: `${backendData.name || ""} ${backendData.surname || ""}`.trim() || "N/A"
    };
};

/**
 * Utility function to map an entire array of paginated results for the grid in one go.
 */
export const mapUserPageableList = (contentArray) => {
    if (!Array.isArray(contentArray)) return [];
    return contentArray.map(mapToDisplayUserPageableDto);
};

export const mapDisplayToAdminUpdateUserDto = (displayDto) => {
    if (!displayDto) return emptyAdminUpdateUserDto;
    return {
        username: displayDto.username || "",
        email: displayDto.email || "",
        notificationsEnabled: displayDto.notificationsEnabled ?? true,
        name: displayDto.profile?.name || "",
        surname: displayDto.profile?.surname || "",
        dateOfBirth: displayDto.profile?.dateOfBirth || "",
        gender: displayDto.profile?.gender || "",
        phoneNumber: displayDto.profile?.phoneNumber || "",
        address: {
            street: displayDto.profile?.address?.street || "",
            city: displayDto.profile?.address?.city || "",
            postalCode: displayDto.profile?.address?.postalCode || ""
        }
    };
};
