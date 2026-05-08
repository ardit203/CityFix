import axiosInstance from "../../common/axios/axios.js";

const userService = {
    // Admin: paged/filterable users
    findAllPaged: async ({
                             page = 0,
                             size = 10,
                             sortBy = "id",
                             sortDir = "asc",
                             id = null,
                             username = null,
                             email = null,
                             role = null
                         }) => {
        return await axiosInstance.get("/users/paged", {
            params: {
                page,
                size,
                sortBy,
                sortDir,
                id,
                username,
                email,
                role
            }
        });
    },

    // Admin: basic users list
    findAll: async () => {
        return await axiosInstance.get("/users");
    },

    // Current logged-in user
    findMe: async () => {
        return await axiosInstance.get("/users/me");
    },

    // Admin: find user by id
    findById: async (id) => {
        return await axiosInstance.get(`/users/${id}`);
    },

    // Admin: delete user
    deleteById: async (id) => {
        return await axiosInstance.delete(`/users/${id}`);
    },

    // Admin: update user
    update: async (id, data) => {
        return await axiosInstance.patch(`/users/${id}`, data);
    },

    // Admin: lock user
    lock: async (id, until) => {
        return await axiosInstance.patch(`/users/${id}/lock`, null, {
            params: {
                until
            }
        });
    },

    // Admin: unlock user
    unlock: async (id) => {
        return await axiosInstance.patch(`/users/${id}/unlock`);
    },

    // Admin: change user role
    changeRole: async (id, role) => {
        return await axiosInstance.patch(`/users/${id}/role`, null, {
            params: {
                role
            }
        });
    },

    // Self-services: update own account
    updateMyAccount: async (data) => {
        return await axiosInstance.patch("/users/me/account", data);
    },

    // Self-services: update own profile
    updateMyProfile: async (data) => {
        return await axiosInstance.patch("/users/me/profile", data);
    },

    // Self-services: change own password
    changeMyPassword: async (data) => {
        return await axiosInstance.patch("/users/me/password", data);
    },

    // Self-services: update own profile picture
    updateMyProfilePicture: async (file) => {
        const formData = new FormData();
        formData.append("file", file);

        return await axiosInstance.patch("/users/me/profile-picture", formData, {
            headers: {
                "Content-Type": "multipart/form-data"
            }
        });
    },

    // Self-services: delete own profile picture
    deleteMyProfilePicture: async () => {
        return await axiosInstance.delete("/users/me/profile-picture");
    }
};

export default userService;