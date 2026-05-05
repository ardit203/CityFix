import React, {useState} from 'react';
import {Box, Button, CircularProgress, MenuItem, Pagination, TextField} from "@mui/material";
import useDepartments from "../../../hooks/useDepartments.js";
import DepartmentGrid from "../../components/department/DepartmentGrid.jsx";
import {useNavigate} from "react-router";


const initialFilters = {
    page: 0,
    size: 10,
    sortBy: "id",
    id: "",
    text: "",
}

const DepartmentsPage = () => {
    const {departments, loading, pagination, fetchDepartmentsPaged} = useDepartments(true);
    const navigate = useNavigate();
    const [filters, setFilters] = useState(initialFilters);

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
        void fetchDepartmentsPaged(newFilters);
    };

    const handleClearFilters = () => {
        setFilters(initialFilters);
        void fetchDepartmentsPaged(initialFilters);
    };

    const handlePageChange = (event, value) => {
        const newFilters = {
            ...filters,
            page: value - 1
        };

        setFilters(newFilters);
        void fetchDepartmentsPaged(newFilters);
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
                    label="Search"
                    name="text"
                    value={filters.text}
                    onChange={handleFilterChange}
                    size="small"
                />

                <Button variant="contained" onClick={handleSearch}>
                    Search
                </Button>

                <Button variant="outlined" onClick={handleClearFilters}>
                    Clear
                </Button>
                <Button variant="contained" onClick={() => navigate("/departments/create")}>
                    Add
                </Button>
            </Box>
            <DepartmentGrid departments={departments} onDelete={() => fetchDepartmentsPaged(filters)}/>

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

export default DepartmentsPage;
