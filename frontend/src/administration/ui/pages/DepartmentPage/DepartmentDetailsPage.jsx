import React from "react";
import { useNavigate, useParams } from "react-router";
import {
    Box,
    Button,
    Card,
    CardContent,
    CircularProgress,
    Divider,
    Grid,
    Paper,
    Stack,
    Typography
} from "@mui/material";
import {
    ArrowBack,
    Delete,
    Edit,
    Business,
    Description,
    Badge
} from "@mui/icons-material";
import useDepartmentsDetails from "../../../hooks/useDepartmentsDetails.js";

const DepartmentDetailsPage = () => {
    const { id } = useParams();
    const { department, loading, error, deleteDepartment } = useDepartmentsDetails(id);
    const navigate = useNavigate();

    const handleDelete = async () => {
        await deleteDepartment();
        // navigate("/departments");
    };

    if (loading) {
        return (
            <Box sx={{ display: "flex", justifyContent: "center", alignItems: "center", minHeight: "60vh" }}>
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
                    onClick={() => navigate("/departments")}
                >
                    Back to Departments
                </Button>

                <Stack direction="row" spacing={2}>
                    <Button
                        variant="contained"
                        color="primary"
                        startIcon={<Edit />}
                        onClick={() => navigate(`/departments/${department.id}/edit`)}
                    >
                        Edit
                    </Button>

                    <Button
                        variant="outlined"
                        color="error"
                        startIcon={<Delete />}
                        onClick={handleDelete}
                    >
                        Delete
                    </Button>
                </Stack>
            </Stack>

            <Paper elevation={2} sx={{ p: 4, borderRadius: 4 }}>
                <Grid container spacing={4}>
                    <Grid size={{ xs: 12 }}>
                        <Stack direction="row" spacing={2} alignItems="center" sx={{ mb: 2 }}>
                            <Business color="primary" sx={{ fontSize: 40 }} />

                            <Box>
                                <Typography variant="h4" fontWeight={600}>
                                    {department.name}
                                </Typography>

                                <Typography variant="body1" color="text.secondary">
                                    Department details
                                </Typography>
                            </Box>
                        </Stack>

                        <Divider sx={{ mb: 3 }} />

                        <Grid container spacing={2}>
                            <Grid size={{ xs: 12, md: 6 }}>
                                <Card variant="outlined">
                                    <CardContent>
                                        <Typography variant="h6" sx={{ mb: 2 }}>
                                            Basic Information
                                        </Typography>

                                        <Stack spacing={2}>
                                            <Stack direction="row" spacing={1} alignItems="center">
                                                <Badge color="primary" />
                                                <Typography>
                                                    ID: {department.id}
                                                </Typography>
                                            </Stack>

                                            <Stack direction="row" spacing={1} alignItems="center">
                                                <Business color="primary" />
                                                <Typography>
                                                    Name: {department.name}
                                                </Typography>
                                            </Stack>
                                        </Stack>
                                    </CardContent>
                                </Card>
                            </Grid>

                            <Grid size={{ xs: 12, md: 6 }}>
                                <Card variant="outlined">
                                    <CardContent>
                                        <Typography variant="h6" sx={{ mb: 2 }}>
                                            Description
                                        </Typography>

                                        <Stack direction="row" spacing={1} alignItems="flex-start">
                                            <Description color="primary" />
                                            <Typography>
                                                {department.description || "No description provided"}
                                            </Typography>
                                        </Stack>
                                    </CardContent>
                                </Card>
                            </Grid>
                        </Grid>
                    </Grid>
                </Grid>
            </Paper>
        </Box>
    );
};

export default DepartmentDetailsPage;