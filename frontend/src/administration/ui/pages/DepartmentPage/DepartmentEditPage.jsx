import React from "react";
import { useNavigate, useParams } from "react-router";
import {
    Box,
    Button,
    CircularProgress,
    Stack,
    Typography
} from "@mui/material";
import { ArrowBack } from "@mui/icons-material";
import useDepartmentsDetails from "../../../hooks/useDepartmentsDetails.js";
import DepartmentForm from "../../components/department/DepartmentForm.jsx";

const DepartmentEditPage = () => {
    const { id } = useParams();
    const navigate = useNavigate();

    const {
        department,
        loading,
        error,
        updateDepartment
    } = useDepartmentsDetails(id);

    const handleUpdate = async (data) => {
        await updateDepartment(data);
        // navigate(`/departments/${id}`);
    };

    if (loading) {
        return (
            <Box sx={{ display: "flex", justifyContent: "center", mt: 5 }}>
                <CircularProgress />
            </Box>
        );
    }

    if (error) {
        return (
            <Box>
                <Typography color="error">{error}</Typography>

                <Button
                    sx={{ mt: 2 }}
                    variant="outlined"
                    startIcon={<ArrowBack />}
                    onClick={() => navigate("/departments")}
                >
                    Back to Departments
                </Button>
            </Box>
        );
    }

    if (!department) {
        return (
            <Box>
                <Typography>Department not found.</Typography>

                <Button
                    sx={{ mt: 2 }}
                    variant="outlined"
                    startIcon={<ArrowBack />}
                    onClick={() => navigate("/departments")}
                >
                    Back to Departments
                </Button>
            </Box>
        );
    }

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
                    onClick={() => navigate(`/departments/${department.id}`)}
                >
                    Back to Department
                </Button>
            </Stack>

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