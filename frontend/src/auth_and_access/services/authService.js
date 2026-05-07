import axiosInstance from "../../common/axios/axios.js";

const authService = {
    register: async (data) => {
        return await axiosInstance.post("/auth/register", data);
    },
    login: async (data) => {
        return await axiosInstance.post("/auth/login", data);
    },
    requestPasswordReset: async (email) => {
        return await axiosInstance.post("/auth/forgot-password", null, {
            params: { email }
        });
    },
    resetPassword: async (data) => {
        return await axiosInstance.post("/auth/reset-password", data);
    }
};

export default authService;