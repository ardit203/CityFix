import React from 'react';
import {Grid} from "@mui/material";
import CategoryCard from "./CategoryCard.jsx";

const CategoryGrid = ({categories, onDelete}) => {
    return (
        <Grid container spacing={{xs: 2, md: 3}}>
            {categories.map((category) => (
                <Grid key={category.id} size={{xs: 12, sm: 6, md: 4, lg: 3}}>
                    <CategoryCard category={category} onDelete={onDelete}/>
                </Grid>
            ))}
        </Grid>
    );
};

export default CategoryGrid;
