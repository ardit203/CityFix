import React from "react";
import {useNavigate} from "react-router";
import {
    Box,
    Typography
} from "@mui/material";
import ActionBar from "../../../../common/ui/components/ActionBar.jsx";
import useMunicipalityActions from "../../../hooks/municipality/useMunicipalityActions.js";
import MunicipalityForm from "../../components/municipality/MunicipalityForm.jsx";

const MunicipalityCreatePage = () => {
    const navigate = useNavigate();

    const {createMunicipality} = useMunicipalityActions();

    const handleCreate = async (data) => {
        const createdMunicipality = await createMunicipality(data);

        if (createdMunicipality?.id) {
            navigate(`/municipalities/${createdMunicipality.id}`);
        } else {
            navigate("/municipalities");
        }
    };

    return (
        <Box>
            <ActionBar
                onSecondaryBack={() => navigate("/municipalities")}
                secondaryBackLabel="Back to Municipalities"
            />
            <Typography variant="h4" fontWeight={600} sx={{mb: 3}}>
                Create Municipality
            </Typography>

            <MunicipalityForm
                submitLabel="Create Municipality"
                onSubmit={handleCreate}
            />
        </Box>
    );
};

export default MunicipalityCreatePage;