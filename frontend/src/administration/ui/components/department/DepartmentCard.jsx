import React from 'react';
import {useNavigate} from "react-router";
import {Box, Button, Card, CardActions, CardContent, Typography} from "@mui/material";
import InfoIcon from "@mui/icons-material/Info";
import EditIcon from "@mui/icons-material/Edit";
import DeleteIcon from "@mui/icons-material/Delete";
import useDepartmentActions from "../../../hooks/department/useDepartmentActions.js";

const DepartmentCard = ({department, onDelete}) => {
    const navigate = useNavigate();

    const {deleteDepartment} = useDepartmentActions()

    const handleDelete = async () => {
        await deleteDepartment(department.id, onDelete);
    };

    return (
        <Card sx={{maxWidth: 300}}>
            <CardContent>
                <Typography variant="h5">
                    {department.name}
                </Typography>

                <Typography variant="subtitle1">
                    {department.name}
                </Typography>
            </CardContent>

            <CardActions sx={{justifyContent: "space-between"}}>
                <Button
                    startIcon={<InfoIcon/>}
                    onClick={() => navigate(`/departments/${department.id}`)}
                >
                    Info
                </Button>

                <Box>
                    <Button
                        startIcon={<EditIcon/>}
                        onClick={() => navigate(`/departments/${department.id}/edit`)}
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

export default DepartmentCard;
