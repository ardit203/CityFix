import apiCall from "../../common/axios/axios.js";

const municipalityApi = {
    findAll: async () => {
        return await apiCall.get("/municipalities");
    },

    findAllPaged: async ({
                             page = 0,
                             size = 10,
                             sortBy = "id",
                             id = null,
                             code = null,
                             name = null
                         }) => {
        return await apiCall.get("/municipalities/paged", {
            params: {
                page,
                size,
                sortBy,
                id,
                code,
                name
            }
        });
    },

    findById: async (id) => {
        return await apiCall.get(`/municipalities/${id}`);
    },

    findByCode: async (code) => {
        return await apiCall.get(`/municipalities/by-code/${code}`);
    },

    create: async (data) => {
        return await apiCall.post("/municipalities", data);
    },

    update: async (id, data) => {
        return await apiCall.put(`/municipalities/${id}`, data);
    },

    deleteById: async (id) => {
        return await apiCall.delete(`/municipalities/${id}`);
    }
};

export default municipalityApi;