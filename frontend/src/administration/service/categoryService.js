import axiosInstance from "../../common/axios/axios.js";

const categoryService = {
    findAll: async () => {
        return await axiosInstance.get("/categories");
    },

    findAllPaged: async ({
                             page = 0,
                             size = 10,
                             sortBy = "id",
                             id = null,
                             text = null,
                             departmentId = null
                         }) => {
        return await axiosInstance.get("/categories/paged", {
            params: {
                page,
                size,
                sortBy,
                id,
                text,
                departmentId
            }
        });
    },

    findById: async (id) => {
        return await axiosInstance.get(`/categories/${id}`);
    },

    findByName: async (name) => {
        return await axiosInstance.get(`/categories/by-name/${name}`);
    },

    findByDepartmentId: async (departmentId) => {
        return await axiosInstance.get(`/categories/by-department/${departmentId}`);
    },

    create: async (data) => {
        return await axiosInstance.post("/categories", data);
    },

    update: async (id, data) => {
        return await axiosInstance.put(`/categories/${id}`, data);
    },

    deleteById: async (id) => {
        return await axiosInstance.delete(`/categories/${id}`);
    }
};

export default categoryService;