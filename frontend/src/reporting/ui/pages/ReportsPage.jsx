import React from "react";
import {
    Box,
    Card,
    CardContent,
    CircularProgress,
    FormControl,
    Grid,
    InputLabel,
    LinearProgress,
    MenuItem,
    Paper,
    Select,
    Stack,
    TextField,
    Typography
} from "@mui/material";

import FilterBar from "../../../common/ui/components/FilterBar.jsx";
import LoadingBar from "../../../common/ui/components/LoadingBar.jsx";
import useFilters from "../../../common/hooks/useFilters.js";
import useDepartments from "../../../administration/hooks/department/useDepartments.js";
import useMunicipalities from "../../../administration/hooks/municipality/useMunicipalities.js";
import useCategories from "../../../administration/hooks/category/useCategories.js";
import useRequestReportSummary from "../../hooks/useRequestReportSummary.js";
import {emptyRequestFilter} from "../../../request_management/dtos/filterDto.js";

const statuses = ["SUBMITTED", "IN_REVIEW", "ASSIGNED", "IN_PROGRESS", "RESOLVED", "REJECTED", "CANCELED"];
const priorities = ["LOW", "MEDIUM", "HIGH"];
const routingStatuses = ["PENDING_REVIEW", "CONFIRMED", "REJECTED"];

const formatLabel = (value) => {
    if (!value) return "Not set";
    return value.replaceAll("_", " ").toLowerCase().replace(/\b\w/g, (char) => char.toUpperCase());
};

const StatCard = ({label, value}) => (
    <Card variant="outlined" sx={{height: "100%", borderRadius: 2}}>
        <CardContent>
            <Typography variant="body2" color="text.secondary">
                {label}
            </Typography>
            <Typography variant="h4" fontWeight={700}>
                {value ?? 0}
            </Typography>
        </CardContent>
    </Card>
);

const CountList = ({title, rows, total}) => (
    <Card variant="outlined" sx={{height: "100%", borderRadius: 2}}>
        <CardContent>
            <Stack spacing={2}>
                <Typography variant="h6" fontWeight={700}>
                    {title}
                </Typography>

                {rows.length === 0 ? (
                    <Typography variant="body2" color="text.secondary">
                        No data.
                    </Typography>
                ) : rows.map((row) => {
                    const percent = total > 0 ? Math.round((row.count / total) * 100) : 0;
                    return (
                        <Box key={row.label}>
                            <Stack direction="row" justifyContent="space-between" spacing={2}>
                                <Typography variant="body2">{formatLabel(row.label)}</Typography>
                                <Typography variant="body2" fontWeight={700}>{row.count}</Typography>
                            </Stack>
                            <LinearProgress variant="determinate" value={percent} sx={{mt: 0.75, height: 7, borderRadius: 1}} />
                        </Box>
                    );
                })}
            </Stack>
        </CardContent>
    </Card>
);

const ReportsPage = () => {
    const {summary, loading, fetchSummary} = useRequestReportSummary();
    const {departments, loading: loadingDepartments} = useDepartments({paged: false});
    const {municipalities, loading: loadingMunicipalities} = useMunicipalities({paged: false});
    const {categories, loading: loadingCategories} = useCategories({paged: false});

    const {
        filters,
        handleFilterChange,
        handleSearch,
        handleClearFilters
    } = useFilters(emptyRequestFilter, fetchSummary);

    return (
        <Box>
            <Paper elevation={2} sx={{p: {xs: 2, md: 3}, borderRadius: 3, mb: 3}}>
                <Typography variant="h4" fontWeight={700}>
                    Reports
                </Typography>
                <Typography variant="body2" color="text.secondary">
                    Request volume, routing, priority, and workload overview.
                </Typography>
            </Paper>

            <FilterBar onSearch={handleSearch} onClear={handleClearFilters}>
                <TextField label="Search" name="text" value={filters.text} onChange={handleFilterChange} size="small" />

                <FormControl size="small" sx={{minWidth: 150}}>
                    <InputLabel>Status</InputLabel>
                    <Select name="status" value={filters.status} label="Status" onChange={handleFilterChange}>
                        <MenuItem value=""><em>All Statuses</em></MenuItem>
                        {statuses.map((status) => <MenuItem key={status} value={status}>{formatLabel(status)}</MenuItem>)}
                    </Select>
                </FormControl>

                <FormControl size="small" sx={{minWidth: 150}}>
                    <InputLabel>Priority</InputLabel>
                    <Select name="priority" value={filters.priority} label="Priority" onChange={handleFilterChange}>
                        <MenuItem value=""><em>All Priorities</em></MenuItem>
                        {priorities.map((priority) => <MenuItem key={priority} value={priority}>{formatLabel(priority)}</MenuItem>)}
                    </Select>
                </FormControl>

                <FormControl size="small" sx={{minWidth: 170}}>
                    <InputLabel>Routing</InputLabel>
                    <Select name="routingStatus" value={filters.routingStatus} label="Routing" onChange={handleFilterChange}>
                        <MenuItem value=""><em>All Routing</em></MenuItem>
                        {routingStatuses.map((status) => <MenuItem key={status} value={status}>{formatLabel(status)}</MenuItem>)}
                    </Select>
                </FormControl>

                <FormControl size="small" sx={{minWidth: 160}}>
                    <InputLabel>Department</InputLabel>
                    <Select name="departmentId" value={filters.departmentId} label="Department" onChange={handleFilterChange} disabled={loadingDepartments}>
                        <MenuItem value=""><em>All Departments</em></MenuItem>
                        {loadingDepartments ? <MenuItem disabled value="loading"><CircularProgress size={18} sx={{mr: 1}} /> Loading...</MenuItem> :
                            departments.map((item) => <MenuItem key={item.id} value={item.id}>{item.name}</MenuItem>)}
                    </Select>
                </FormControl>

                <FormControl size="small" sx={{minWidth: 160}}>
                    <InputLabel>Municipality</InputLabel>
                    <Select name="municipalityId" value={filters.municipalityId} label="Municipality" onChange={handleFilterChange} disabled={loadingMunicipalities}>
                        <MenuItem value=""><em>All Municipalities</em></MenuItem>
                        {loadingMunicipalities ? <MenuItem disabled value="loading"><CircularProgress size={18} sx={{mr: 1}} /> Loading...</MenuItem> :
                            municipalities.map((item) => <MenuItem key={item.id} value={item.id}>{item.code || item.name}</MenuItem>)}
                    </Select>
                </FormControl>

                <FormControl size="small" sx={{minWidth: 150}}>
                    <InputLabel>Category</InputLabel>
                    <Select name="categoryId" value={filters.categoryId} label="Category" onChange={handleFilterChange} disabled={loadingCategories}>
                        <MenuItem value=""><em>All Categories</em></MenuItem>
                        {loadingCategories ? <MenuItem disabled value="loading"><CircularProgress size={18} sx={{mr: 1}} /> Loading...</MenuItem> :
                            categories.map((item) => <MenuItem key={item.id} value={item.id}>{item.name}</MenuItem>)}
                    </Select>
                </FormControl>
            </FilterBar>

            <LoadingBar loading={loading} />

            <Grid container spacing={2.5}>
                <Grid size={{xs: 12, md: 4}}>
                    <StatCard label="Total Requests" value={summary.totalRequests} />
                </Grid>
                <Grid size={{xs: 12, md: 4}}>
                    <StatCard label="Assigned Requests" value={summary.assignedRequests} />
                </Grid>
                <Grid size={{xs: 12, md: 4}}>
                    <StatCard label="Unassigned Requests" value={summary.unassignedRequests} />
                </Grid>

                <Grid size={{xs: 12, md: 4}}>
                    <CountList title="By Status" rows={summary.byStatus} total={summary.totalRequests} />
                </Grid>
                <Grid size={{xs: 12, md: 4}}>
                    <CountList title="By Priority" rows={summary.byPriority} total={summary.totalRequests} />
                </Grid>
                <Grid size={{xs: 12, md: 4}}>
                    <CountList title="By Routing" rows={summary.byRoutingStatus} total={summary.totalRequests} />
                </Grid>
                <Grid size={{xs: 12, md: 4}}>
                    <CountList title="By Department" rows={summary.byDepartment} total={summary.totalRequests} />
                </Grid>
                <Grid size={{xs: 12, md: 4}}>
                    <CountList title="By Municipality" rows={summary.byMunicipality} total={summary.totalRequests} />
                </Grid>
                <Grid size={{xs: 12, md: 4}}>
                    <CountList title="By Category" rows={summary.byCategory} total={summary.totalRequests} />
                </Grid>
            </Grid>
        </Box>
    );
};

export default ReportsPage;
