import apiCall from "../../common/axios/axios.js";

const userApi = {
    findAll: async ({
                        page = 0,
                        size = 10,
                        sortBy = "id",
                        id = null,
                        username = null,
                        email = null,
                        role = null
                    }) => {
        return await apiCall.get("/users/paged", {
            params: {
                page,
                size,
                sortBy,
                id,
                username,
                email,
                role
            }
        });
    },
    findMe: async () => {
        return await apiCall.get("/users/me");
    },
    findById: async (id) => {
        return await apiCall.get(`/users/${id}`);
    },
    deleteById: async (id) => {
        return await apiCall.delete(`/users/${id}`);
    },

    update: async (id, data) => {
        return await apiCall.put(`/users/${id}/admin`, data);
    },

    lock: async (id, until) => {
        return await apiCall.patch(`/users/${id}/lock`, null, {
            params: {
                until: until
            }
        });
    },

    unlock: async (id) => {
        return await apiCall.patch(`/users/${id}/unlock`);
    },

    changeRole: async (id, role) => {
        return await apiCall.patch(`/users/${id}/role`, null, {
            params: {
                role
            }
        });
    },
};

export default userApi;