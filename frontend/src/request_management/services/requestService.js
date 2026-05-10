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
        const safeData = mapToCreateRequestDto(data);
        const response = await axiosInstance.post("/requests", safeData);
        response.data = mapToDisplayRequestDto(response.data);
        return response;
    },

    cancel: async (id) => {
        return await axiosInstance.patch(`/requests/${id}/cancel`);
    },

    deleteById: async (id) => {
        return await axiosInstance.delete(`/requests/${id}`);
    },

    bulkDelete: async (ids) => {
        return await axiosInstance.delete("/requests/bulk", { data: ids });
    }
};

export default requestService;
