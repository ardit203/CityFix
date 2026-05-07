import React from 'react';
import {Grid} from "@mui/material";
import StaffCard from "./StaffCard.jsx";

const StaffGrid = ({staff, onDelete}) => {
    return (
        <Grid container spacing={{xs: 2, md: 3}}>
            {staff.map((staff) => (
                <Grid key={staff.id} size={{xs: 12, sm: 6, md: 4, lg: 3}}>
                    <StaffCard staff={staff} onDelete={onDelete}/>
                </Grid>
            ))}
        </Grid>
    );
};

export default StaffGrid;
