import React from 'react';
import {Grid} from "@mui/material";
import RequestLogCard from "./RequestLogCard.jsx";

const RequestLogGrid = ({requestId, requestLogs}) => {
    return (
        <Grid container spacing={{xs: 2, md: 2.5}} className="entity-grid">
            {requestLogs.map((log) => (
                <Grid key={log.id} size={{xs: 12, sm: 6, lg: 4, xl: 3}}>
                    <RequestLogCard log={log} requestId={requestId}/>
                </Grid>
            ))}
        </Grid>
    );
};

export default RequestLogGrid;
