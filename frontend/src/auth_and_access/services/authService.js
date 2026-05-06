import axiosInstance from "../../common/axios/axios.js";

const authService = {
    register: async (data) => {
        return await axiosInstance.post("/auth/register", data);
    },
    login: async (data) => {
        return await axiosInstance.post("/auth/login", data);
    },
};

export default authService;