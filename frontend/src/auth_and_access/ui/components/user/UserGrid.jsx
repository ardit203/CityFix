import {Grid} from "@mui/material";
import React from "react";
import UserCard from "./UserCard.jsx";

const UserGrid = ({users, onDelete}) => {
    return (
        <Grid container spacing={{xs: 2, md: 2.5}} className="entity-grid">
            {users.map((user) => (
                <Grid key={user.id} size={{xs: 12, sm: 6, lg: 4, xl: 3}}>
                    <UserCard user={user} onDelete={onDelete}/>
                </Grid>
            ))}
        </Grid>
    );
};

export default UserGrid;
