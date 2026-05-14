import React from 'react';
import {Grid} from "@mui/material";
import StaffCard from "./StaffCard.jsx";

const StaffGrid = ({staff, onDelete}) => {
    return (
        <Grid container spacing={{xs: 2, md: 2.5}} className="entity-grid">
            {staff.map((staff) => (
                <Grid key={staff.id} size={{xs: 12, sm: 6, lg: 4, xl: 3}}>
                    <StaffCard staff={staff} onDelete={onDelete}/>
                </Grid>
            ))}
        </Grid>
    );
};

export default StaffGrid;
