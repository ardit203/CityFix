export const initialUserFilters = {
    page: 0,
    size: 10,
    sortBy: "id",
    sortDir: "asc",
    id: "",
    username: "",
    email: "",
    role: ""
};

export const userColumns = [
    {id: 'id', label: 'ID'},
    {id: 'name', label: 'Name'},
    {id: 'surname', label: 'Surname'},
    {id: 'username', label: 'Username'},
    {id: 'role', label: 'Role'}
];

export const userSortOptions = [
    {value: 'id', label: 'ID'},
    {value: 'name', label: 'Name'},
    {value: 'username', label: 'Username'},
];
