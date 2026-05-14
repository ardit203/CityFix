import { useState } from "react";
import { useNavigate, useParams } from "react-router";
import {
    Box,
    Button,
    CircularProgress,
    Typography
} from "@mui/material";
import {
    AdminPanelSettings,
    Lock,
    LockOpen
} from "@mui/icons-material";

// Adjust these paths to match your actual structure!
import useUserDetails from "../../hooks/user/useUserDetails.js";
import useUserActions from "../../hooks/user/useUserActions.js";
import UserForm from "../components/user/UserForm.jsx";
import { mapDisplayToAdminUpdateUserDto } from "../../dtos/userDto.js";
import LockUserDialog from "../components/user/LockUserDialog.jsx";
import ChangeUserRoleDialog from "../components/user/ChangeUserRoleDialog.jsx";

// Import the common ActionBar to keep design consistent!
import ActionBar from "../../../common/ui/components/ActionBar.jsx";

const UserEditPage = () => {
    const { id } = useParams();
    const navigate = useNavigate();
    const { user, loading, error, fetchUser } = useUserDetails(id);
    const { unlockUser, updateUser, lockUser, changeRole } = useUserActions();
    const [roleDialogOpen, setRoleDialogOpen] = useState(false);
    const [lockDialogOpen, setLockDialogOpen] = useState(false);

    const handleUnlock = async () => {
        return await unlockUser(id, fetchUser);
    };

    const handleUpdate = async (requestBody) => {
        await updateUser(id, requestBody, fetchUser);
    };

    const handleLock = async (until) => {
        return await lockUser(id, until, fetchUser);
    };

    const handleChangeRole = async (role) => {
        return await changeRole(id, role, fetchUser);
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
                <Button sx={{ mt: 2 }} variant="outlined" onClick={() => navigate("/users")}>
                    Back to Users
                </Button>
            </Box>
        );
    }

    if (!user) {
        return <Typography>User not found.</Typography>;
    }

    return (
        <Box sx={{ maxWidth: 1000, mx: 'auto', p: 3 }}>
            
            {/* The beautiful ActionBar instead of standard Stack! */}
            <ActionBar 
                title={`Edit User: @${user.username}`}
                onBack={() => navigate(`/users/${user.id}`)}
                backLabel="Back to User"
                onSecondaryBack={() => navigate("/users")}
                secondaryBackLabel="Back to Users"
            >
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
                    {user.locked ? "Unlock User" : "Lock User"}
                </Button>
            </ActionBar>

            <UserForm
                initialValues={mapDisplayToAdminUpdateUserDto(user)}
                submitLabel="Update User"
                onSubmit={handleUpdate}
            />

            {roleDialogOpen && (
                <ChangeUserRoleDialog
                    open={roleDialogOpen}
                    onClose={() => setRoleDialogOpen(false)}
                    user={user}
                    onSubmit={handleChangeRole}
                />
            )}

            {lockDialogOpen && (
                <LockUserDialog
                    open={lockDialogOpen}
                    onClose={() => setLockDialogOpen(false)}
                    onSubmit={handleLock}
                />
            )}
        </Box>
    );
};

export default UserEditPage;
