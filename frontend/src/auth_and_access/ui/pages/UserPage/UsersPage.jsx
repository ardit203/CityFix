import {useState} from "react";
import {
    Box,
    Button,
    CircularProgress,
    MenuItem,
    Pagination,
    TextField
} from "@mui/material";
import UserGrid from "../../components/user/UserGrid/UserGrid.jsx";
import useUsers from "../../../hooks/useUsers.js";

const UsersPage = () => {
    const {users, loading, pagination, fetchUsers} = useUsers();

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
        const {name, value} = event.target;

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
    if (loading) {
        return (
            <Box sx={{ display: "flex", justifyContent: "center", alignItems: "center", minHeight: "60vh" }}>
                <CircularProgress />
            </Box>
        );
    }


    return (
        <Box>
            <Box
                sx={{
                    display: "flex",
                    gap: 2,
                    mb: 3,
                    flexWrap: "wrap"
                }}
            >
                <TextField
                    label="ID"
                    name="id"
                    value={filters.id}
                    onChange={handleFilterChange}
                    size="small"
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
                    sx={{minWidth: 160}}
                >
                    <MenuItem value="">All roles</MenuItem>
                    <MenuItem value="ADMIN">Admin</MenuItem>
                    <MenuItem value="MANAGER">Manager</MenuItem>
                    <MenuItem value="EMPLOYEE">Employee</MenuItem>
                    <MenuItem value="CITIZEN">Citizen</MenuItem>
                </TextField>

                <Button variant="contained" onClick={handleSearch}>
                    Search
                </Button>

                <Button variant="outlined" onClick={handleClearFilters}>
                    Clear
                </Button>
            </Box>
            <UserGrid users={users}/>

            <Box sx={{display: "flex", justifyContent: "center", mt: 3}}>
                <Pagination
                    count={pagination.totalPages}
                    page={pagination.page + 1}
                    onChange={handlePageChange}
                />
            </Box>
        </Box>
    );
};

export default UsersPage;