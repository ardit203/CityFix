import React from 'react';
import {Grid} from "@mui/material";
import MunicipalityCard from "./MunicipalityCard.jsx";

const MunicipalityGrid = ({ municipalities, onDelete }) => {
    return (
        <Grid container spacing={{ xs: 2, md: 2.5 }} className="entity-grid">
            {municipalities.map((municipality) => (
                <Grid key={municipality.id} size={{ xs: 12, sm: 6, lg: 4, xl: 3 }}>
                    <MunicipalityCard municipality={municipality} onDelete={onDelete}/>
                </Grid>
            ))}
        </Grid>
    );
};

export default MunicipalityGrid;
