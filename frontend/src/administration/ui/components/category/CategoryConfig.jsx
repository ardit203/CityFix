export const initialCategoryFilters = {
    page: 0,
    size: 10,
    sortBy: "id",
    sortDir: "asc",
    id: "",
    text: "",
    departmentId: ""
};

export const categoryColumns = [
    {id: 'id', label: 'ID'},
    {id: 'name', label: 'Category Name'},
    {id: 'departmentName', label: 'Department'}
];

export const categorySortOptions = [
    {value: 'id', label: 'ID'},
    {value: 'name', label: 'Category Name'},
    {value: 'departmentId', label: 'Department'},
];
