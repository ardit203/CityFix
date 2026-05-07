import React from "react";
import {useNavigate, useParams} from "react-router";
import {Box, Typography} from "@mui/material";
import AsyncDataView from "../../../../common/ui/components/AsyncDataView.jsx";
import ActionBar from "../../../../common/ui/components/ActionBar.jsx";
import MunicipalityForm from "../../components/municipality/MunicipalityForm.jsx";
import useMunicipalityActions from "../../../hooks/municipality/useMunicipalityActions.js";
import useMunicipalityDetails from "../../../hooks/municipality/useMunicipalityDetails.js";


const MunicipalityEditPage = () => {
    const {id} = useParams();
    const navigate = useNavigate();
    const {municipality, loading, error} = useMunicipalityDetails(id);
    const {updateMunicipality} = useMunicipalityActions();

    const handleUpdate = async (data) => {
        await updateMunicipality(municipality.id, data, () => {
            navigate(`/municipalities/${municipality.id}`);
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
                onBack={() => navigate(`/municipalities/${municipality.id}`)}
                backLabel="Back to Municipality"
                onSecondaryBack={() => navigate("/municipalities")}
                secondaryBackLabel="Back to Municipalities"
            >

            </ActionBar>

            <Typography variant="h4" fontWeight={600} sx={{mb: 3}}>
                Edit Municipality
            </Typography>

            <MunicipalityForm
                initialValues={municipality}
                submitLabel="Update Municipality"
                onSubmit={handleUpdate}
            />
        </Box>
    );
};

export default MunicipalityEditPage;
