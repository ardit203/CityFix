import React, {useEffect, useState} from "react";
import {Box, Button, Card, CardContent, Stack, TextField} from "@mui/material";
import useForm from "../../../../common/hooks/useForm.js";

const initialFormData = {
    name: "",
    code: ""
};

const MunicipalityForm = ({
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
                code: initialValues.code || ""
            });
        }
    }, [initialValues, resetForm]);

    const validate = () => {
        return true
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
                            label="Code"
                            name="code"
                            value={formData.code}
                            onChange={handleChange}
                            fullWidth
                            error={!!errors.code}
                            helperText={errors.code}
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

export default MunicipalityForm;
