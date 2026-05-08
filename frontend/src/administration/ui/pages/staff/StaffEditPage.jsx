import React from "react";
import {useNavigate, useParams} from "react-router";
import {Box, Typography} from "@mui/material";

import AsyncDataView from "../../../../common/ui/components/AsyncDataView.jsx";
import ActionBar from "../../../../common/ui/components/ActionBar.jsx";
import useStaffDetails from "../../../hooks/staff/useStaffDetails.js";
import useStaffActions from "../../../hooks/staff/useStaffActions.js";
import StaffForm from "../../components/staff/StaffForm.jsx";
import { mapDisplayToFormStaffDto } from "../../../dtos/staffDto.js";


const StaffEditPage = () => {
    const {id} = useParams();
    const navigate = useNavigate();
    const {staff, loading, error} = useStaffDetails(id);
    const {updateStaff} = useStaffActions();

    const handleUpdate = async (data) => {
        await updateStaff(staff.id, data, () => {
            navigate(`/staff/${staff.id}`);
        });
    };

    if (loading || error || !staff) {
        return (
            <AsyncDataView
                loading={loading}
                error={error}
                data={staff}
                notFoundMessage="Staff not found."
                backUrl="/staff"
            />
        );
    }

    return (
        <Box>
            <ActionBar
                onBack={() => navigate(`/staff/${staff.id}`)}
                backLabel="Back to Staff"
                onSecondaryBack={() => navigate("/staff")}
                secondaryBackLabel="Back to Staff's"
            >

            </ActionBar>

            <Typography variant="h4" fontWeight={600} sx={{mb: 3}}>
                Edit Category
            </Typography>

            <StaffForm
                initialValues={mapDisplayToFormStaffDto(staff)}
                submitLabel="Update category"
                onSubmit={handleUpdate}
            />
        </Box>
    );
};

export default StaffEditPage;
