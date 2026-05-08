import React, {useState} from 'react';
import {Box, MenuItem, TextField} from "@mui/material";
import {useNavigate} from "react-router";

import {
    initialUserFilters,
    userColumns,
    userSortOptions
} from "../components/user/UserConfig.jsx";
import useUsers from "../../hooks/user/useUsers.js";
import useUserActions from "../../hooks/user/useUserActions.js";
import useFilters from "../../../common/hooks/useFilters.js";
import FilterBar from "../../../common/ui/components/FilterBar.jsx";
import SortControls from "../../../common/ui/components/SortControls.jsx";
import LoadingBar from "../../../common/ui/components/LoadingBar.jsx";
import PaginatedDataView from "../../../common/ui/components/PaginatedDataView.jsx";
import AdminTable from "../../../common/ui/components/AdminTable.jsx";
import UserGrid from "../components/user/UserGrid.jsx";

const UsersPage = () => {
    const navigate = useNavigate();
    const {users, loading, pagination, fetchUsersPaged} = useUsers();
    const {deleteUser} = useUserActions();
    const [viewMode, setViewMode] = useState('table');

    const {
        filters,
        handleFilterChange,
        handleSearch,
        handleClearFilters,
        handlePageChange,
        handleSizeChange,
        handleSortChange
    } = useFilters(initialUserFilters, fetchUsersPaged);

    const handleViewChange = (event, nextView) => {
        if (nextView !== null) {
            setViewMode(nextView);
        }
    };
    console.log(users)

    return (
        <Box>
            <FilterBar
                onSearch={handleSearch}
                onClear={handleClearFilters}
                addLabel="Add Department"
                viewMode={viewMode}
                onViewChange={handleViewChange}
            >
                <TextField label="ID" name="id" value={filters.id} onChange={handleFilterChange} size="small"/>
                <TextField label="Username" name="username" value={filters.username} onChange={handleFilterChange}
                           size="small"/>
                <TextField label="Email" name="email" value={filters.email} onChange={handleFilterChange} size="small"/>
                <TextField
                    select
                    label="Role"
                    name="role"
                    value={filters.role}
                    onChange={handleFilterChange}
                    size="small"
                    sx={{minWidth: 140}} // Ensures it doesn't shrink too small when empty
                >
                    <MenuItem value="">All Roles</MenuItem>
                    <MenuItem value="ROLE_CITIZEN">Citizen</MenuItem>
                    <MenuItem value="ROLE_EMPLOYEE">Employee</MenuItem>
                    <MenuItem value="ROLE_MANAGER">Manager</MenuItem>
                    <MenuItem value="ROLE_ADMINISTRATOR">Administrator</MenuItem>
                </TextField>
                <SortControls
                    sortByValue={filters.sortBy}
                    sortDirValue={filters.sortDir}
                    onSortChange={handleSortChange}
                    options={userSortOptions}
                />
            </FilterBar>

            <LoadingBar loading={loading}/>

            <PaginatedDataView
                loading={loading}
                data={users}
                pagination={pagination}
                onPageChange={handlePageChange}
                onSizeChange={handleSizeChange}
                emptyMessage="No users found."
                emptySubMessage="Try clearing your filters or refresh the page."
            >
                {viewMode === 'table' ? (
                    <AdminTable
                        data={users}
                        columns={userColumns}
                        onInfo={(department) => navigate(`/users/${department.id}`)}
                        onEdit={(department) => navigate(`/users/${department.id}/edit`)}
                        onDelete={async (user) => {
                            await deleteUser(user.id, () => fetchUsersPaged(filters));
                        }}
                        onBulkDelete={async (selectedIds) => {
                            console.log("Deleting multiple IDs:", selectedIds);
                        }}
                    />
                ) : (
                    <UserGrid
                        users={users}
                        onDelete={() => fetchUsersPaged(filters)}
                    />
                )}
            </PaginatedDataView>
        </Box>
    );


};

export default UsersPage;
