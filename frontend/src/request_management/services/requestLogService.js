import axiosInstance from "../../common/axios/axios.js";
import {
    mapRequestLogPageableList,
    mapToDisplayRequestLogBasicDto,
    mapToDisplayRequestLogDto
} from "../dtos/requestLogsDto.js";

const requestLogService = {
    findAllByRequestId: async (requestId) => {
        const response = await axiosInstance.get(`/requests/${requestId}/logs`);
        response.data = mapToDisplayRequestLogBasicDto(response.data);
        return response;
    },

    findAllPaged: async (filters, requestId) => {
        const response = await axiosInstance.get(`/requests/${requestId}/logs/paged`, {
            params: filters
        });
        response.data.content = mapRequestLogPageableList(response.data.content);
        return response;
    },

    findById: async (id, requestId) => {
        const response = await axiosInstance.get(`/requests/${requestId}/logs/${id}`);
        response.data = mapToDisplayRequestLogDto(response.data);
        return response;
    },
};

export default requestLogService;
