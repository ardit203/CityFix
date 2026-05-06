import React from "react";
import {useNavigate} from "react-router";
import {
    Box,
    Button,
    Stack,
    Typography
} from "@mui/material";
import DepartmentForm from "../../components/department/DepartmentForm.jsx";
import ActionBar from "../../../../common/ui/components/ActionBar.jsx";
import useDepartmentActions from "../../../hooks/useDepartmentActions.js";

const DepartmentCreatePage = () => {
    const navigate = useNavigate();

    const {createDepartment} = useDepartmentActions();

    const handleCreate = async (data) => {
        const createdDepartment = await createDepartment(data);

        if (createdDepartment?.id) {
            navigate(`/departments/${createdDepartment.id}`);
        } else {
            navigate("/departments");
        }
    };

    return (
        <Box>
            <ActionBar
                onSecondaryBack={() => navigate("/departments")}
                secondaryBackLabel="Back to Departments"
            />
            <Typography variant="h4" fontWeight={600} sx={{mb: 3}}>
                Create Department
            </Typography>

            <DepartmentForm
                submitLabel="Create Department"
                onSubmit={handleCreate}
            />
        </Box>
    );
};

export default DepartmentCreatePage;