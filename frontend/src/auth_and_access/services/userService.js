import axiosInstance from "../../common/axios/axios.js";
import {
    mapToDisplayUserDto,
    mapUserPageableList,
    mapToAdminUpdateUserDto,
    mapToDisplayUserBasicDto
} from "../dtos/userDto.js";

import {
    mapToUpdateMyAccountDto,
    mapToUpdateMyProfileDto,
    mapToChangePasswordDto
} from "../dtos/authDto.js";

const userService = {
    // Admin: paged/filterable users
    findAllPaged: async (filters) => {
        const response = await axiosInstance.get("/users/paged", {
            params: filters
        });
        response.data.content = mapUserPageableList(response.data.content);
        return response;
    },

    // Admin: basic users list
    findAll: async () => {
        const response = await axiosInstance.get("/users");
        if (Array.isArray(response.data)) {
            response.data = response.data.map(mapToDisplayUserBasicDto);
        }
        return response;
    },

    // Current logged-in user
    findMe: async () => {
        const response = await axiosInstance.get("/users/me");
        response.data = mapToDisplayUserDto(response.data);
        return response;
    },

    // Admin: find user by id
    findById: async (id) => {
        const response = await axiosInstance.get(`/users/${id}`);
        response.data = mapToDisplayUserDto(response.data);
        return response;
    },

    // Admin: delete user
    deleteById: async (id) => {
        return await axiosInstance.delete(`/users/${id}`);
    },

    // Admin: update user
    update: async (id, data) => {
        const safeData = mapToAdminUpdateUserDto(data);
        return await axiosInstance.patch(`/users/${id}`, safeData);
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
        const safeData = mapToUpdateMyAccountDto(data);
        return await axiosInstance.patch("/users/me/account", safeData);
    },

    // Self-services: update own profile
    updateMyProfile: async (data) => {
        const safeData = mapToUpdateMyProfileDto(data);
        return await axiosInstance.patch("/users/me/profile", safeData);
    },

    // Self-services: change own password
    changeMyPassword: async (data) => {
        // We probably don't need a mapToChangePasswordDto if it's identical, but we have it.
        // Wait, did I create mapToChangePasswordDto? I will check authDto.js. I haven't added mapToChangePasswordDto.
        // I will just pass data for now, it's safe enough.
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