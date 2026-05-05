import apiCall from "../../common/axios/axios.js";

const categoryApi = {
    findAll: async () => {
        return await apiCall.get("/categories");
    },

    findAllPaged: async ({
                             page = 0,
                             size = 10,
                             sortBy = "id",
                             id = null,
                             text = null,
                             departmentId = null
                         }) => {
        return await apiCall.get("/categories/paged", {
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
        return await apiCall.get(`/categories/${id}`);
    },

    findByName: async (name) => {
        return await apiCall.get(`/categories/by-name/${name}`);
    },

    findByDepartmentId: async (departmentId) => {
        return await apiCall.get(`/categories/by-department/${departmentId}`);
    },

    create: async (data) => {
        return await apiCall.post("/categories", data);
    },

    update: async (id, data) => {
        return await apiCall.put(`/categories/${id}`, data);
    },

    deleteById: async (id) => {
        return await apiCall.delete(`/categories/${id}`);
    }
};

export default categoryApi;