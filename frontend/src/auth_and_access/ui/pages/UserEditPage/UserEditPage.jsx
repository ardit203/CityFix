import { useState } from "react";
import { useNavigate, useParams } from "react-router";
import {
    Box,
    Button,
    CircularProgress,
    Stack,
    Typography
} from "@mui/material";
import {
    ArrowBack,
    AdminPanelSettings,
    Lock,
    LockOpen
} from "@mui/icons-material";

import useUserDetails from "../../../hooks/useUserDetails.js";
import useConfirmDialog from "../../../../common/hooks/useConfirmDialog.js";
import useSnackbar from "../../../../common/hooks/useSnackbar.js";
import LockUserDialog from "../../components/user/LockUserDialog/LockUserDialog.jsx";
import ChangeUserRoleDialog from "../../components/user/ChangeRoleDialog/ChangeRoleDialog.jsx";
import UserAdminForm from "../../components/user/UserAdminForm/UserAdminForm.jsx";
import useConfirmedAction from "../../../../common/hooks/useConfirmedAction.js";
// import userApi from "../../../service/userApi.js";

const UserEditPage = () => {
    const { id } = useParams();
    const navigate = useNavigate();

    const { user, loading, error, fetchUser } = useUserDetails(id);

    const [roleDialogOpen, setRoleDialogOpen] = useState(false);
    const [lockDialogOpen, setLockDialogOpen] = useState(false);

    const { confirm } = useConfirmDialog();
    const { showSnackbar } = useSnackbar();
    const { runConfirmedAction } = useConfirmedAction();


    const handleUpdate = async (formData) => {
        await runConfirmedAction({
            confirmOptions: {
                title: "Update user?",
                message: `Are you sure you want to update ${user.username}?`,
                confirmText: "Update",
                cancelText: "Cancel"
            },
            action: async () => {
                console.log("Update user", user.id, formData);

                // later:
                // await userApi.update(user.id, formData);

                if (fetchUser) {
                    fetchUser();
                }
            },
            successMessage: "User updated successfully",
            navigateTo: "/users"
        });
    };

    const handleUnlock = async () => {
        const confirmed = await confirm({
            title: "Unlock user account?",
            message: `Are you sure you want to unlock ${user.username}?`,
            confirmText: "Unlock",
            cancelText: "Cancel"
        });

        if (!confirmed) {
            return;
        }

        try {
            console.log("Unlock user", user.id);

            // later:
            // await userApi.unlock(user.id);

            showSnackbar("User unlocked successfully", "success");

            if (fetchUser) {
                fetchUser();
            }
        } catch (error) {
            const message =
                error.response?.data?.message ||
                error.response?.data?.error ||
                error.message ||
                "Failed to unlock user";

            showSnackbar(message, "error");
        }
    };

    if (loading) {
        return (
            <Box sx={{ display: "flex", justifyContent: "center", mt: 5 }}>
                <CircularProgress />
            </Box>
        );
    }

    if (error) {
        return (
            <Box>
                <Typography color="error">{error}</Typography>

                <Button
                    sx={{ mt: 2 }}
                    variant="outlined"
                    startIcon={<ArrowBack />}
                    onClick={() => navigate("/users")}
                >
                    Back to Users
                </Button>
            </Box>
        );
    }

    if (!user) {
        return <Typography>User not found.</Typography>;
    }

    return (
        <>
            <Stack
                direction="row"
                justifyContent="space-between"
                alignItems="center"
                sx={{ mb: 3 }}
            >
                <Button
                    variant="outlined"
                    startIcon={<ArrowBack />}
                    onClick={() => navigate("/users")}
                >
                    Back to Users
                </Button>

                <Stack direction="row" spacing={2}>
                    <Button
                        variant="contained"
                        color="primary"
                        startIcon={<AdminPanelSettings />}
                        onClick={() => setRoleDialogOpen(true)}
                    >
                        Change Role
                    </Button>

                    <Button
                        variant="outlined"
                        color={user.locked ? "success" : "warning"}
                        startIcon={user.locked ? <LockOpen /> : <Lock />}
                        onClick={user.locked ? handleUnlock : () => setLockDialogOpen(true)}
                    >
                        {user.locked ? "Unlock" : "Lock"}
                    </Button>
                </Stack>
            </Stack>

            <UserAdminForm
                initialValues={user}
                submitLabel="Update User"
                onSubmit={handleUpdate}
            />

            <ChangeUserRoleDialog
                open={roleDialogOpen}
                onClose={() => setRoleDialogOpen(false)}
                user={user}
                onSuccess={fetchUser}
            />

            <LockUserDialog
                open={lockDialogOpen}
                onClose={() => setLockDialogOpen(false)}
                user={user}
                onSuccess={fetchUser}
            />
        </>
    );
};

export default UserEditPage;