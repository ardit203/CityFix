import { Box, Button, Card, CardActions, CardContent, Typography } from '@mui/material';
import InfoIcon from '@mui/icons-material/Info';
import EditIcon from '@mui/icons-material/Edit';
import DeleteIcon from '@mui/icons-material/Delete';
import useConfirmedAction from "../../../../../common/hooks/useConfirmedAction.js";
import {useNavigate} from "react-router";


const ProductCard = ({ user }) => {
    const navigate = useNavigate();
    const { runConfirmedAction } = useConfirmedAction();


    const handleDelete = () => {
        runConfirmedAction({
            confirmOptions: {
                title: "Delete user?",
                message: `Are you sure you want to delete ${user.username}? This action cannot be undone.`,
                confirmText: "Delete",
                cancelText: "Cancel"
            },
            action: async () => {
                console.log("Delete user", user.id);
                // await userApi.deleteById(user.id);
            },
            successMessage: "User deleted successfully",
            navigateTo: "/users"
        })
    };

    return (
        <Card sx={{ maxWidth: 300 }}>
            <CardContent>
                <Typography variant='h5'>{user.name}</Typography>
                <Typography variant='subtitle1'>{user.surname}</Typography>
                <Typography variant='h6' sx={{ textAlign: 'right' }}>${user.username}</Typography>
            </CardContent>
            <CardActions sx={{ justifyContent: 'space-between' }}>
                <Button
                    startIcon={<InfoIcon />}
                    onClick={() => navigate(`/users/${user.id}`)}
                >
                    Info
                </Button>
                <Box>
                    <Button startIcon={<EditIcon />} color='warning'>Edit</Button>
                    <Button startIcon={<DeleteIcon />} onClick={handleDelete} color='error'>Delete</Button>
                </Box>
            </CardActions>
        </Card>
    );
};

export default ProductCard;