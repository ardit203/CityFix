import React from "react";
import {Paper, Typography} from "@mui/material";

const PageHeader = ({title, subtitle}) => (
    <Paper elevation={2} sx={{p: {xs: 2, md: 3}, borderRadius: 3, mb: 3}}>
        <Typography variant="h4" fontWeight={700}>
            {title}
        </Typography>
        {subtitle && (
            <Typography variant="body2" color="text.secondary">
                {subtitle}
            </Typography>
        )}
    </Paper>
);

export default PageHeader;
