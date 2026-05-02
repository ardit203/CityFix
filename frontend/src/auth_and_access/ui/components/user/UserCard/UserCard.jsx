import { Box, Button, Card, CardActions, CardContent, Typography } from '@mui/material';
import InfoIcon from '@mui/icons-material/Info';
import EditIcon from '@mui/icons-material/Edit';
import DeleteIcon from '@mui/icons-material/Delete';
import {useNavigate} from "react-router";
import useUserActions from "../../../../hooks/useUserActions.js";


const ProductCard = ({ user }) => {
    const navigate = useNavigate();
    const { deleteUser } = useUserActions();

    const handleDelete = async () => {
        await deleteUser(user);
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
                    <Button startIcon={<EditIcon />} onClick={() => navigate(`/users/${user.id}/edit`)} color='warning'>Edit</Button>
                    <Button startIcon={<DeleteIcon />} onClick={handleDelete} color='error'>Delete</Button>
                </Box>
            </CardActions>
        </Card>
    );
};

export default ProductCard;