import axiosInstance from "../../common/axios/axios.js";

const staffService = {
    findAll: async () => {
        return await axiosInstance.get("/staff");
    },

    findAllPaged: async ({
                             page = 0,
                             size = 10,
                             sortBy = "id",
                             id = null,
                             userId = null,
                             departmentId = null,
                             municipalityId = null,
                             username = null,
                             municipalityCode = null,
                             municipalityName = null
                         }) => {
        return await axiosInstance.get("/staff/paged", {
            params: {
                page,
                size,
                sortBy,
                id,
                userId,
                departmentId,
                municipalityId,
                username,
                municipalityCode,
                municipalityName
            }
        });
    },

    findAllDetailed: async () => {
        return await axiosInstance.get("/staff/detailed");
    },

    findById: async (id) => {
        return await axiosInstance.get(`/staff/${id}`);
    },

    findByUserId: async (userId) => {
        return await axiosInstance.get(`/staff/by-user/${userId}`);
    },

    findByDepartmentId: async (departmentId) => {
        return await axiosInstance.get(`/staff/by-department/${departmentId}`);
    },

    findByMunicipalityId: async (municipalityId) => {
        return await axiosInstance.get(`/staff/by-municipality/${municipalityId}`);
    },

    findUsersAvailableForStaff: async () => {
        return await axiosInstance.get("/staff/available-users");
    },

    create: async (data) => {
        return await axiosInstance.post("/staff", data);
    },

    update: async (id, data) => {
        return await axiosInstance.put(`/staff/${id}`, data);
    },

    deleteById: async (id) => {
        return await axiosInstance.delete(`/staff/${id}`);
    }
};

export default staffService;