import axiosInstance from "../../common/axios/axios.js";
import {
    mapToCreateStaffDto,
    mapToDisplayStaffDto,
    mapStaffPageableList,
    mapToDisplayBasicStaffDto
} from "../dtos/staffDto.js";

const staffService = {
    findAll: async () => {
        const response = await axiosInstance.get("/staff");
        if (Array.isArray(response.data)) {
            response.data = response.data.map(mapToDisplayBasicStaffDto);
        }
        return response;
    },

    findAllPaged: async (filters) => {
        const response = await axiosInstance.get("/staff/paged", {
            params: filters
        });
        response.data.content = mapStaffPageableList(response.data.content);
        return response;
    },

    findAllDetailed: async () => {
        const response = await axiosInstance.get("/staff/detailed");
        if (Array.isArray(response.data)) {
            response.data = response.data.map(mapToDisplayStaffDto);
        }
        return response;
    },

    findById: async (id) => {
        const response = await axiosInstance.get(`/staff/${id}`);
        response.data = mapToDisplayStaffDto(response.data);
        return response;
    },

    findByUserId: async (userId) => {
        const response = await axiosInstance.get(`/staff/by-user/${userId}`);
        response.data = mapToDisplayStaffDto(response.data);
        return response;
    },

    findByDepartmentId: async (departmentId) => {
        const response = await axiosInstance.get(`/staff/by-department/${departmentId}`);
        if (Array.isArray(response.data)) {
            response.data = response.data.map(mapToDisplayBasicStaffDto);
        }
        return response;
    },

    findByMunicipalityId: async (municipalityId) => {
        const response = await axiosInstance.get(`/staff/by-municipality/${municipalityId}`);
        if (Array.isArray(response.data)) {
            response.data = response.data.map(mapToDisplayBasicStaffDto);
        }
        return response;
    },

    findUsersAvailableForStaff: async () => {
        return await axiosInstance.get("/staff/available-users");
    },

    create: async (data) => {
        const safeData = mapToCreateStaffDto(data);
        return await axiosInstance.post("/staff", safeData);
    },

    importExcel: async (file) => {
        const formData = new FormData();
        formData.append("file", file);

        return await axiosInstance.post("/staff/import/excel", formData, {
            headers: {
                "Content-Type": "multipart/form-data"
            }
        });
    },

    update: async (id, data) => {
        const safeData = mapToCreateStaffDto(data);
        return await axiosInstance.put(`/staff/${id}`, safeData);
    },

    deleteById: async (id) => {
        return await axiosInstance.delete(`/staff/${id}`);
    },

    bulkDelete: async (ids) => {
        return await axiosInstance.delete('/staff/bulk', { data: ids });
    }
};

export default staffService;
