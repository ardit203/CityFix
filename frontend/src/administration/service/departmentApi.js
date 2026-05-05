import apiCall from "../../common/axios/axios.js";

const departmentApi = {
    findAll: async () => {
        return await apiCall.get("/departments");
    },

    findAllPaged: async ({
                             page = 0,
                             size = 10,
                             sortBy = "id",
                             id = null,
                             text = null
                         }) => {
        return await apiCall.get("/departments/paged", {
            params: {
                page,
                size,
                sortBy,
                id,
                text
            }
        });
    },

    findById: async (id) => {
        return await apiCall.get(`/departments/${id}`);
    },

    findByName: async (name) => {
        return await apiCall.get(`/departments/by-name/${name}`);
    },

    create: async (data) => {
        return await apiCall.post("/departments", data);
    },

    update: async (id, data) => {
        return await apiCall.put(`/departments/${id}`, data);
    },

    deleteById: async (id) => {
        return await apiCall.delete(`/departments/${id}`);
    }
};

export default departmentApi;