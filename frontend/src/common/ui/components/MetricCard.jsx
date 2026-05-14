import React from "react";
import {Box, Card, CardContent, Typography} from "@mui/material";

const MetricCard = ({label, value, helper, icon, accent = "primary"}) => {
    return (
        <Card variant="outlined" className="metric-card">
            <CardContent className="metric-card-content">
                <Box className={`metric-card-icon ${accent}`}>
                    {icon}
                </Box>
                <Box sx={{minWidth: 0}}>
                    <Typography variant="body2" color="text.secondary">
                        {label}
                    </Typography>
                    <Typography variant="h3" className="metric-card-value">
                        {value ?? 0}
                    </Typography>
                    {helper && (
                        <Typography variant="caption" color="text.secondary">
                            {helper}
                        </Typography>
                    )}
                </Box>
            </CardContent>
        </Card>
    );
};

export default MetricCard;
