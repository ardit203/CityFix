import { useState } from "react";
import {
    Avatar,
    Box,
    Button,
    Stack,
    Typography
} from "@mui/material";
import useProfileActions from "../../../../hooks/useProfileActions.js";

const ProfilePictureForm = ({ user, onSuccess }) => {
    const [file, setFile] = useState(null);

    const {
        updateMyProfilePicture,
        deleteMyProfilePicture
    } = useProfileActions();

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
        <Stack spacing={3}>
            <Box sx={{ display: "flex", alignItems: "center", gap: 3 }}>
                <Avatar
                    src={profile?.profilePictureUrl || undefined}
                    sx={{ width: 120, height: 120, fontSize: 42 }}
                >
                    {profile?.name?.charAt(0) || user?.username?.charAt(0) || "U"}
                </Avatar>

                <Box>
                    <Typography variant="h6">
                        Current profile picture
                    </Typography>

                    <Typography variant="body2" color="text.secondary">
                        Upload a new image or delete the current one.
                    </Typography>
                </Box>
            </Box>

            <Button variant="outlined" component="label">
                Select Image
                <input
                    type="file"
                    hidden
                    accept="image/*"
                    onChange={handleFileChange}
                />
            </Button>

            {file && (
                <Typography variant="body2">
                    Selected file: {file.name}
                </Typography>
            )}

            <Stack direction="row" spacing={2}>
                <Button
                    variant="contained"
                    onClick={handleUpload}
                    disabled={!file}
                >
                    Upload Picture
                </Button>

                <Button
                    variant="outlined"
                    color="error"
                    onClick={handleDelete}
                    disabled={!profile?.profilePictureUrl}
                >
                    Delete Picture
                </Button>
            </Stack>
        </Stack>
    );
};

export default ProfilePictureForm;