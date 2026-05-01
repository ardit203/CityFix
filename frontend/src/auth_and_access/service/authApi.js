import apiCall from "../../common/axios/axios.js";

const authApi = {
    register: async (data) => {
        return await apiCall.post("/user/register", data);
    },
    login: async (data) => {
        return await apiCall.post("/user/login", data);
    },
};

export default authApi;