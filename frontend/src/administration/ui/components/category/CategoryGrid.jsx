import React from 'react';
import {Grid} from "@mui/material";
import CategoryCard from "./CategoryCard.jsx";

const CategoryGrid = ({categories, onDelete}) => {
    return (
        <Grid container spacing={{xs: 2, md: 2.5}} className="entity-grid">
            {categories.map((category) => (
                <Grid key={category.id} size={{xs: 12, sm: 6, lg: 4, xl: 3}}>
                    <CategoryCard category={category} onDelete={onDelete}/>
                </Grid>
            ))}
        </Grid>
    );
};

export default CategoryGrid;
