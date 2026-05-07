import React from 'react';
import {Grid} from "@mui/material";
import MunicipalityCard from "./MunicipalityCard.jsx";

const MunicipalityGrid = ({ municipalities, onDelete }) => {
    return (
        <Grid container spacing={{ xs: 2, md: 3 }}>
            {municipalities.map((municipality) => (
                <Grid key={municipality.id} size={{ xs: 12, sm: 6, md: 4, lg: 3 }}>
                    <MunicipalityCard municipality={municipality} onDelete={onDelete}/>
                </Grid>
            ))}
        </Grid>
    );
};

export default MunicipalityGrid;
