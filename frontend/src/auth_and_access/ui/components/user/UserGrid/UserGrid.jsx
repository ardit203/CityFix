import { Grid } from '@mui/material';
import UserCard from "../UserCard/UserCard.jsx";

const UserGrid = ({ users, onDelete }) => {
    return (
        <Grid container spacing={{ xs: 2, md: 3 }}>
            {users.map((user) => (
                <Grid key={user.id} size={{ xs: 12, sm: 6, md: 4, lg: 3 }}>
                    <UserCard user={user} onDelete={onDelete}/>
                </Grid>
            ))}
        </Grid>
    );
};

export default UserGrid;