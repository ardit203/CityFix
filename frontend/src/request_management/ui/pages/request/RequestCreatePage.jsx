import React from "react";
import {useNavigate} from "react-router";
import {
    Box,
    Typography
} from "@mui/material";
import ActionBar from "../../../../common/ui/components/ActionBar.jsx";
import useRequestActions from "../../../hooks/request/useRequestActions.js";
import RequestForm from "../../component/request/RequestForm.jsx";

const RequestCreatePage = () => {
    const navigate = useNavigate();

    const {createRequest} = useRequestActions();

    const handleCreate = async (data) => {
        const createdRequest = await createRequest(data);
        navigate("/requests");
        // if (createdRequest?.id) {
        //     navigate(`/requests/${createdRequest.id}`);
        // } else {
        //     navigate("/requests");
        // }
    };

    return (
        <Box>
            <ActionBar
                onSecondaryBack={() => navigate("/requests")}
                secondaryBackLabel="Back to Requests"
            />
            <Typography variant="h4" fontWeight={600} sx={{mb: 3}}>
                Create Request
            </Typography>

            <RequestForm
                submitLabel="Create Request"
                onSubmit={handleCreate}
            />
        </Box>
    );
};

export default RequestCreatePage;