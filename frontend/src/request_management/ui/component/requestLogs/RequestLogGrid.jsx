import React from 'react';
import {Grid} from "@mui/material";
import RequestLogCard from "./RequestLogCard.jsx";

const RequestLogGrid = ({requestId, requestLogs}) => {
    return (
        <Grid container spacing={{xs: 2, md: 3}}>
            {requestLogs.map((log) => (
                <Grid key={log.id} size={{xs: 12, sm: 6, md: 4, lg: 3}}>
                    <RequestLogCard log={log} requestId={requestId}/>
                </Grid>
            ))}
        </Grid>
    );
};

export default RequestLogGrid;