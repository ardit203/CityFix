import React from "react";
import { useNavigate, useParams } from "react-router";
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
    Badge,
    Business,
    Email,
    LocationCity,
    Person,
    Engineering as StaffIcon,
    Description,
    Code
} from "@mui/icons-material";

// IMPORTANT: Adjust these paths to point to your actual hooks/components!
import useStaffActions from "../../../hooks/staff/useStaffActions.js";
import useStaffDetails from "../../../hooks/staff/useStaffDetails.js";
import AsyncDataView from "../../../../common/ui/components/AsyncDataView.jsx";
import ActionBar from "../../../../common/ui/components/ActionBar.jsx";

const StaffDetailsPage = () => {
    const { id } = useParams();
    const navigate = useNavigate();

    const { staff, loading, error } = useStaffDetails(id);
    const { deleteStaff } = useStaffActions();

    const handleDelete = async () => {
        await deleteStaff(staff.id, () => {
            navigate("/staff");
        });
    };

    if (loading || error || !staff) {
        return (
            <AsyncDataView
                loading={loading}
                error={error}
                data={staff}
                notFoundMessage="Staff member not found."
                backUrl="/staff"
            />
        );
    }

    return (
        <Box>
            <ActionBar
                onSecondaryBack={() => navigate("/staff")}
                secondaryBackLabel="Back to Staff"
                onEdit={() => navigate(`/staff/${staff.id}/edit`)}
                onDelete={handleDelete}
            />

            <Paper elevation={2} sx={{ p: 4, borderRadius: 4 }}>
                <Grid container spacing={4}>
                    <Grid item xs={12}>
                        <Stack direction="row" spacing={2} alignItems="center" sx={{ mb: 2 }}>
                            <StaffIcon color="primary" sx={{ fontSize: 40 }} />

                            <Box>
                                <Typography variant="h4" fontWeight={600}>
                                    {staff.user?.username}
                                </Typography>

                                <Typography variant="body1" color="text.secondary">
                                    Staff Assignment Details
                                </Typography>
                            </Box>
                        </Stack>

                        <Divider sx={{ mb: 3 }} />

                        <Grid container spacing={3}>

                            {/* 1. User Info */}
                            <Grid item xs={12} md={4}>
                                <Card variant="outlined" sx={{ height: '100%' }}>
                                    <CardContent>
                                        <Typography variant="h6" sx={{ mb: 3 }} color="primary">
                                            User Information
                                        </Typography>

                                        <Stack spacing={2.5}>
                                            <Stack direction="row" spacing={1.5} alignItems="center">
                                                <Badge color="action" />
                                                <Typography>
                                                    Staff ID: {staff.id}
                                                </Typography>
                                            </Stack>

                                            <Stack direction="row" spacing={1.5} alignItems="center">
                                                <Person color="action" />
                                                <Typography>
                                                    Username: @{staff.user?.username}
                                                </Typography>
                                            </Stack>

                                            <Stack direction="row" spacing={1.5} alignItems="center">
                                                <Email color="action" />
                                                <Typography>
                                                    Email: {staff.user?.email}
                                                </Typography>
                                            </Stack>
                                        </Stack>
                                    </CardContent>
                                </Card>
                            </Grid>

                            {/* 2. Department Info */}
                            <Grid item xs={12} md={4}>
                                <Card variant="outlined" sx={{ height: '100%' }}>
                                    <CardContent>
                                        <Typography variant="h6" sx={{ mb: 3 }} color="primary">
                                            Department
                                        </Typography>

                                        {staff.department ? (
                                            <Stack spacing={2.5}>
                                                <Stack direction="row" spacing={1.5} alignItems="center">
                                                    <Badge color="action" />
                                                    <Typography>
                                                        Dep ID: {staff.department.id}
                                                    </Typography>
                                                </Stack>

                                                <Stack direction="row" spacing={1.5} alignItems="center">
                                                    <Business color="action" />
                                                    <Typography>
                                                        Name: {staff.department.name}
                                                    </Typography>
                                                </Stack>

                                                <Stack direction="row" spacing={1.5} alignItems="flex-start">
                                                    <Description color="action" sx={{ mt: 0.5 }} />
                                                    <Typography variant="body2">
                                                        {staff.department.description || "No description provided"}
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

                            {/* 3. Municipality Info */}
                            <Grid item xs={12} md={4}>
                                <Card variant="outlined" sx={{ height: '100%' }}>
                                    <CardContent>
                                        <Typography variant="h6" sx={{ mb: 3 }} color="primary">
                                            Municipality
                                        </Typography>

                                        {staff.municipality ? (
                                            <Stack spacing={2.5}>
                                                <Stack direction="row" spacing={1.5} alignItems="center">
                                                    <Badge color="action" />
                                                    <Typography>
                                                        Mun ID: {staff.municipality.id}
                                                    </Typography>
                                                </Stack>

                                                <Stack direction="row" spacing={1.5} alignItems="center">
                                                    <LocationCity color="action" />
                                                    <Typography>
                                                        Name: {staff.municipality.name}
                                                    </Typography>
                                                </Stack>

                                                <Stack direction="row" spacing={1.5} alignItems="center">
                                                    <Code color="action" />
                                                    <Typography>
                                                        Code: {staff.municipality.code}
                                                    </Typography>
                                                </Stack>
                                            </Stack>
                                        ) : (
                                            <Typography color="text.secondary" fontStyle="italic">
                                                No Municipality Assigned
                                            </Typography>
                                        )}
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

export default StaffDetailsPage;
