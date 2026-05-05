import React from "react";
import { useNavigate } from "react-router";
import {
    Box,
    Button,
    Stack,
    Typography
} from "@mui/material";
import { ArrowBack } from "@mui/icons-material";
import useDepartments from "../../../hooks/useDepartments.js";
import DepartmentForm from "../../components/department/DepartmentForm.jsx";

const DepartmentCreatePage = () => {
    const navigate = useNavigate();

    const { createDepartment } = useDepartments();

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
            <Stack
                direction="row"
                justifyContent="space-between"
                alignItems="center"
                sx={{ mb: 3 }}
            >
                <Button
                    variant="outlined"
                    startIcon={<ArrowBack />}
                    onClick={() => navigate("/departments")}
                >
                    Back to Departments
                </Button>
            </Stack>

            <Typography variant="h4" fontWeight={600} sx={{ mb: 3 }}>
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