import React from 'react';
import {useNavigate} from "react-router";
import {Box, Button, Card, CardActions, CardContent, Typography} from "@mui/material";
import InfoIcon from "@mui/icons-material/Info";
import EditIcon from "@mui/icons-material/Edit";
import DeleteIcon from "@mui/icons-material/Delete";
import useMunicipalityActions from "../../../hooks/municipality/useMunicipalityActions.js";

const MunicipalityCard = ({municipality, onDelete}) => {
    const navigate = useNavigate();

    const {deleteMunicipality} = useMunicipalityActions()

    const handleDelete = async () => {
        await deleteMunicipality(municipality.id, onDelete);
    };

    return (
        <Card sx={{maxWidth: 300}}>
            <CardContent>
                <Typography variant="h5">
                    {municipality.name}
                </Typography>

                <Typography variant="subtitle1">
                    {municipality.name}
                </Typography>

                <Typography variant="h6" sx={{textAlign: "right"}}>
                    {municipality.code}
                </Typography>
            </CardContent>

            <CardActions sx={{justifyContent: "space-between"}}>
                <Button
                    startIcon={<InfoIcon/>}
                    onClick={() => navigate(`/municipalities/${municipality.id}`)}
                >
                    Info
                </Button>

                <Box>
                    <Button
                        startIcon={<EditIcon/>}
                        onClick={() => navigate(`/municipalities/${municipality.id}/edit`)}
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

export default MunicipalityCard;
