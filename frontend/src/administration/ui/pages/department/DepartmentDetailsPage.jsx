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

// Import your brand new hooks!

import useDepartmentActions from "../../../hooks/useDepartmentActions.js";
// Import your UI abstractions!
import EmptyState from "../../../../common/ui/components/EmptyState.jsx";
import useDepartmentDetails from "../../../hooks/useDepartmentsDetails.js";
import AsyncDataView from "../../../../common/ui/components/AsyncDataView.jsx";
import ActionBar from "../../../../common/ui/components/ActionBar.jsx";

const DepartmentDetailsPage = () => {
    const { id } = useParams();
    const navigate = useNavigate();

    // ... hooks ...
    const { department, loading, error } = useDepartmentDetails(id);
    const { deleteDepartment } = useDepartmentActions();

    const handleDelete = async () => {
        await deleteDepartment(department.id, () => {
            navigate("/departments");
        });
    };

    // 1. THE FIX: Let AsyncDataView handle all the bad states as an early return!
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

    // 2. If we make it to this line, we GUARANTEE department exists!
    // You don't even need to wrap your UI in AsyncDataView anymore.
    return (
        <Box>
            <ActionBar
                onSecondaryBack={() => navigate("/departments")}
                secondaryBackLabel="Back to Departments"
                onEdit={() => navigate(`/departments/${department.id}/edit`)}
                onDelete={handleDelete}
            >
                {/*
                    Because you asked for {children}, you can add extra
                    buttons here anytime! For example:
                */}
                {/* <Button variant="text">Print Info</Button> */}
            </ActionBar>

            <Paper elevation={2} sx={{ p: 4, borderRadius: 4 }}>
                <Grid container spacing={4}>
                    <Grid size={{ xs: 12 }}>
                        <Stack direction="row" spacing={2} alignItems="center" sx={{ mb: 2 }}>
                            <Business color="primary" sx={{ fontSize: 40 }} />

                            <Box>
                                <Typography variant="h4" fontWeight={600}>
                                    {/* This is now 100% safe! */}
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
