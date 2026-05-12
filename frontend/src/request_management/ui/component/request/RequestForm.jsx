import React, { useEffect, useState } from "react";
import {
    Box, Button, Card, CardContent, Stack, TextField,
    FormControl, InputLabel, Select, MenuItem, FormHelperText, CircularProgress, Typography,
    List, ListItem, ListItemText, ListItemSecondaryAction, IconButton
} from "@mui/material";
import { CloudUpload, Delete as DeleteIcon } from "@mui/icons-material";

import useForm from "../../../../common/hooks/useForm.js";
import useMunicipalities from "../../../../administration/hooks/municipality/useMunicipalities.js";
import { emptyCreateRequestDto } from "../../../dtos/requestDto.js";
import LocationPicker from "./LocationPicker.jsx";

const RequestForm = ({
    initialValues,
    submitLabel = "Submit Request",
    onSubmit
}) => {
    const [errors, setErrors] = useState({});
    
    // Fetch municipalities for the dropdown
    const { municipalities, loading: loadingMunicipalities } = useMunicipalities({ paged: false });
    
    const { formData, handleChange, resetForm, handleSubmit } = useForm(emptyCreateRequestDto);

    useEffect(() => {
        if (initialValues) {
            resetForm(initialValues);
        }
    }, [initialValues, resetForm]);

    // Validation Logic
    const validate = () => {
        let tempErrors = {};

        if (!formData.title.trim()) {
            tempErrors.title = "Request title is required.";
        } else if (formData.title.length > 255) {
            tempErrors.title = "Title cannot exceed 255 characters.";
        }

        if (!formData.description.trim()) {
            tempErrors.description = "Description is required.";
        } else if (formData.description.length > 2000) {
            tempErrors.description = "Description cannot exceed 2000 characters.";
        }

        if (!formData.municipalityId) {
            tempErrors.municipalityId = "Please select a municipality.";
        }

        if (!formData.requestLocationDto?.latitude || !formData.requestLocationDto?.longitude) {
            tempErrors.location = "Please select a location on the map.";
        }

        if (formData.files && formData.files.length > 5) {
            tempErrors.files = "You can only upload up to 5 files.";
        }

        setErrors(tempErrors);
        return Object.keys(tempErrors).length === 0;
    };

    const handleFileChange = (e) => {
        const selectedFiles = Array.from(e.target.files);
        if (selectedFiles.length + (formData.files?.length || 0) > 5) {
            setErrors(prev => ({...prev, files: "You can only upload up to 5 files total."}));
            return;
        }
        
        handleChange({
            target: {
                name: 'files',
                value: [...(formData.files || []), ...selectedFiles]
            }
        });
        
        if (errors.files) {
            setErrors(prev => ({...prev, files: null}));
        }
        
        // Reset input value so same files can be selected again if removed
        e.target.value = null;
    };

    const handleRemoveFile = (index) => {
        const newFiles = [...(formData.files || [])];
        newFiles.splice(index, 1);
        handleChange({
            target: {
                name: 'files',
                value: newFiles
            }
        });
    };

    const handleLocationSelect = (location) => {
        // Since useForm supports dot notation for nested objects, we can construct an event object
        // but here we are replacing the entire requestLocationDto object.
        handleChange({
            target: {
                name: 'requestLocationDto',
                value: {
                    latitude: location.latitude,
                    longitude: location.longitude
                }
            }
        });
        
        // Clear location error if it existed
        if (errors.location) {
            setErrors(prev => ({...prev, location: null}));
        }
    };

    return (
        <Card variant="outlined">
            <CardContent>
                <Box component="form" onSubmit={(e) => handleSubmit(e, validate, onSubmit)} noValidate>
                    <Stack spacing={3}>
                        <TextField
                            label="Title"
                            name="title"
                            value={formData.title}
                            onChange={handleChange}
                            fullWidth
                            required
                            error={!!errors.title}
                            helperText={errors.title}
                        />

                        <TextField
                            label="Description"
                            name="description"
                            value={formData.description}
                            onChange={handleChange}
                            fullWidth
                            required
                            multiline
                            minRows={4}
                            error={!!errors.description}
                            helperText={errors.description || "Maximum 2000 characters"}
                        />

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
                                    <MenuItem value={formData.municipalityId || ""} disabled>
                                        <CircularProgress size={20} sx={{mr: 2}}/> Loading municipalities...
                                    </MenuItem>
                                ) : municipalities.length === 0 ? (
                                    <MenuItem value={formData.municipalityId || ""} disabled>No municipalities available</MenuItem>
                                ) : (
                                    [
                                        ...municipalities.map((mun) => (
                                            <MenuItem key={mun.id} value={mun.id}>
                                                {mun.name} ({mun.code})
                                            </MenuItem>
                                        )),
                                        formData.municipalityId && !municipalities.some(m => m.id === formData.municipalityId) ? (
                                            <MenuItem key="fallback-mun" value={formData.municipalityId} sx={{ display: 'none' }}>
                                                Unknown Municipality ({formData.municipalityId})
                                            </MenuItem>
                                        ) : null
                                    ]
                                )}
                            </Select>
                            {errors.municipalityId && <FormHelperText>{errors.municipalityId}</FormHelperText>}
                        </FormControl>

                        <Box>
                            <Typography variant="subtitle1" gutterBottom>
                                Location *
                            </Typography>
                            <Typography variant="body2" color="text.secondary" gutterBottom>
                                Please click on the map or search for an address to set the exact location of your request.
                            </Typography>
                            
                            <Box sx={{ 
                                border: errors.location ? '1px solid #d32f2f' : '1px solid #e0e0e0',
                                borderRadius: 1,
                                overflow: 'hidden',
                                mt: 1
                            }}>
                                <LocationPicker onLocationSelect={handleLocationSelect} />
                            </Box>
                            
                            {errors.location && (
                                <Typography variant="caption" color="error" sx={{ mt: 1, display: 'block' }}>
                                    {errors.location}
                                </Typography>
                            )}
                            
                            {formData.requestLocationDto?.latitude && (
                                <Typography variant="caption" color="success.main" sx={{ mt: 1, display: 'block' }}>
                                    Location selected: {formData.requestLocationDto.latitude.toFixed(4)}, {formData.requestLocationDto.longitude.toFixed(4)}
                                </Typography>
                            )}
                        </Box>

                        <Box>
                            <Typography variant="subtitle1" gutterBottom>
                                Attachments (Optional, Max 5)
                            </Typography>
                            <Button
                                variant="outlined"
                                component="label"
                                startIcon={<CloudUpload />}
                            >
                                Upload Files
                                <input
                                    type="file"
                                    hidden
                                    multiple
                                    onChange={handleFileChange}
                                    accept="image/*,application/pdf"
                                />
                            </Button>
                            
                            {errors.files && (
                                <Typography variant="caption" color="error" sx={{ mt: 1, display: 'block' }}>
                                    {errors.files}
                                </Typography>
                            )}

                            {formData.files && formData.files.length > 0 && (
                                <List dense>
                                    {formData.files.map((file, index) => (
                                        <ListItem key={index}>
                                            <ListItemText 
                                                primary={file.name} 
                                                secondary={`${(file.size / 1024 / 1024).toFixed(2)} MB`} 
                                            />
                                            <ListItemSecondaryAction>
                                                <IconButton edge="end" aria-label="delete" onClick={() => handleRemoveFile(index)}>
                                                    <DeleteIcon />
                                                </IconButton>
                                            </ListItemSecondaryAction>
                                        </ListItem>
                                    ))}
                                </List>
                            )}
                        </Box>

                        <Box sx={{ pt: 2 }}>
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

export default RequestForm;
