import React from 'react';
import {Grid} from "@mui/material";
import DepartmentCard from "./DepartmentCard.jsx";

const DepartmentGrid = ({ departments, onDelete }) => {
    return (
        <Grid container spacing={{ xs: 2, md: 3 }}>
            {departments.map((department) => (
                <Grid key={department.id} size={{ xs: 12, sm: 6, md: 4, lg: 3 }}>
                    <DepartmentCard department={department} onDelete={onDelete}/>
                </Grid>
            ))}
        </Grid>
    );
};

export default DepartmentGrid;
