import React from "react";
import { useNavigate, useParams } from "react-router";
import { Box, Typography } from "@mui/material";

import DepartmentForm from "../../components/department/DepartmentForm.jsx";
import useDepartmentActions from "../../../hooks/department/useDepartmentActions.js";
import AsyncDataView from "../../../../common/ui/components/AsyncDataView.jsx";
import useDepartmentDetails from "../../../hooks/department/useDepartmentsDetails.js";
import ActionBar from "../../../../common/ui/components/ActionBar.jsx";
import { mapDisplayToFormDepartmentDto } from "../../../dtos/departmentDto.js";


const DepartmentEditPage = () => {
    const { id } = useParams();
    const navigate = useNavigate();
    const { department, loading, error } = useDepartmentDetails(id);
    const { updateDepartment } = useDepartmentActions();

    const handleUpdate = async (data) => {
        await updateDepartment(department.id, data, () => {
            navigate(`/departments/${department.id}`);
        });
    };

    if (loading || error || !department) {
        return (
            <AsyncDataView
                loading={loading}
                error={error}
                data={department}
                notFoundMessage="Department not found."
                backUrl="/departments"
            />
        );
    }

    return (
        <Box>
            <ActionBar
                onBack={() => navigate(`/departments/${department.id}`)}
                backLabel="Back to Department"
                onSecondaryBack={() => navigate("/departments")}
                secondaryBackLabel="Back to Departments"
            >

            </ActionBar>

            <Typography variant="h4" fontWeight={600} sx={{ mb: 3 }}>
                Edit Department
            </Typography>

            <DepartmentForm
                initialValues={mapDisplayToFormDepartmentDto(department)}
                submitLabel="Update Department"
                onSubmit={handleUpdate}
            />
        </Box>
    );
};

export default DepartmentEditPage;
