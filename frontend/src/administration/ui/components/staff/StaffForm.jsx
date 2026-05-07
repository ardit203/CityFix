import React, {useEffect, useState} from "react";
import {
    Box, Button, Card, CardContent, Stack,
    FormControl, InputLabel, Select, MenuItem, FormHelperText, CircularProgress, Typography
} from "@mui/material";

import useForm from "../../../../common/hooks/useForm.js";
import useDepartments from "../../../hooks/department/useDepartments.js";
import useMunicipalities from "../../../hooks/municipality/useMunicipalities.js";
import useAvailableUsersForStaff from "../../../hooks/staff/useAvailableUsersForStaff.js";

const initialFormData = {
    userId: "",
    departmentId: "",
    municipalityId: ""
};

const StaffForm = ({
                       initialValues,
                       submitLabel = "Save",
                       onSubmit
                   }) => {
    const [errors, setErrors] = useState({});

    // Fetch the necessary dropdown data
    const {departments, loading: loadingDepartments} = useDepartments({paged: false});
    const {municipalities, loading: loadingMunicipalities} = useMunicipalities({paged: false});
    const {availableUsers, loading: loadingAvailableUsers} = useAvailableUsersForStaff();

    const {formData, handleChange, resetForm, handleSubmit} = useForm(initialFormData);

    // Determine if we are in Edit Mode
    const isEditMode = !!initialValues;

    useEffect(() => {
        if (initialValues) {
            resetForm({
                userId: initialValues.userId || "",
                departmentId: initialValues.departmentId || "",
                municipalityId: initialValues.municipalityId || ""
            });
        }
    }, [initialValues, resetForm]);


    const validate = () => {
        let tempErrors = {};

        if (!formData.userId) tempErrors.userId = "Please select a user.";
        if (!formData.departmentId) tempErrors.departmentId = "Please select a department.";
        if (!formData.municipalityId) tempErrors.municipalityId = "Please select a municipality.";

        setErrors(tempErrors);
        return Object.keys(tempErrors).length === 0;
    };

    return (
        <Card variant="outlined">
            <CardContent sx={{p: {xs: 2, sm: 4}}}>
                <Box component="form" onSubmit={(event) => handleSubmit(event, validate, onSubmit)} noValidate>
                    <Stack spacing={4}>

                        {/* --- USER DROPDOWN --- */}
                        <FormControl fullWidth required error={!!errors.userId}>
                            <InputLabel>User (Staff Member)</InputLabel>
                            <Select
                                name="userId"
                                value={formData.userId}
                                label="User (Staff Member)"
                                onChange={handleChange}
                                disabled={loadingAvailableUsers || isEditMode}
                                // Disable in edit mode because you usually can't change the underlying User of an existing Staff member!
                            >
                                {loadingAvailableUsers ? (
                                    <MenuItem disabled>
                                        <CircularProgress size={20} sx={{mr: 2}}/> Loading available users...
                                    </MenuItem>
                                ) : isEditMode ? (
                                    // If in edit mode, the user might not be in the "available" list anymore, so we just show their ID
                                    // You can enhance this by passing down the initial user's name via initialValues if you want!
                                    <MenuItem value={formData.userId}>
                                        Selected User ID: {formData.userId}
                                    </MenuItem>
                                ) : availableUsers.length === 0 ? (
                                    <MenuItem disabled>No users available for staff assignment</MenuItem>
                                ) : (
                                    availableUsers.map((u) => (
                                        <MenuItem key={u.id} value={u.id}>
                                            {u.name} {u.surname} (@{u.username})
                                        </MenuItem>
                                    ))
                                )}
                            </Select>
                            {errors.userId && <FormHelperText>{errors.userId}</FormHelperText>}
                            {isEditMode && <FormHelperText>The user assigned to this staff profile cannot be
                                changed.</FormHelperText>}
                        </FormControl>

                        {/* --- DEPARTMENT DROPDOWN --- */}
                        <FormControl fullWidth required error={!!errors.departmentId}>
                            <InputLabel>Department</InputLabel>
                            <Select
                                name="departmentId"
                                value={formData.departmentId}
                                label="Department"
                                onChange={handleChange}
                                disabled={loadingDepartments}
                            >
                                {loadingDepartments ? (
                                    <MenuItem disabled>
                                        <CircularProgress size={20} sx={{mr: 2}}/> Loading departments...
                                    </MenuItem>
                                ) : departments.length === 0 ? (
                                    <MenuItem disabled>No departments available</MenuItem>
                                ) : (
                                    departments.map((dep) => (
                                        <MenuItem key={dep.id} value={dep.id}>
                                            {dep.name}
                                        </MenuItem>
                                    ))
                                )}
                            </Select>
                            {errors.departmentId && <FormHelperText>{errors.departmentId}</FormHelperText>}
                        </FormControl>

                        {/* --- MUNICIPALITY DROPDOWN --- */}
                        <FormControl fullWidth required error={!!errors.municipalityId}>
                            <InputLabel>Municipality</InputLabel>
                            <Select
                                name="municipalityId"
                                value={formData.municipalityId}
                                label="Municipality"
                                onChange={handleChange}
                                disabled={loadingMunicipalities}
                            >
                                {loadingMunicipalities ? (
                                    <MenuItem disabled>
                                        <CircularProgress size={20} sx={{mr: 2}}/> Loading municipalities...
                                    </MenuItem>
                                ) : municipalities.length === 0 ? (
                                    <MenuItem disabled>No municipalities available</MenuItem>
                                ) : (
                                    municipalities.map((mun) => (
                                        <MenuItem key={mun.id} value={mun.id}>
                                            {mun.name}
                                        </MenuItem>
                                    ))
                                )}
                            </Select>
                            {errors.municipalityId && <FormHelperText>{errors.municipalityId}</FormHelperText>}
                        </FormControl>

                        <Box sx={{display: 'flex', justifyContent: 'flex-end', mt: 2}}>
                            <Button type="submit" variant="contained" size="large">
                                {submitLabel}
                            </Button>
                        </Box>
                    </Stack>
                </Box>
            </CardContent>
        </Card>
    );
};

export default StaffForm;
