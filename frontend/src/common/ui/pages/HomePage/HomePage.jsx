import { Box, Button, LinearProgress, Paper, Stack, Typography } from '@mui/material';
import { Link } from 'react-router';
import AssignmentTurnedInIcon from '@mui/icons-material/AssignmentTurnedIn';
import GroupsIcon from '@mui/icons-material/Groups';
import InsightsIcon from '@mui/icons-material/Insights';
import MapIcon from '@mui/icons-material/Map';
import RouteIcon from '@mui/icons-material/Route';
import FactCheckIcon from '@mui/icons-material/FactCheck';
import EngineeringIcon from '@mui/icons-material/Engineering';
import TrendingUpIcon from '@mui/icons-material/TrendingUp';
import ArrowForwardIcon from '@mui/icons-material/ArrowForward';
import useAuth from "../../../../auth_and_access/hooks/auth/useAuth.js";

const HomePage = () => {
    const { user } = useAuth();


    const hasRole = (allowedRoles) => {
        if (!allowedRoles || allowedRoles.length === 0) {
            return true;
        }

        return user?.roles?.some(roleObj => {
            const roleName = typeof roleObj === "string" ? roleObj : roleObj.authority;
            const cleanRole = roleName?.replace("ROLE_", "");

            return allowedRoles.includes(roleName) || allowedRoles.includes(cleanRole);
        }) === true;
    };


    const commandStats = [
        {
            label: "Open intake",
            value: "Requests",
            icon: <AssignmentTurnedInIcon />,
            to: "/requests"
            // no allowedRoles = visible to everyone logged in
        },
        {
            label: "Field teams",
            value: "Staff",
            icon: <GroupsIcon />,
            to: "/staff",
            allowedRoles: ["ADMINISTRATOR", "MANAGER"]
        },
        {
            label: "Operational view",
            value: "Reports",
            icon: <InsightsIcon />,
            to: "/reports",
            allowedRoles: ["ADMINISTRATOR", "MANAGER"]
        },
        {
            label: "Coverage zones",
            value: "Areas",
            icon: <MapIcon />,
            to: "/municipalities",
            allowedRoles: ["ADMINISTRATOR"]
        }
    ];

    const visibleCommandStats = commandStats.filter(item => hasRole(item.allowedRoles));

    const workflow = [
        {label: 'Submit', detail: 'Citizen requests enter the queue', icon: <AssignmentTurnedInIcon />},
        {label: 'Route', detail: 'Categories and departments are assigned', icon: <RouteIcon />},
        {label: 'Resolve', detail: 'Staff action is tracked to completion', icon: <FactCheckIcon />}
    ];

    const focusPanels = [
        {title: 'Routing quality', value: 'Review', progress: 76, icon: <RouteIcon />},
        {title: 'Team allocation', value: 'Assign', progress: 64, icon: <EngineeringIcon />},
        {title: 'Service trend', value: 'Measure', progress: 82, icon: <TrendingUpIcon />}
    ];

    return (
        <Box className="home-dashboard">
            <Paper elevation={0} className="home-hero">
                <Box className="home-hero-copy">
                    <Typography className="dashboard-eyebrow">
                        Municipal operations workspace
                    </Typography>
                    <Typography variant="h1" className="home-hero-title">
                        Coordinate the city from intake to resolution.
                    </Typography>
                    <Typography className="home-hero-text">
                        CityFix brings requests, departments, staff, assignments, and reporting into one focused command surface for daily municipal work.
                    </Typography>

                    <Stack direction={{xs: 'column', sm: 'row'}} spacing={1.5} className="home-hero-actions">
                        <Button component={Link} to="/requests" variant="contained" size="large" endIcon={<ArrowForwardIcon />}>
                            Open request queue
                        </Button>
                        <Button component={Link} to="/reports" variant="outlined" size="large">
                            Review reports
                        </Button>
                    </Stack>
                </Box>

                <Box className="operations-board" aria-hidden="true">
                    <Box className="board-toolbar">
                        <span />
                        <span />
                        <span />
                    </Box>
                    <Box className="board-grid">
                        <Box className="board-panel panel-wide">
                            <Typography variant="caption">Queue health</Typography>
                            <Typography variant="h5">Requests routed by priority</Typography>
                            <Stack spacing={1.25} sx={{mt: 2}}>
                                {['High priority', 'In progress', 'Ready for review'].map((item, index) => (
                                    <Box key={item}>
                                        <Stack direction="row" justifyContent="space-between">
                                            <Typography variant="body2">{item}</Typography>
                                            <Typography variant="body2" fontWeight={800}>{[34, 58, 72][index]}%</Typography>
                                        </Stack>
                                        <LinearProgress variant="determinate" value={[34, 58, 72][index]} sx={{mt: 0.75}} />
                                    </Box>
                                ))}
                            </Stack>
                        </Box>
                        <Box className="board-panel">
                            <Typography variant="caption">Assignments</Typography>
                            <Typography variant="h4">12</Typography>
                            <Typography variant="body2">active teams</Typography>
                        </Box>
                        <Box className="board-panel">
                            <Typography variant="caption">Reports</Typography>
                            <Typography variant="h4">8</Typography>
                            <Typography variant="body2">views ready</Typography>
                        </Box>
                        <Box className="city-map-panel">
                            <span />
                            <span />
                            <span />
                            <span />
                            <span />
                            <span />
                        </Box>
                    </Box>
                </Box>
            </Paper>

            <Box className="command-card-grid">
                {visibleCommandStats.map((item) => (
                    <Paper
                        component={Link}
                        to={item.to}
                        elevation={0}
                        className="command-card"
                        key={item.label}
                    >
                        <Box className="command-card-icon">{item.icon}</Box>

                        <Box>
                            <Typography variant="h6">{item.value}</Typography>
                            <Typography variant="body2" color="text.secondary">
                                {item.label}
                            </Typography>
                        </Box>

                        <ArrowForwardIcon className="command-card-arrow" />
                    </Paper>
                ))}
            </Box>

            <Box className="home-insight-grid">
                <Paper elevation={0} className="section-card workflow-panel">
                    <Box>
                        <Typography className="dashboard-eyebrow">Operating flow</Typography>
                        <Typography variant="h4">From report to resolution</Typography>
                    </Box>
                    <Box className="workflow-lanes">
                        {workflow.map((item) => (
                            <Box className="workflow-step" key={item.label}>
                                <Box className="workflow-icon">{item.icon}</Box>
                                <Box>
                                    <Typography variant="h6">{item.label}</Typography>
                                    <Typography variant="body2" color="text.secondary">{item.detail}</Typography>
                                </Box>
                            </Box>
                        ))}
                    </Box>
                </Paper>

                <Paper elevation={0} className="section-card focus-panel">
                    <Typography className="dashboard-eyebrow">Daily focus</Typography>
                    <Stack spacing={2}>
                        {focusPanels.map((item) => (
                            <Box className="focus-row" key={item.title}>
                                <Box className="focus-icon">{item.icon}</Box>
                                <Box sx={{minWidth: 0, flex: 1}}>
                                    <Stack direction="row" justifyContent="space-between" spacing={2}>
                                        <Typography variant="subtitle2">{item.title}</Typography>
                                        <Typography variant="subtitle2" color="primary">{item.value}</Typography>
                                    </Stack>
                                    <LinearProgress variant="determinate" value={item.progress} sx={{mt: 1}} />
                                </Box>
                            </Box>
                        ))}
                    </Stack>
                </Paper>
            </Box>
        </Box>
    );
};

export default HomePage;
