import React from 'react';
import {useNavigate} from "react-router";
import {Box, Button, Card, CardActions, CardContent, Typography} from "@mui/material";
import InfoIcon from "@mui/icons-material/Info";
import EditIcon from "@mui/icons-material/Edit";
import DeleteIcon from "@mui/icons-material/Delete";
import useCategoryActions from "../../../hooks/category/useCategoryActions.js";

const CategoryCard = ({category, onDelete}) => {
    const navigate = useNavigate();

    const {deleteCategory} = useCategoryActions()

    const handleDelete = async () => {
        await deleteCategory(category.id, onDelete);
    };

    return (
        <Card sx={{maxWidth: 300}}>
            <CardContent>
                <Typography variant="h5">
                    {category.name}
                </Typography>

                <Typography variant="subtitle1">
                    {category.departmentName}
                </Typography>
            </CardContent>

            <CardActions sx={{justifyContent: "space-between"}}>
                <Button
                    startIcon={<InfoIcon/>}
                    onClick={() => navigate(`/categories/${category.id}`)}
                >
                    Info
                </Button>

                <Box>
                    <Button
                        startIcon={<EditIcon/>}
                        onClick={() => navigate(`/categories/${category.id}/edit`)}
                        color="warning"
                    >
                        Edit
                    </Button>

                    <Button
                        startIcon={<DeleteIcon/>}
                        onClick={handleDelete}
                        color="error"
                    >
                        Delete
                    </Button>
                </Box>
            </CardActions>
        </Card>
    );
};

export default CategoryCard;
