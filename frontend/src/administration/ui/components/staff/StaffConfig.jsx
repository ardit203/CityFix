export const initialStaffFilters = {
    page: 0,
    size: 10,
    sortBy: "id",
    sortDir: "asc",
    id: "",
    userId:"",
    departmentId:"",
    municipalityId: "",
    username:"",
    municipalityCode:"",
    municipalityName:"",
};

export const staffColumns = [
    {id: 'id', label: 'ID'},
    {id: 'name', label: 'Name'},
    {id: 'surname', label: 'Surname'},
    {id: 'username', label: 'Username'},
    {id: 'departmentName', label: 'Department'},
    {id: 'municipalityCode', label: 'Municipality'},
];

export const staffSortOptions = [
    {value: 'id', label: 'ID'},
    {value: 'name', label: 'Name'},
    {value: 'username', label: 'Username'},
    {value: 'departmentName', label: 'Department'},
    {value: 'municipality', label: 'Municipality'},
];
