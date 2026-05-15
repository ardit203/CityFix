import React from "react";
import {Box, Paper, Typography} from "@mui/material";

const PageHeader = ({title, subtitle}) => (
    <Paper
        elevation={0}
        className="section-card"
        sx={{
            position: "relative",
            overflow: "hidden",
            width: "100%",
            boxSizing: "border-box",
            flex: 1,
            p: {xs: 2.5, md: 3.5},
            mb: 3,
            display: "flex",
            alignItems: {xs: "flex-start", sm: "center"},
            justifyContent: "space-between",
            gap: 2,
            "&::before": {
                content: '""',
                position: "absolute",
                inset: "0 auto 0 0",
                width: 5,
                background: "linear-gradient(180deg, #0f8fce, #16a673)"
            }
        }}
    >
        <Box sx={{pl: {xs: 1, md: 1.5}}}>
            <Typography variant="h5" sx={{fontSize: {xs: "1.4rem", md: "1.75rem"}, mb: 0.65}}>
                {title}
            </Typography>
            {subtitle && (
                <Typography variant="body2" color="text.secondary" sx={{maxWidth: 720}}>
                    {subtitle}
                </Typography>
            )}
        </Box>
    </Paper>
);

export default PageHeader;
