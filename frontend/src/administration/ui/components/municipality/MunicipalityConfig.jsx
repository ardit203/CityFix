export const initialMunicipalityFilters = {
    page: 0,
    size: 10,
    sortBy: "id",
    sortDir: "asc",
    id: "",
    code: "",
    name: ""
};

export const municipalityColumns = [
    {id: 'id', label: 'ID'},
    {id: 'name', label: 'Municipality Name'},
    {id: 'code', label: 'Code'}
];

export const municipalitySortOptions = [
    {value: 'id', label: 'ID'},
    {value: 'name', label: 'Municipality Name'},
    {value: 'code', label: 'Code'},
];
