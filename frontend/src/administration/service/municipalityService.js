import axiosInstance from "../../common/axios/axios.js";

const municipalityService = {
    findAll: async () => {
        return await axiosInstance.get("/municipalities");
    },

    findAllPaged: async ({
                             page = 0,
                             size = 10,
                             sortBy = "id",
                             id = null,
                             code = null,
                             name = null
                         }) => {
        return await axiosInstance.get("/municipalities/paged", {
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
        return await axiosInstance.get(`/municipalities/${id}`);
    },

    findByCode: async (code) => {
        return await axiosInstance.get(`/municipalities/by-code/${code}`);
    },

    create: async (data) => {
        return await axiosInstance.post("/municipalities", data);
    },

    update: async (id, data) => {
        return await axiosInstance.put(`/municipalities/${id}`, data);
    },

    deleteById: async (id) => {
        return await axiosInstance.delete(`/municipalities/${id}`);
    }
};

export default municipalityService;