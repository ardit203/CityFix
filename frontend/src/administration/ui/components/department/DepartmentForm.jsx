import React, {useEffect, useState} from "react";
import {Box, Button, Card, CardContent, Stack, TextField} from "@mui/material";
import useForm from "../../../../common/hooks/useForm.js";

const initialFormData = {
    name: "",
    description: ""
};

const DepartmentForm = ({
                            initialValues,
                            submitLabel = "Save",
                            onSubmit
                        }) => {
    const [errors, setErrors] = useState({});
    const {formData, handleChange, resetForm, handleSubmit} = useForm(initialFormData);

    useEffect(() => {
        if (initialValues) {
            resetForm({
                name: initialValues.name || "",
                description: initialValues.description || ""
            });
        }
    }, [initialValues, resetForm]);

    const validate = () => {
        let tempErrors = {};
        // Validation for 'name'
        if (!formData.name.trim()) {
            tempErrors.name = "Department name is required.";
        } else if (formData.name.length < 3) {
            tempErrors.name = "Name must be at least 3 characters long.";
        } else if (formData.name.length > 50) {
            tempErrors.name = "Name cannot exceed 50 characters.";
        }
        // Validation for 'description' (optional field, so we only validate length if it exists)
        if (formData.description && formData.description.length > 255) {
            tempErrors.description = "Description cannot exceed 255 characters.";
        }
        setErrors(tempErrors);

        // Returns true if the object has no keys (meaning zero errors!)
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

export default DepartmentForm;
