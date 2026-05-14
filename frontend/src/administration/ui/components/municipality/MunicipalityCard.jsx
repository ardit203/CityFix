import React from 'react';
import {useNavigate} from "react-router";
import {Box, Button, Card, CardActions, CardContent, Chip, Stack, Typography} from "@mui/material";
import InfoIcon from "@mui/icons-material/Info";
import EditIcon from "@mui/icons-material/Edit";
import DeleteIcon from "@mui/icons-material/Delete";
import LocationCityIcon from "@mui/icons-material/LocationCity";
import useMunicipalityActions from "../../../hooks/municipality/useMunicipalityActions.js";

const MunicipalityCard = ({municipality, onDelete}) => {
    const navigate = useNavigate();

    const {deleteMunicipality} = useMunicipalityActions()

    const handleDelete = async () => {
        await deleteMunicipality(municipality.id, onDelete);
    };

    return (
        <Card className="entity-card">
            <CardContent className="entity-card-content">
                <Box className="entity-card-header">
                    <Box className="entity-card-icon">
                        <LocationCityIcon />
                    </Box>
                    <Box sx={{minWidth: 0}}>
                        <Typography variant="h6" noWrap title={municipality.name}>
                            {municipality.name}
                        </Typography>
                        <Typography variant="body2" color="text.secondary">
                            Municipality
                        </Typography>
                    </Box>
                </Box>

                <Stack direction="row" spacing={1} flexWrap="wrap" useFlexGap>
                    <Chip size="small" color="primary" variant="outlined" label={municipality.code || "No code"} />
                    <Chip size="small" variant="outlined" label={`ID ${municipality.id}`} />
                </Stack>
            </CardContent>

            <CardActions className="entity-card-actions" sx={{justifyContent: "space-between"}}>
                <Button
                    size="small"
                    startIcon={<InfoIcon/>}
                    onClick={() => navigate(`/municipalities/${municipality.id}`)}
                >
                    Info
                </Button>

                <Box>
                    <Button
                        size="small"
                        startIcon={<EditIcon/>}
                        onClick={() => navigate(`/municipalities/${municipality.id}/edit`)}
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

export default MunicipalityCard;
