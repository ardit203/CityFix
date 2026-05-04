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
import LockUserDialog from "../../components/user/LockUserDialog/LockUserDialog.jsx";
import ChangeUserRoleDialog from "../../components/user/ChangeRoleDialog/ChangeRoleDialog.jsx";
import UserAdminForm from "../../components/user/UserAdminForm/UserAdminForm.jsx";
import useUserActions from "../../../hooks/useUserActions.js";


const UserEditPage = () => {
    const { id } = useParams();
    const navigate = useNavigate();

    const { user, loading, error, fetchUser } = useUserDetails(id);

    const [roleDialogOpen, setRoleDialogOpen] = useState(false);
    const [lockDialogOpen, setLockDialogOpen] = useState(false);
    const { updateUser, unlockUser } = useUserActions();


    const handleUpdate = async (formData) => {
        await updateUser(user, formData, fetchUser);
    };

    const handleUnlock = async () => {
        await unlockUser(user, fetchUser);
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
                    onClick={() => navigate(`/users/${user.id}`)}
                >
                    Back to User
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