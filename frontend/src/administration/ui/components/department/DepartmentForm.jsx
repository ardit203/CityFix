import React, { useEffect, useState } from "react";
import {
    Box,
    Button,
    Card,
    CardContent,
    Stack,
    TextField
} from "@mui/material";

const initialFormData = {
    name: "",
    description: ""
};

const DepartmentForm = ({
                            initialValues = initialFormData,
                            submitLabel = "Save",
                            onSubmit
                        }) => {
    const [formData, setFormData] = useState(initialFormData);

    useEffect(() => {
        if (initialValues) {
            setFormData({
                name: initialValues.name || "",
                description: initialValues.description || ""
            });
        }
    }, [initialValues]);

    const handleChange = (event) => {
        const { name, value } = event.target;

        setFormData((prev) => ({
            ...prev,
            [name]: value
        }));
    };

    const handleSubmit = async (event) => {
        event.preventDefault();
        await onSubmit(formData);
    };

    return (
        <Card variant="outlined">
            <CardContent>
                <Box component="form" onSubmit={handleSubmit}>
                    <Stack spacing={3}>
                        <TextField
                            label="Name"
                            name="name"
                            value={formData.name}
                            onChange={handleChange}
                            fullWidth
                            required
                        />

                        <TextField
                            label="Description"
                            name="description"
                            value={formData.description}
                            onChange={handleChange}
                            fullWidth
                            multiline
                            minRows={4}
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