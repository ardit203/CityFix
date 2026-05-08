/**
 * Initial empty state for Login form.
 * Matches the backend: LoginUserRequestDto
 */
export const emptyLoginDto = {
    username: "",
    password: ""
};

/**
 * Initial empty state for Register form.
 * Matches the backend: RegisterDto
 */
export const emptyRegisterDto = {
    username: "",
    email: "",
    password: "",
    confirmPassword: "",
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
 * Initial empty state for Change Password form.
 * Matches the backend: ChangePasswordDto
 */
export const emptyChangePasswordDto = {
    currentPassword: "",
    newPassword: "",
    confirmNewPassword: ""
};

/**
 * Maps raw form data into the exact structure expected by the backend for Login.
 */
export const mapToLoginUserRequestDto = (formData) => {
    return {
        username: formData.username?.trim() || "",
        password: formData.password || ""
    };
};

/**
 * Maps raw form data into the exact structure expected by the backend for Register.
 */
export const mapToRegisterDto = (formData) => {
    return {
        username: formData.username?.trim() || "",
        email: formData.email?.trim() || "",
        password: formData.password || "",
        confirmPassword: formData.confirmPassword || "",
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
 * Maps raw form data into the exact structure expected by the backend for UpdateMyAccount.
 * Matches the backend: UpdateMyAccountDto
 */
export const emptyUpdateMyAccountDto = {
    username: "",
    email: "",
    notificationsEnabled: true
};

export const mapToUpdateMyAccountDto = (formData) => {
    return {
        username: formData.username?.trim() || "",
        email: formData.email?.trim() || "",
        notificationsEnabled: formData.notificationsEnabled ?? true
    };
};

/**
 * Maps raw form data into the exact structure expected by the backend for UpdateMyProfile.
 * Matches the backend: UpdateMyProfileDto
 */
export const emptyUpdateMyProfileDto = {
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

export const mapToUpdateMyProfileDto = (formData) => {
    return {
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

export const emptyForgotPasswordDto = {
    email: ""
};

export const emptyResetPasswordDto = {
    token: "",
    newPassword: "",
    confirmNewPassword: ""
};

export const mapToChangePasswordDto = (formData) => {
    return {
        currentPassword: formData.currentPassword || "",
        newPassword: formData.newPassword || "",
        confirmNewPassword: formData.confirmNewPassword || ""
    };
};

export const mapToResetPasswordDto = (formData) => {
    return {
        token: formData.token || "",
        newPassword: formData.newPassword || "",
        confirmNewPassword: formData.confirmNewPassword || ""
    };
};
