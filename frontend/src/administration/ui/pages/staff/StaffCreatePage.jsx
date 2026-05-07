import React from "react";
import {useNavigate} from "react-router";
import {
    Box,
    Typography
} from "@mui/material";
import ActionBar from "../../../../common/ui/components/ActionBar.jsx";
import useStaffActions from "../../../hooks/staff/useStaffActions.js";
import StaffForm from "../../components/staff/StaffForm.jsx";

const StaffCreatePage = () => {
    const navigate = useNavigate();

    const {createStaff} = useStaffActions();

    const handleCreate = async (data) => {
        const createdStaff = await createStaff(data);

        if (createdStaff?.id) {
            navigate(`/staff/${createdStaff.id}`);
        } else {
            navigate("/staff");
        }
    };

    return (
        <Box>
            <ActionBar
                onSecondaryBack={() => navigate("/staff")}
                secondaryBackLabel="Back to Staff"
            />
            <Typography variant="h4" fontWeight={600} sx={{mb: 3}}>
                Create Staff
            </Typography>

            <StaffForm
                submitLabel="Create Staff"
                onSubmit={handleCreate}
            />
        </Box>
    );
};

export default StaffCreatePage;