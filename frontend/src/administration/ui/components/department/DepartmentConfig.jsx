// Import your formatters!
import {renderTruncatedText} from "../../../../common/utils/tableFormatters.jsx";

export const initialDepartmentFilters = {
    page: 0,
    size: 10,
    sortBy: "id",
    sortDir: "asc", // Added this so it has a default!
    id: "",
    text: ""
};

export const departmentColumns = [
    {id: 'id', label: 'ID'},
    {id: 'name', label: 'Department Name'},
    {id: 'description', label: 'Description', render: (row) => renderTruncatedText(row.description, 55)}
];

export const departmentSortOptions = [
    {value: 'id', label: 'ID'},
    {value: 'name', label: 'Department Name'},
    {value: 'createdAt', label: 'Date Created'}
];
