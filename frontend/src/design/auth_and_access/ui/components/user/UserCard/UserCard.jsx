import { Box, Button, Card, CardActions, CardContent, Typography, Avatar, Chip } from '@mui/material';
import InfoIcon from '@mui/icons-material/Info';
import EditIcon from '@mui/icons-material/Edit';
import DeleteIcon from '@mui/icons-material/Delete';
import PersonIcon from '@mui/icons-material/Person';
import { useNavigate } from 'react-router';

const UserCard = ({ user }) => {
    const navigate = useNavigate();

    return (
        <Card sx={{ maxWidth: '100%', display: 'flex', flexDirection: 'column', height: '100%' }}>
            <CardContent sx={{ flexGrow: 1 }}>
                <Box sx={{ display: 'flex', alignItems: 'center', mb: 2 }}>
                    <Avatar sx={{ bgcolor: 'primary.main', mr: 2 }}>
                        <PersonIcon />
                    </Avatar>
                    <Box>
                        <Typography variant='h6' sx={{ lineHeight: 1.2 }}>{user.name} {user.surname}</Typography>
                        <Typography variant='body2' color="text.secondary">@{user.username}</Typography>
                    </Box>
                </Box>
                
                <Box sx={{ mt: 2 }}>
                    <Chip 
                        label={user.role || 'USER'} 
                        size="small" 
                        color="secondary" 
                        variant="outlined" 
                        sx={{ mb: 1 }}
                    />
                    <Typography variant='body2' sx={{ display: 'flex', alignItems: 'center', gap: 1 }}>
                        <Typography component="span" sx={{ color: 'text.secondary', fontSize: '0.85rem' }}>ID:</Typography> {user.id}
                    </Typography>
                    {user.email && (
                        <Typography variant='body2' sx={{ mt: 0.5, opacity: 0.8 }}>
                            {user.email}
                        </Typography>
                    )}
                </Box>
            </CardContent>
            
            <CardActions sx={{ justifyContent: 'space-between', borderTop: '1px solid rgba(255,255,255,0.05)', pt: 2, pb: 2, px: 2 }}>
                <Button
                    size="small"
                    startIcon={<InfoIcon/>}
                    onClick={() => navigate(`/users/${user.id}`)}
                >
                    Details
                </Button>
                <Box sx={{ display: 'flex', gap: 1 }}>
                    <Button size="small" variant="outlined" color="warning" sx={{ minWidth: '40px', px: 1 }}>
                        <EditIcon fontSize="small"/>
                    </Button>
                    <Button size="small" variant="outlined" color="error" sx={{ minWidth: '40px', px: 1 }}>
                        <DeleteIcon fontSize="small"/>
                    </Button>
                </Box>
            </CardActions>
        </Card>
    );
};

export default UserCard;