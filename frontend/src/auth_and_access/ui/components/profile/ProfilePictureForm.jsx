import React, { useState } from "react";
import { Avatar, Box, Button, Stack, Typography, Card, CardContent } from "@mui/material";
import { CloudUpload, Delete } from "@mui/icons-material";
import useProfileActions from "../../../hooks/profile/useProflieActions.js";



const ProfilePictureForm = ({ user, onSuccess }) => {
    const [file, setFile] = useState(null);
    const { updateMyProfilePicture, deleteMyProfilePicture } = useProfileActions();
    const profile = user?.profile;

    const handleFileChange = (event) => {
        setFile(event.target.files[0]);
    };

    const handleUpload = async () => {
        const success = await updateMyProfilePicture(file, onSuccess);
        if (success) {
            setFile(null);
        }
    };

    const handleDelete = async () => {
        await deleteMyProfilePicture(onSuccess);
    };

    return (
        <Box sx={{ maxWidth: 600, mx: 'auto', mt: 2 }}>
            <Card variant="outlined" sx={{ borderRadius: 3 }}>
                <CardContent sx={{ p: 4, display: 'flex', flexDirection: 'column', alignItems: 'center', textAlign: 'center' }}>
                    
                    <Avatar
                        src={profile?.profilePictureUrl || undefined}
                        sx={{ 
                            width: 140, height: 140, 
                            fontSize: '4rem', mb: 3, 
                            bgcolor: 'primary.main',
                            boxShadow: 2 
                        }}
                    >
                        {profile?.name?.charAt(0) || user?.username?.charAt(0) || "U"}
                    </Avatar>

                    <Typography variant="h6" gutterBottom>Profile Picture</Typography>
                    <Typography variant="body2" color="text.secondary" sx={{ mb: 4 }}>
                        Upload a new image or delete the current one to return to the default avatar.
                    </Typography>

                    <Stack spacing={3} sx={{ width: '100%' }}>
                        <Button 
                            variant="outlined" 
                            component="label" 
                            size="large" 
                            startIcon={<CloudUpload />}
                            sx={{ borderStyle: 'dashed', borderWidth: 2 }}
                        >
                            {file ? file.name : "Select New Image"}
                            <input type="file" hidden accept="image/*" onChange={handleFileChange} />
                        </Button>

                        <Stack direction={{ xs: 'column', sm: 'row' }} spacing={2}>
                            <Button 
                                variant="contained" 
                                onClick={handleUpload} 
                                disabled={!file} 
                                fullWidth
                            >
                                Upload
                            </Button>
                            
                            <Button 
                                variant="contained" 
                                color="error" 
                                onClick={handleDelete} 
                                disabled={!profile?.profilePictureUrl} 
                                fullWidth 
                                startIcon={<Delete />}
                            >
                                Delete
                            </Button>
                        </Stack>
                    </Stack>
                </CardContent>
            </Card>
        </Box>
    );
};

export default ProfilePictureForm;
