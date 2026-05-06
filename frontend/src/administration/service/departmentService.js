import axiosInstance from "../../common/axios/axios.js";

const departmentService = {
    findAll: async () => {
        return await axiosInstance.get("/departments");
    },

    findAllPaged: async ({
                             page = 0,
                             size = 10,
                             sortBy = "id",
                             id = null,
                             text = null
                         }) => {
        return await axiosInstance.get("/departments/paged", {
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
        return await axiosInstance.get(`/departments/${id}`);
    },

    findByName: async (name) => {
        return await axiosInstance.get(`/departments/by-name/${name}`);
    },

    create: async (data) => {
        return await axiosInstance.post("/departments", data);
    },

    update: async (id, data) => {
        return await axiosInstance.put(`/departments/${id}`, data);
    },

    deleteById: async (id) => {
        return await axiosInstance.delete(`/departments/${id}`);
    }
};

export default departmentService;