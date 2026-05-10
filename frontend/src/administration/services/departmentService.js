import axiosInstance from "../../common/axios/axios.js";
import {
    mapToCreateDepartmentDto,
    mapToDisplayDepartmentDto,
    mapDepartmentList
} from "../dtos/departmentDto.js";

const departmentService = {
    findAll: async () => {
        const response = await axiosInstance.get("/departments");
        response.data = mapDepartmentList(response.data);
        return response;
    },

    findAllPaged: async (filters) => {
        const response = await axiosInstance.get("/departments/paged", {
            params: filters
        });
        response.data.content = mapDepartmentList(response.data.content);
        return response;
    },

    findById: async (id) => {
        const response = await axiosInstance.get(`/departments/${id}`);
        response.data = mapToDisplayDepartmentDto(response.data);
        return response;
    },

    findByName: async (name) => {
        const response = await axiosInstance.get(`/departments/by-name/${name}`);
        response.data = mapToDisplayDepartmentDto(response.data);
        return response;
    },

    create: async (data) => {
        const safeData = mapToCreateDepartmentDto(data);
        return await axiosInstance.post("/departments", safeData);
    },

    update: async (id, data) => {
        const safeData = mapToCreateDepartmentDto(data);
        return await axiosInstance.put(`/departments/${id}`, safeData);
    },

    deleteById: async (id) => {
        return await axiosInstance.delete(`/departments/${id}`);
    },

    bulkDelete: async (ids) => {
        return await axiosInstance.delete('/departments/bulk', { data: ids });
    }
};

export default departmentService;