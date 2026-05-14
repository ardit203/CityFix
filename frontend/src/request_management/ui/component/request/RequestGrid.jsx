import React from 'react';
import {Grid} from "@mui/material";
import RequestCard from "./RequestCard.jsx";

const RequestGrid = ({requests, onDelete}) => {
    return (
        <Grid container spacing={{xs: 2, md: 2.5}} className="entity-grid">
            {requests.map((request) => (
                <Grid key={request.id} size={{xs: 12, sm: 6, lg: 4, xl: 3}}>
                    <RequestCard request={request} onDelete={onDelete}/>
                </Grid>
            ))}
        </Grid>
    );
};

export default RequestGrid;
