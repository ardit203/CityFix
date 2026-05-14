import axiosInstance from "../../common/axios/axios.js";
import {
    mapRequestAssignmentList,
    mapToCreateRequestAssignmentDto,
    mapToDisplayRequestAssignmentDto
} from "../dtos/requestAssignmentDto.js";

const requestAssignmentService = {
    findAllByRequest: async (requestId) => {
        const response = await axiosInstance.get(`/requests/${requestId}/assignments`);
        response.data = mapRequestAssignmentList(response.data);
        return response;
    },

    findAllPaged: async (filters) => {
        const response = await axiosInstance.get("/request-assignments/paged", {
            params: filters
        });
        response.data.content = mapRequestAssignmentList(response.data.content);
        return response;
    },

    findById: async (assignmentId) => {
        const response = await axiosInstance.get(`/request-assignments/${assignmentId}`);
        response.data = mapToDisplayRequestAssignmentDto(response.data);
        return response;
    },

    assignEmployee: async (requestId, data) => {
        const response = await axiosInstance.post(
            `/requests/${requestId}/assignments`,
            mapToCreateRequestAssignmentDto(data)
        );
        response.data = mapToDisplayRequestAssignmentDto(response.data);
        return response;
    },

    removeAssignment: async (requestId, assignmentId) => {
        return await axiosInstance.delete(`/requests/${requestId}/assignments/${assignmentId}`);
    },

    removeAllAssignments: async (requestId) => {
        return await axiosInstance.delete(`/requests/${requestId}/assignments`);
    }
};

export default requestAssignmentService;
