import React from "react";
import { useNavigate, useParams } from "react-router";
import { Box, Typography } from "@mui/material";

import DepartmentForm from "../../components/department/DepartmentForm.jsx";

// Import your brand new architecture!

import useDepartmentActions from "../../../hooks/useDepartmentActions.js";
import AsyncDataView from "../../../../common/ui/components/AsyncDataView.jsx";
import useDepartmentDetails from "../../../hooks/useDepartmentsDetails.js";
import ActionBar from "../../../../common/ui/components/ActionBar.jsx"; // Adjust paths if needed


const DepartmentEditPage = () => {
    const { id } = useParams();
    const navigate = useNavigate();

    // 1. Separation of Concerns! Fetching vs Actions
    const { department, loading, error } = useDepartmentDetails(id);
    const { updateDepartment } = useDepartmentActions();

    const handleUpdate = async (data) => {
        // 2. Pass the ID, the Data, and the onSuccess callback!
        await updateDepartment(department.id, data, () => {
            navigate(`/departments/${department.id}`);
        });
    };

    // 3. Let AsyncDataView handle ALL loading and error states as an early return!
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

    // 4. If we reach here, `department` is guaranteed to exist. Render the UI!
    return (
        <Box>
            {/* Use your new Action Bar for the Back button! */}
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
                initialValues={department}
                submitLabel="Update Department"
                onSubmit={handleUpdate}
            />
        </Box>
    );
};

export default DepartmentEditPage;
