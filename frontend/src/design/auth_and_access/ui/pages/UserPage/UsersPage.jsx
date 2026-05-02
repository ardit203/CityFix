import { useState } from "react";
import {
    Box,
    Button,
    CircularProgress,
    MenuItem,
    Pagination,
    TextField,
    Typography,
    Card
} from "@mui/material";
import SearchIcon from '@mui/icons-material/Search';
import ClearIcon from '@mui/icons-material/Clear';
import UserGrid from "../../components/user/UserGrid/UserGrid.jsx";
import useUsers from "../../../hooks/useUsers.js";

const UsersPage = () => {
    const { users, loading, pagination, fetchUsers } = useUsers();

    const [filters, setFilters] = useState({
        page: 0,
        size: 10,
        sortBy: "id",
        id: "",
        username: "",
        email: "",
        role: ""
    });

    const handleFilterChange = (event) => {
        const { name, value } = event.target;

        setFilters((prev) => ({
            ...prev,
            [name]: value
        }));
    };

    const handleSearch = () => {
        const newFilters = {
            ...filters,
            page: 0
        };

        setFilters(newFilters);
        fetchUsers(newFilters);
    };

    const handleClearFilters = () => {
        const clearedFilters = {
            page: 0,
            size: 10,
            sortBy: "id",
            id: "",
            username: "",
            email: "",
            role: ""
        };

        setFilters(clearedFilters);
        fetchUsers(clearedFilters);
    };

    const handlePageChange = (event, value) => {
        const newFilters = {
            ...filters,
            page: value - 1
        };

        setFilters(newFilters);
        fetchUsers(newFilters);
    };

    return (
        <Box className="animate-fade-in-up delay-100">
            <Typography variant="h3" sx={{ mb: 4, fontWeight: 700 }} className="text-gradient">
                User Management
            </Typography>

            <Card
                sx={{
                    p: 3,
                    mb: 4,
                    display: "flex",
                    gap: 2,
                    flexWrap: "wrap",
                    alignItems: "center"
                }}
            >
                <TextField
                    label="ID"
                    name="id"
                    value={filters.id}
                    onChange={handleFilterChange}
                    size="small"
                    variant="outlined"
                />

                <TextField
                    label="Username"
                    name="username"
                    value={filters.username}
                    onChange={handleFilterChange}
                    size="small"
                />

                <TextField
                    label="Email"
                    name="email"
                    value={filters.email}
                    onChange={handleFilterChange}
                    size="small"
                />

                <TextField
                    select
                    label="Role"
                    name="role"
                    value={filters.role}
                    onChange={handleFilterChange}
                    size="small"
                    sx={{ minWidth: 160 }}
                >
                    <MenuItem value="">All roles</MenuItem>
                    <MenuItem value="ADMIN">Admin</MenuItem>
                    <MenuItem value="MANAGER">Manager</MenuItem>
                    <MenuItem value="EMPLOYEE">Employee</MenuItem>
                    <MenuItem value="CITIZEN">Citizen</MenuItem>
                </TextField>

                <Box sx={{ display: 'flex', gap: 1, ml: 'auto' }}>
                    <Button 
                        variant="contained" 
                        color="primary" 
                        onClick={handleSearch}
                        startIcon={<SearchIcon />}
                    >
                        Search
                    </Button>

                    <Button 
                        variant="outlined" 
                        color="secondary" 
                        onClick={handleClearFilters}
                        startIcon={<ClearIcon />}
                    >
                        Clear
                    </Button>
                </Box>
            </Card>

            {loading ? (
                <Box sx={{ display: 'flex', justifyContent: 'center', p: 5 }}>
                    <CircularProgress color="primary" />
                </Box>
            ) : (
                <>
                    <UserGrid users={users} />

                    <Box sx={{ display: "flex", justifyContent: "center", mt: 5, mb: 2 }}>
                        <Pagination
                            count={pagination.totalPages}
                            page={pagination.page + 1}
                            onChange={handlePageChange}
                            color="primary"
                            size="large"
                        />
                    </Box>
                </>
            )}
        </Box>
    );
};

export default UsersPage;