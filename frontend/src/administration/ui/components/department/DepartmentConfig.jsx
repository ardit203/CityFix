import {renderTruncatedText} from "../../../../common/utils/tableFormatters.jsx";


export const departmentColumns = [
    {id: 'id', label: 'ID'},
    {id: 'name', label: 'Department Name'},
    {id: 'description', label: 'Description', render: (row) => renderTruncatedText(row.description, 55)}
];

export const departmentSortOptions = [
    {value: 'id', label: 'ID'},
    {value: 'name', label: 'Department Name'},
];
