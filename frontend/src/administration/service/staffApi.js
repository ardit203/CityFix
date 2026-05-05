import apiCall from "../../common/axios/axios.js";

const staffApi = {
    findAll: async () => {
        return await apiCall.get("/staff");
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
        return await apiCall.get("/staff/paged", {
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
        return await apiCall.get("/staff/detailed");
    },

    findById: async (id) => {
        return await apiCall.get(`/staff/${id}`);
    },

    findByUserId: async (userId) => {
        return await apiCall.get(`/staff/by-user/${userId}`);
    },

    findByDepartmentId: async (departmentId) => {
        return await apiCall.get(`/staff/by-department/${departmentId}`);
    },

    findByMunicipalityId: async (municipalityId) => {
        return await apiCall.get(`/staff/by-municipality/${municipalityId}`);
    },

    findUsersAvailableForStaff: async () => {
        return await apiCall.get("/staff/available-users");
    },

    create: async (data) => {
        return await apiCall.post("/staff", data);
    },

    update: async (id, data) => {
        return await apiCall.put(`/staff/${id}`, data);
    },

    deleteById: async (id) => {
        return await apiCall.delete(`/staff/${id}`);
    }
};

export default staffApi;