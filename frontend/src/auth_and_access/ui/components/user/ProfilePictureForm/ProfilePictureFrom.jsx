import { useState } from "react";
import {
    Avatar,
    Box,
    Button,
    Stack,
    Typography
} from "@mui/material";
import useSnackbar from "../../../../../common/hooks/useSnackbar.js";
import useConfirmDialog from "../../../../../common/hooks/useConfirmDialog.js";
// import userApi from "../../../../service/userApi.js";

const ProfilePictureForm = ({ user, onSuccess }) => {
    const [file, setFile] = useState(null);

    const { confirm } = useConfirmDialog();
    const { showSnackbar } = useSnackbar();

    const profile = user?.profile;

    const handleFileChange = (event) => {
        setFile(event.target.files[0]);
    };

    const handleUpload = async () => {
        if (!file) {
            showSnackbar("Please select a file first", "error");
            return;
        }

        const confirmed = await confirm({
            title: "Update profile picture?",
            message: "Are you sure you want to update your profile picture?",
            confirmText: "Upload",
            cancelText: "Cancel"
        });

        if (!confirmed) {
            return;
        }

        try {
            const formData = new FormData();
            formData.append("file", file);

            console.log("Upload profile picture", formData);

            // await userApi.updateMyProfilePicture(formData);

            showSnackbar("Profile picture updated successfully", "success");
            setFile(null);

            if (onSuccess) {
                onSuccess();
            }
        } catch (error) {
            const message =
                error.response?.data?.message ||
                error.response?.data?.error ||
                error.message ||
                "Failed to update profile picture";

            showSnackbar(message, "error");
        }
    };

    const handleDelete = async () => {
        const confirmed = await confirm({
            title: "Delete profile picture?",
            message: "Are you sure you want to delete your profile picture?",
            confirmText: "Delete",
            cancelText: "Cancel"
        });

        if (!confirmed) {
            return;
        }

        try {
            console.log("Delete profile picture");

            // await userApi.deleteMyProfilePicture();

            showSnackbar("Profile picture deleted successfully", "success");

            if (onSuccess) {
                onSuccess();
            }
        } catch (error) {
            const message =
                error.response?.data?.message ||
                error.response?.data?.error ||
                error.message ||
                "Failed to delete profile picture";

            showSnackbar(message, "error");
        }
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
                >
                    Delete Picture
                </Button>
            </Stack>
        </Stack>
    );
};

export default ProfilePictureForm;