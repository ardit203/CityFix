import React from "react";
import {Chip} from "@mui/material";

const formatLabel = (value) => {
    if (!value) return "Not set";
    return value
        .replaceAll("_", " ")
        .toLowerCase()
        .replace(/\b\w/g, (char) => char.toUpperCase());
};

const statusColor = (status) => {
    switch (status) {
        case "ASSIGNED": return "secondary";
        case "IN_PROGRESS": return "primary";
        case "RESOLVED": return "success";
        case "REJECTED": return "error";
        case "CANCELED": return "default";
        default: return "warning";
    }
};

export const requestAssignmentColumns = [
    {id: "id", label: "ID"},
    {id: "requestTitle", label: "Request"},
    {
        id: "requestStatus",
        label: "Status",
        render: (assignment) => (
            <Chip
                size="small"
                label={formatLabel(assignment.requestStatus)}
                color={statusColor(assignment.requestStatus)}
            />
        )
    },
    {id: "departmentName", label: "Department"},
    {
        id: "employee",
        label: "Assignee",
        render: (assignment) => {
            const fullName = [assignment.employeeName, assignment.employeeSurname].filter(Boolean).join(" ");
            return fullName || assignment.employeeUsername;
        }
    },
    {id: "assignedByUsername", label: "Assigned By"},
    {
        id: "assignedAt",
        label: "Assigned At",
        render: (assignment) => assignment.assignedAt ? new Date(assignment.assignedAt).toLocaleString() : "Not set"
    }
];

export const requestAssignmentSortOptions = [
    {value: "id", label: "ID"},
    {value: "assignedAt", label: "Assigned At"},
    {value: "requestTitle", label: "Request"},
    {value: "employeeUsername", label: "Assignee"}
];
