import apiCall from "../../common/axios/axios.js";

const userApi = {
    findAll: async ({
                        page = 0,
                        size = 10,
                        sortBy = "id",
                        id = null,
                        username = null,
                        email = null,
                        role = null
                    }) => {
        return await apiCall.get("/users/paged", {
            params: {
                page,
                size,
                sortBy,
                id,
                username,
                email,
                role
            }
        });
    },
};

export default userApi;