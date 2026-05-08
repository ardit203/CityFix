import React, { useEffect, useState } from "react";
import {
    Box, Button, Card, CardContent, Stack, TextField,
    FormControl, InputLabel, Select, MenuItem, FormHelperText, CircularProgress
} from "@mui/material";

import useForm from "../../../../common/hooks/useForm.js";
import useDepartments from "../../../hooks/department/useDepartments.js";

import { emptyCreateCategoryDto } from "../../../dtos/categoryDto.js";

const CategoryForm = ({
                          initialValues,
                          submitLabel = "Save",
                          onSubmit
                      }) => {
    const [errors, setErrors] = useState({});

    const { departments, loading: loadingDepartments } = useDepartments({ paged: false });
    const { formData, handleChange, resetForm, handleSubmit } = useForm(emptyCreateCategoryDto);

    useEffect(() => {
        if (initialValues) {
            resetForm(initialValues);
        }
    }, [initialValues, resetForm]);

    // 3. Validation Logic
    const validate = () => {
        let tempErrors = {};

        // Name validation
        if (!formData.name.trim()) {
            tempErrors.name = "Category name is required.";
        } else if (formData.name.length < 3) {
            tempErrors.name = "Name must be at least 3 characters long.";
        } else if (formData.name.length > 50) {
            tempErrors.name = "Name cannot exceed 50 characters.";
        }

        // Description validation
        if (formData.description && formData.description.length > 255) {
            tempErrors.description = "Description cannot exceed 255 characters.";
        }

        // Department validation
        if (!formData.departmentId) {
            tempErrors.departmentId = "Please select a department.";
        }

        setErrors(tempErrors);
        return Object.keys(tempErrors).length === 0;
    };

    return (
        <Card variant="outlined">
            <CardContent>
                <Box component="form" onSubmit={() => handleSubmit(event, validate, onSubmit)} noValidate>
                    <Stack spacing={3}>

                        <TextField
                            label="Name"
                            name="name"
                            value={formData.name}
                            onChange={handleChange}
                            fullWidth
                            required
                            error={!!errors.name}
                            helperText={errors.name}
                        />

                        {/* --- THE NEW DEPARTMENT DROPDOWN --- */}
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
                                    <MenuItem value={formData.departmentId || ""} disabled>
                                        <CircularProgress size={20} sx={{mr: 2}}/> Loading departments...
                                    </MenuItem>
                                ) : departments.length === 0 ? (
                                    <MenuItem value={formData.departmentId || ""} disabled>No departments available</MenuItem>
                                ) : (
                                    [
                                        ...departments.map((dep) => (
                                            <MenuItem key={dep.id} value={dep.id}>
                                                {dep.name}
                                            </MenuItem>
                                        )),
                                        formData.departmentId && !departments.some(d => d.id === formData.departmentId) ? (
                                            <MenuItem key="fallback-dep" value={formData.departmentId} sx={{ display: 'none' }}>
                                                Unknown Department ({formData.departmentId})
                                            </MenuItem>
                                        ) : null
                                    ]
                                )}
                            </Select>
                            {errors.departmentId && <FormHelperText>{errors.departmentId}</FormHelperText>}
                        </FormControl>

                        <TextField
                            label="Description"
                            name="description"
                            value={formData.description}
                            onChange={handleChange}
                            fullWidth
                            multiline
                            minRows={4}
                            error={!!errors.description}
                            helperText={errors.description || "Maximum 255 characters"}
                        />

                        <Box>
                            <Button type="submit" variant="contained">
                                {submitLabel}
                            </Button>
                        </Box>
                    </Stack>
                </Box>
            </CardContent>
        </Card>
    );
};

export default CategoryForm;
