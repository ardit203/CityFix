import axiosInstance from "../../common/axios/axios.js";
import {
    mapToCreateMunicipalityDto,
    mapToDisplayMunicipalityDto,
    mapMunicipalityList
} from "../dtos/municipalityDto.js";

const municipalityService = {
    findAll: async () => {
        const response = await axiosInstance.get("/municipalities");
        response.data = mapMunicipalityList(response.data);
        return response;
    },

    findAllPaged: async (filters) => {
        const response = await axiosInstance.get("/municipalities/paged", {
            params: filters
        });
        response.data.content = mapMunicipalityList(response.data.content);
        return response;
    },

    findById: async (id) => {
        const response = await axiosInstance.get(`/municipalities/${id}`);
        response.data = mapToDisplayMunicipalityDto(response.data);
        return response;
    },

    findByName: async (name) => {
        const response = await axiosInstance.get(`/municipalities/by-name/${name}`);
        response.data = mapToDisplayMunicipalityDto(response.data);
        return response;
    },

    findByCode: async (code) => {
        const response = await axiosInstance.get(`/municipalities/by-code/${code}`);
        response.data = mapToDisplayMunicipalityDto(response.data);
        return response;
    },

    create: async (data) => {
        const safeData = mapToCreateMunicipalityDto(data);
        return await axiosInstance.post("/municipalities", safeData);
    },

    update: async (id, data) => {
        const safeData = mapToCreateMunicipalityDto(data);
        return await axiosInstance.put(`/municipalities/${id}`, safeData);
    },

    deleteById: async (id) => {
        return await axiosInstance.delete(`/municipalities/${id}`);
    },

    bulkDelete: async (ids) => {
        return await axiosInstance.delete('/municipalities/bulk', { data: ids });
    }
};

export default municipalityService;