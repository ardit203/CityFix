import React from "react";
import {useNavigate, useParams} from "react-router";
import {
    Box,
    Card,
    CardContent,
    Divider,
    Grid,
    Paper,
    Stack,
    Typography
} from "@mui/material";
import {
    Category as CategoryIcon, // A fitting icon for Categories
    Business,
    Description,
    Badge
} from "@mui/icons-material";

// IMPORTANT: Adjust these paths to point to your actual Category hooks!
import useCategoryActions from "../../../hooks/category/useCategoryActions.js";
import useCategoryDetails from "../../../hooks/category/useCategoryDetails.js";

import AsyncDataView from "../../../../common/ui/components/AsyncDataView.jsx";
import ActionBar from "../../../../common/ui/components/ActionBar.jsx";

const CategoryDetailsPage = () => {
    const {id} = useParams();
    const navigate = useNavigate();

    const {category, loading, error} = useCategoryDetails(id);
    const {deleteCategory} = useCategoryActions();

    const handleDelete = async () => {
        await deleteCategory(category.id, () => {
            navigate("/categories");
        });
    };

    if (loading || error || !category) {
        return (
            <AsyncDataView
                loading={loading}
                error={error}
                data={category}
                notFoundMessage="Category not found."
                backUrl="/categories"
            />
        );
    }

    return (
        <Box>
            <ActionBar
                onSecondaryBack={() => navigate("/categories")}
                secondaryBackLabel="Back to Categories"
                onEdit={() => navigate(`/categories/${category.id}/edit`)}
                onDelete={handleDelete}
            />

            <Paper elevation={2} sx={{p: 4, borderRadius: 4}}>
                <Grid container spacing={4}>
                    <Grid size={{xs: 12}}>
                        <Stack direction="row" spacing={2} alignItems="center" sx={{mb: 2}}>
                            {/* We use the CategoryIcon here instead of Business */}
                            <CategoryIcon color="primary" sx={{fontSize: 40}}/>

                            <Box>
                                <Typography variant="h4" fontWeight={600}>
                                    {category.name}
                                </Typography>

                                <Typography variant="body1" color="text.secondary">
                                    Category details
                                </Typography>
                            </Box>
                        </Stack>

                        <Divider sx={{mb: 3}}/>

                        {/* Split into 3 columns for Name, Department, and Description */}
                        <Grid container spacing={2}>

                            {/* 1. Basic Info */}
                            <Grid size={{xs: 12, md: 4}}>
                                <Card variant="outlined" sx={{height: '100%'}}>
                                    <CardContent>
                                        <Typography variant="h6" sx={{mb: 2}}>
                                            Basic Information
                                        </Typography>

                                        <Stack spacing={2}>
                                            <Stack direction="row" spacing={1} alignItems="center">
                                                <Badge color="primary"/>
                                                <Typography>
                                                    ID: {category.id}
                                                </Typography>
                                            </Stack>

                                            <Stack direction="row" spacing={1} alignItems="center">
                                                <CategoryIcon color="primary"/>
                                                <Typography>
                                                    Name: {category.name}
                                                </Typography>
                                            </Stack>
                                        </Stack>
                                    </CardContent>
                                </Card>
                            </Grid>

                            {/* 2. Department Info */}
                            <Grid size={{xs: 12, md: 4}}>
                                <Card variant="outlined" sx={{height: '100%'}}>
                                    <CardContent>
                                        <Typography variant="h6" sx={{mb: 2}}>
                                            Department
                                        </Typography>

                                        {category.department ? (
                                            <Stack spacing={2}>
                                                <Stack direction="row" spacing={1} alignItems="center">
                                                    <Badge color="primary"/>
                                                    <Typography>
                                                        Dep ID: {category.department.id}
                                                    </Typography>
                                                </Stack>

                                                <Stack direction="row" spacing={1} alignItems="center">
                                                    <Business color="primary"/>
                                                    <Typography>
                                                        {category.department.name}
                                                    </Typography>
                                                </Stack>
                                            </Stack>
                                        ) : (
                                            <Typography color="text.secondary" fontStyle="italic">
                                                No Department Assigned
                                            </Typography>
                                        )}
                                    </CardContent>
                                </Card>
                            </Grid>

                            {/* 3. Description */}
                            <Grid size={{xs: 12, md: 4}}>
                                <Card variant="outlined" sx={{height: '100%'}}>
                                    <CardContent>
                                        <Typography variant="h6" sx={{mb: 2}}>
                                            Description
                                        </Typography>

                                        <Stack direction="row" spacing={1} alignItems="flex-start">
                                            <Description color="primary"/>
                                            <Typography>
                                                {category.description || "No description provided"}
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

export default CategoryDetailsPage;
