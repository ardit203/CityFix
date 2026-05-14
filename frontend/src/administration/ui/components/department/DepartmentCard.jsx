import React from 'react';
import {useNavigate} from "react-router";
import {Box, Button, Card, CardActions, CardContent, Chip, Stack, Typography} from "@mui/material";
import InfoIcon from "@mui/icons-material/Info";
import EditIcon from "@mui/icons-material/Edit";
import DeleteIcon from "@mui/icons-material/Delete";
import BusinessIcon from "@mui/icons-material/Business";
import useDepartmentActions from "../../../hooks/department/useDepartmentActions.js";

const DepartmentCard = ({department, onDelete}) => {
    const navigate = useNavigate();

    const {deleteDepartment} = useDepartmentActions()

    const handleDelete = async () => {
        await deleteDepartment(department.id, onDelete);
    };

    return (
        <Card className="entity-card">
            <CardContent className="entity-card-content">
                <Box className="entity-card-header">
                    <Box className="entity-card-icon">
                        <BusinessIcon />
                    </Box>
                    <Box sx={{minWidth: 0}}>
                        <Typography variant="h6" noWrap title={department.name}>
                            {department.name}
                        </Typography>
                        <Typography variant="body2" color="text.secondary">
                            Department
                        </Typography>
                    </Box>
                </Box>

                <Stack direction="row" spacing={1} flexWrap="wrap" useFlexGap>
                    <Chip size="small" variant="outlined" label={`ID ${department.id}`} />
                </Stack>
            </CardContent>

            <CardActions className="entity-card-actions" sx={{justifyContent: "space-between"}}>
                <Button
                    size="small"
                    startIcon={<InfoIcon/>}
                    onClick={() => navigate(`/departments/${department.id}`)}
                >
                    Info
                </Button>

                <Box>
                    <Button
                        size="small"
                        startIcon={<EditIcon/>}
                        onClick={() => navigate(`/departments/${department.id}/edit`)}
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

export default DepartmentCard;
