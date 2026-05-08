import axiosInstance from "../../common/axios/axios.js";
import {
    mapToRegisterDto,
    mapToLoginUserRequestDto,
    mapToResetPasswordDto
} from "../dtos/authDto.js";

const authService = {
    register: async (data) => {
        const safeData = mapToRegisterDto(data);
        return await axiosInstance.post("/auth/register", safeData);
    },
    login: async (data) => {
        const safeData = mapToLoginUserRequestDto(data);
        return await axiosInstance.post("/auth/login", safeData);
    },
    requestPasswordReset: async (email) => {
        return await axiosInstance.post("/auth/forgot-password", null, {
            params: { email }
        });
    },
    resetPassword: async (data) => {
        const safeData = mapToResetPasswordDto(data);
        return await axiosInstance.post("/auth/reset-password", safeData);
    }
};

export default authService;