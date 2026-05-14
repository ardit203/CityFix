export const emptyRequestAssignmentFilter = {
    id: "",
    requestId: "",
    employeeId: "",
    assignedByUserId: "",
    departmentId: "",
    municipalityId: "",
    requestStatus: "",
    assignedFrom: "",
    assignedTo: "",
    page: 0,
    size: 10,
    sortBy: "assignedAt",
    sortDir: "desc"
};

export const mapToCreateRequestAssignmentDto = (formData) => ({
    employeeId: formData.employeeId || null
});

export const mapToDisplayRequestAssignmentDto = (backendData) => {
    if (!backendData) return null;

    return {
        id: backendData.id,
        requestId: backendData.requestId,
        requestTitle: backendData.requestTitle,
        requestPriority: backendData.requestPriority,
        requestStatus: backendData.requestStatus,
        departmentId: backendData.departmentId,
        departmentName: backendData.departmentName,
        employeeId: backendData.employeeId,
        employeeName: backendData.employeeName,
        employeeSurname: backendData.employeeSurname,
        employeeUsername: backendData.employeeUsername,
        assignedByUserId: backendData.assignedByUserId ?? backendData.assignedById,
        assignedByName: backendData.assignedByName,
        assignedBySurname: backendData.assignedBySurname,
        assignedByUsername: backendData.assignedByUsername,
        assignedAt: backendData.assignedAt
    };
};

export const mapRequestAssignmentList = (contentArray) => {
    if (!Array.isArray(contentArray)) return [];
    return contentArray.map(mapToDisplayRequestAssignmentDto);
};
