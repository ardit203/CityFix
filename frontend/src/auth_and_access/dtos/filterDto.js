import { emptyPaginationFilter } from "../../common/dtos/filterDto.js";

/**
 * Initial empty state for User Filters.
 * Matches the backend: UserFilterDto
 */
export const emptyUserFilter = {
    ...emptyPaginationFilter,
    id: "",
    username: "",
    email: "",
    role: ""
};

// Also keeping your access scopes here just in case!
export const emptyStaffScopeFilters = {
    departmentId: "",
    municipalityId: ""
};

export const emptyRequestScopeFilters = {
    requestedUserId: "",
    departmentId: "",
    municipalityId: "",
    assignedEmployeeUserId: ""
};
