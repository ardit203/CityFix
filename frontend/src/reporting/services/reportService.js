import axiosInstance from "../../common/axios/axios.js";
import {mapToCleanQueryParams} from "../../common/dtos/filterDto.js";

const reportService = {
    getRequestSummary: async (filters = {}) => {
        return await axiosInstance.get("/reports/requests/summary", {
            params: mapToCleanQueryParams(filters)
        });
    }
};

export default reportService;
