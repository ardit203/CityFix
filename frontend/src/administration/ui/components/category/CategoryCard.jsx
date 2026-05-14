import React from 'react';
import {useNavigate} from "react-router";
import {Box, Button, Card, CardActions, CardContent, Chip, Stack, Typography} from "@mui/material";
import InfoIcon from "@mui/icons-material/Info";
import EditIcon from "@mui/icons-material/Edit";
import DeleteIcon from "@mui/icons-material/Delete";
import CategoryIcon from "@mui/icons-material/Category";
import useCategoryActions from "../../../hooks/category/useCategoryActions.js";

const CategoryCard = ({category, onDelete}) => {
    const navigate = useNavigate();

    const {deleteCategory} = useCategoryActions()

    const handleDelete = async () => {
        await deleteCategory(category.id, onDelete);
    };

    return (
        <Card className="entity-card">
            <CardContent className="entity-card-content">
                <Box className="entity-card-header">
                    <Box className="entity-card-icon">
                        <CategoryIcon />
                    </Box>
                    <Box sx={{minWidth: 0}}>
                        <Typography variant="h6" noWrap title={category.name}>
                            {category.name}
                        </Typography>
                        <Typography variant="body2" color="text.secondary" noWrap title={category.departmentName}>
                            {category.departmentName || "No department"}
                        </Typography>
                    </Box>
                </Box>

                <Stack direction="row" spacing={1} flexWrap="wrap" useFlexGap>
                    <Chip size="small" variant="outlined" label={`ID ${category.id}`} />
                </Stack>
            </CardContent>

            <CardActions className="entity-card-actions" sx={{justifyContent: "space-between"}}>
                <Button
                    size="small"
                    startIcon={<InfoIcon/>}
                    onClick={() => navigate(`/categories/${category.id}`)}
                >
                    Info
                </Button>

                <Box>
                    <Button
                        size="small"
                        startIcon={<EditIcon/>}
                        onClick={() => navigate(`/categories/${category.id}/edit`)}
                        color="warning"
                    >
                        Edit
                    </Button>

                    <Button
                        size="small"
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
