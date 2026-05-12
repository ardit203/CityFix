import axiosInstance from "../../common/axios/axios.js";

const requestAiSuggestionService = {
    findSuggestion: async (requestId) => {
        const response = await axiosInstance.get(`/requests/${requestId}/ai-suggestion`);
        return response.data;
    },

    processSuggestion: async (requestId, payload) => {
        return await axiosInstance.post(`/requests/${requestId}/ai-suggestion/process`, payload);
    },

    rejectSuggestion: async (requestId, payload) => {
        return await axiosInstance.post(`/requests/${requestId}/ai-suggestion/reject`, payload);
    }
};

export default requestAiSuggestionService;
