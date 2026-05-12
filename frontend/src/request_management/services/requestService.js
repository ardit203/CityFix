import axiosInstance from "../../common/axios/axios.js";
import {
    mapToCreateRequestDto,
    mapToDisplayRequestDto,
    mapBasicRequestList,
    mapRequestPageableList
} from "../dtos/requestDto.js";
import { mapRequestFilterForApi } from "../dtos/filterDto.js";

const requestService = {
    findAll: async () => {
        const response = await axiosInstance.get("/requests");
        response.data = mapBasicRequestList(response.data);
        return response;
    },

    findAllPaged: async (filters) => {
        const apiFilters = mapRequestFilterForApi(filters);
        const response = await axiosInstance.get("/requests/paged", {
            params: apiFilters
        });
        // Map the content array while preserving pagination info
        response.data.content = mapRequestPageableList(response.data.content);
        return response;
    },

    findById: async (id) => {
        const response = await axiosInstance.get(`/requests/${id}`);
        response.data = mapToDisplayRequestDto(response.data);
        return response;
    },

    create: async (data) => {
        const formData = new FormData();
        const safeData = mapToCreateRequestDto(data);
        
        // Append the DTO as a JSON string
        formData.append('request', new Blob([JSON.stringify(safeData)], {
            type: 'application/json'
        }));
        
        // Append files if they exist
        if (data.files && Array.isArray(data.files)) {
            data.files.forEach(file => {
                formData.append('files', file);
            });
        }

        const response = await axiosInstance.post("/requests", formData, {
            headers: {
                'Content-Type': 'multipart/form-data'
            }
        });
        
        response.data = mapToDisplayRequestDto(response.data);
        return response;
    },

    cancel: async (id) => {
        return await axiosInstance.patch(`/requests/${id}/cancel`);
    },

    changeStatus: async (id, data) => {
        const response = await axiosInstance.patch(`/requests/${id}/status`, data);
        response.data = mapToDisplayRequestDto(response.data);
        return response;
    },

    deleteById: async (id) => {
        return await axiosInstance.delete(`/requests/${id}`);
    },

    bulkDelete: async (ids) => {
        return await axiosInstance.delete("/requests/bulk", { data: ids });
    },

    confirmRouting: async (id) => {
        const response = await axiosInstance.patch(`/requests/${id}/routing/confirm`);
        response.data = mapToDisplayRequestDto(response.data);
        return response;
    },

    rejectRouting: async (id, data) => {
        const response = await axiosInstance.patch(`/requests/${id}/routing/reject`, data);
        response.data = mapToDisplayRequestDto(response.data);
        return response;
    },

    updateRouting: async (id, data) => {
        const response = await axiosInstance.patch(`/requests/${id}/routing`, data);
        response.data = mapToDisplayRequestDto(response.data);
        return response;
    },

    reopenRouting: async (id) => {
        const response = await axiosInstance.patch(`/requests/${id}/routing/reopen`);
        response.data = mapToDisplayRequestDto(response.data);
        return response;
    }
};

export default requestService;
