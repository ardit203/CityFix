import React from 'react';
import {Grid} from "@mui/material";
import DepartmentCard from "./DepartmentCard.jsx";

const DepartmentGrid = ({departments, onDelete}) => {
    return (
        <Grid container spacing={{xs: 2, md: 2.5}} className="entity-grid">
            {departments.map((department) => (
                <Grid key={department.id} size={{xs: 12, sm: 6, lg: 4, xl: 3}}>
                    <DepartmentCard department={department} onDelete={onDelete}/>
                </Grid>
            ))}
        </Grid>
    );
};

export default DepartmentGrid;
