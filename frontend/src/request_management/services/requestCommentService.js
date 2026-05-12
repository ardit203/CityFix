import axiosInstance from "../../common/axios/axios.js";
import {
    mapRequestCommentList,
    mapToDisplayRequestCommentDto,
    mapToRequestCommentDto
} from "../dtos/requestCommentDto.js";

const requestCommentService = {
    findAllByRequest: async (requestId) => {
        const response = await axiosInstance.get(`/requests/${requestId}/comments`);
        response.data = mapRequestCommentList(response.data);
        return response;
    },

    create: async (requestId, data) => {
        const response = await axiosInstance.post(
            `/requests/${requestId}/comments`,
            mapToRequestCommentDto(data)
        );
        response.data = mapToDisplayRequestCommentDto(response.data);
        return response;
    },

    update: async (requestId, commentId, data) => {
        const response = await axiosInstance.put(
            `/requests/${requestId}/comments/${commentId}`,
            mapToRequestCommentDto(data)
        );
        response.data = mapToDisplayRequestCommentDto(response.data);
        return response;
    },

    deleteById: async (requestId, commentId) => {
        const response = await axiosInstance.delete(`/requests/${requestId}/comments/${commentId}`);
        response.data = mapToDisplayRequestCommentDto(response.data);
        return response;
    }
};

export default requestCommentService;
