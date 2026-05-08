import { emptyPaginationFilter } from "../../common/dtos/filterDto.js";

/**
 * Initial empty state for Category Filters.
 * Matches the backend: CategoryFilterDto
 */
export const emptyCategoryFilter = {
    ...emptyPaginationFilter,
    id: "",
    text: "",
    departmentId: ""
};

/**
 * Initial empty state for Department Filters.
 * Matches the backend: DepartmentFilterDto
 */
export const emptyDepartmentFilter = {
    ...emptyPaginationFilter,
    id: "",
    text: ""
};

/**
 * Initial empty state for Municipality Filters.
 * Matches the backend: MunicipalityFilterDto
 */
export const emptyMunicipalityFilter = {
    ...emptyPaginationFilter,
    id: "",
    code: "",
    name: ""
};

/**
 * Initial empty state for Staff Filters.
 * Matches the backend: StaffFilterDto
 */
export const emptyStaffFilter = {
    ...emptyPaginationFilter,
    id: "",
    userId: "",
    departmentId: "",
    municipalityId: "",
    username: "",
    municipalityCode: "",
    municipalityName: ""
};
