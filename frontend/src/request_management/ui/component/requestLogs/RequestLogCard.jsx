import React from "react";
import { useNavigate } from "react-router";
import {
    Box,
    Button,
    Card,
    CardActions,
    CardContent,
    Chip,
    Divider,
    Stack,
    Typography
} from "@mui/material";
import {
    Info as InfoIcon,
    History as HistoryIcon,
    EditNote as EditNoteIcon,
    AccessTime as AccessTimeIcon
} from "@mui/icons-material";

const getActionColor = (action) => {
    switch (action) {
        case "CREATED":
            return "success";
        case "UPDATED":
            return "info";
        case "STATUS_CHANGED":
            return "warning";
        case "ROUTING_CHANGED":
            return "secondary";
        case "DELETED":
            return "error";
        default:
            return "default";
    }
};

const formatAction = (action) => {
    if (!action) return "Unknown Action";

    return action
        .replaceAll("_", " ")
        .toLowerCase()
        .replace(/\b\w/g, char => char.toUpperCase());
};

const formatDateTime = (value) => {
    if (!value) return "N/A";

    return new Date(value).toLocaleString();
};

const RequestLogCard = ({ log, requestId }) => {
    const navigate = useNavigate();
    console.log(requestId)

    const handleInfo = () => {
        navigate(`/requests/${requestId}/logs/${log.id}`);
    };

    return (
        <Card
            sx={{
                maxWidth: 380,
                display: "flex",
                flexDirection: "column",
                height: "100%"
            }}
        >
            <CardContent sx={{ flexGrow: 1 }}>
                <Stack direction="row" spacing={2} alignItems="center" sx={{ mb: 2 }}>
                    <HistoryIcon color="primary" sx={{ fontSize: 38 }} />

                    <Box sx={{ flexGrow: 1, overflow: "hidden" }}>
                        <Typography variant="h6" fontWeight={600} noWrap>
                            Log #{log.id}
                        </Typography>

                        <Typography variant="body2" color="text.secondary" noWrap>
                            Request action history
                        </Typography>
                    </Box>
                </Stack>

                <Stack direction="row" spacing={1} sx={{ mb: 2 }}>
                    <Chip
                        label={formatAction(log.action)}
                        color={getActionColor(log.action)}
                        size="small"
                        variant="outlined"
                    />
                </Stack>

                <Divider sx={{ mb: 2 }} />

                <Stack spacing={1.5}>
                    <Stack direction="row" spacing={1} alignItems="flex-start">
                        <EditNoteIcon color="primary" fontSize="small" />

                        <Box>
                            <Typography variant="subtitle2" color="text.secondary">
                                Old Value
                            </Typography>
                            <Typography variant="body2">
                                {log.oldValue || "N/A"}
                            </Typography>
                        </Box>
                    </Stack>

                    <Stack direction="row" spacing={1} alignItems="flex-start">
                        <EditNoteIcon color="primary" fontSize="small" />

                        <Box>
                            <Typography variant="subtitle2" color="text.secondary">
                                New Value
                            </Typography>
                            <Typography variant="body2">
                                {log.newValue || "N/A"}
                            </Typography>
                        </Box>
                    </Stack>

                    <Stack direction="row" spacing={1} alignItems="center">
                        <AccessTimeIcon color="primary" fontSize="small" />

                        <Typography variant="body2">
                            {formatDateTime(log.createdAt)}
                        </Typography>
                    </Stack>
                </Stack>
            </CardContent>

            <CardActions sx={{ justifyContent: "flex-end", px: 2, pb: 2 }}>
                <Button
                    size="small"
                    startIcon={<InfoIcon />}
                    onClick={handleInfo}
                >
                    Info
                </Button>
            </CardActions>
        </Card>
    );
};

export default RequestLogCard;