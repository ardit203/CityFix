export const emptyRequestFilter = {
    page: 0,
    size: 10,
    sortBy: "id",
    sortDir: "asc",

    id: "",
    citizenId: "",
    departmentId: "",
    categoryId: "",
    municipalityId: "",
    assignedEmployeeUserId: "",
    status: "",
    routingStatus: "",
    priority: "",
    text: "",
    submittedFrom: "",
    submittedTo: ""
};

export const mapRequestFilterForApi = (filterState) => {
    // Remove empty/default values before sending to the backend
    const apiFilter = {};

    // Always include pagination and sorting if valid
    if (filterState.page !== undefined) apiFilter.page = filterState.page;
    if (filterState.size !== undefined) apiFilter.size = filterState.size;
    if (filterState.sortBy) apiFilter.sortBy = filterState.sortBy;
    if (filterState.sortDir) apiFilter.sortDir = filterState.sortDir;

    // Conditionally include other fields if they have a value
    if (filterState.id) apiFilter.id = filterState.id;
    if (filterState.citizenId) apiFilter.citizenId = filterState.citizenId;
    if (filterState.departmentId) apiFilter.departmentId = filterState.departmentId;
    if (filterState.categoryId) apiFilter.categoryId = filterState.categoryId;
    if (filterState.municipalityId) apiFilter.municipalityId = filterState.municipalityId;
    if (filterState.assignedEmployeeUserId) apiFilter.assignedEmployeeUserId = filterState.assignedEmployeeUserId;
    if (filterState.status) apiFilter.status = filterState.status;
    if (filterState.routingStatus) apiFilter.routingStatus = filterState.routingStatus;
    if (filterState.priority) apiFilter.priority = filterState.priority;
    if (filterState.text) apiFilter.text = filterState.text;
    if (filterState.submittedFrom) apiFilter.submittedFrom = filterState.submittedFrom;
    if (filterState.submittedTo) apiFilter.submittedTo = filterState.submittedTo;

    return apiFilter;
};

export const emptyRequestLogFilters = {
    ...emptyRequestFilter,
    id: '',
    action: '',
    text: '',
    createdFrom: '',
    createdTo: ''
}
