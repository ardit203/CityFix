import axiosInstance from "../../common/axios/axios.js";
import { 
    mapToCreateCategoryDto, 
    mapToDisplayCategoryDto, 
    mapCategoryPageableList 
} from "../dtos/categoryDto.js";

const categoryService = {
    findAll: async () => {
        const response = await axiosInstance.get("/categories");
        response.data = mapCategoryPageableList(response.data);
        return response;
    },

    findAllPaged: async (filters) => {
        const response = await axiosInstance.get("/categories/paged", {
            params: filters
        });
        // Map the content array while preserving pagination info
        response.data.content = mapCategoryPageableList(response.data.content);
        return response;
    },

    findById: async (id) => {
        const response = await axiosInstance.get(`/categories/${id}`);
        response.data = mapToDisplayCategoryDto(response.data);
        return response;
    },

    findByName: async (name) => {
        const response = await axiosInstance.get(`/categories/by-name/${name}`);
        response.data = mapToDisplayCategoryDto(response.data);
        return response;
    },

    findByDepartmentId: async (departmentId) => {
        const response = await axiosInstance.get(`/categories/by-department/${departmentId}`);
        response.data = mapCategoryPageableList(response.data);
        return response;
    },

    create: async (data) => {
        const safeData = mapToCreateCategoryDto(data);
        return await axiosInstance.post("/categories", safeData);
    },

    update: async (id, data) => {
        const safeData = mapToCreateCategoryDto(data);
        return await axiosInstance.put(`/categories/${id}`, safeData);
    },

    deleteById: async (id) => {
        return await axiosInstance.delete(`/categories/${id}`);
    }
};

export default categoryService;