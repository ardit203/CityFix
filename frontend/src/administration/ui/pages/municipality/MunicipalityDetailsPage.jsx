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
    Business,
    Code,
    Badge
} from "@mui/icons-material";
import AsyncDataView from "../../../../common/ui/components/AsyncDataView.jsx";
import ActionBar from "../../../../common/ui/components/ActionBar.jsx";
import useMunicipalityDetails from "../../../hooks/municipality/useMunicipalityDetails.js";
import useMunicipalityActions from "../../../hooks/municipality/useMunicipalityActions.js";

const MunicipalityDetailsPage = () => {
    const { id } = useParams();
    const navigate = useNavigate();
    const { municipality, loading, error } = useMunicipalityDetails(id);
    const { deleteMunicipality } = useMunicipalityActions();

    const handleDelete = async () => {
        await deleteMunicipality(municipality.id, () => {
            navigate("/municipalities");
        });
    };

    if (loading || error || !municipality) {
        return (
            <AsyncDataView
                loading={loading}
                error={error}
                data={municipality}
                notFoundMessage="Municipality not found."
                backUrl="/municipalities"
            />
        );
    }

    return (
        <Box>
            <ActionBar
                onSecondaryBack={() => navigate("/municipalities")}
                secondaryBackLabel="Back to Municipalities"
                onEdit={() => navigate(`/municipalities/${municipality.id}/edit`)}
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
                                    {municipality.name}
                                </Typography>

                                <Typography variant="body1" color="text.secondary">
                                    Municipality details
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
                                                    ID: {municipality.id}
                                                </Typography>
                                            </Stack>

                                            <Stack direction="row" spacing={1} alignItems="center">
                                                <Business color="primary" />
                                                <Typography>
                                                    Name: {municipality.name}
                                                </Typography>
                                            </Stack>

                                            <Stack direction="row" spacing={1} alignItems="center">
                                                <Code color="primary" />
                                                <Typography>
                                                    Code: {municipality.code}
                                                </Typography>
                                            </Stack>
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

export default MunicipalityDetailsPage;
