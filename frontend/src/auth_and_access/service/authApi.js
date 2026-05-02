import apiCall from "../../common/axios/axios.js";

const authApi = {
    register: async (data) => {
        return await apiCall.post("/auth/register", data);
    },
    login: async (data) => {
        return await apiCall.post("/auth/login", data);
    },
};

export default authApi;