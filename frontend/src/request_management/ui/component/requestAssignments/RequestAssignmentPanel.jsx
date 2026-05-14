import React, {useEffect, useMemo, useState} from "react";
import {
    Box,
    Button,
    Card,
    CardContent,
    CircularProgress,
    Dialog,
    DialogActions,
    DialogContent,
    DialogTitle,
    FormControl,
    IconButton,
    InputLabel,
    MenuItem,
    Select,
    Stack,
    Typography
} from "@mui/material";
import AssignmentIndIcon from "@mui/icons-material/AssignmentInd";
import PersonAddIcon from "@mui/icons-material/PersonAdd";
import PersonRemoveIcon from "@mui/icons-material/PersonRemove";

import staffService from "../../../../administration/services/staffService.js";
import useRequestAssignments from "../../../hooks/requestAssignments/useRequestAssignments.js";
import useRequestAssignmentActions from "../../../hooks/requestAssignments/useRequestAssignmentActions.js";

const formatPerson = (assignment) => {
    const fullName = [assignment.employeeName, assignment.employeeSurname].filter(Boolean).join(" ");
    return fullName || assignment.employeeUsername || `Employee #${assignment.employeeId}`;
};

const RequestAssignmentPanel = ({request, canManage}) => {
    const [dialogOpen, setDialogOpen] = useState(false);
    const [employeeId, setEmployeeId] = useState("");
    const [staff, setStaff] = useState([]);
    const [loadingStaff, setLoadingStaff] = useState(false);

    const {
        assignments,
        loading,
        fetchAssignmentsByRequest
    } = useRequestAssignments({requestId: request.id});

    const {
        assignEmployee,
        removeAssignment,
        removeAllAssignments,
        isExecuting
    } = useRequestAssignmentActions();

    const assignedEmployeeIds = useMemo(
        () => assignments.map((assignment) => assignment.employeeId),
        [assignments]
    );

    const availableStaff = useMemo(
        () => staff.filter((item) => !assignedEmployeeIds.includes(item.userId)),
        [staff, assignedEmployeeIds]
    );

    useEffect(() => {
        if (!dialogOpen) return;

        const fetchStaff = async () => {
            setLoadingStaff(true);
            try {
                const response = await staffService.findAllPaged({
                    departmentId: request.departmentId || "",
                    municipalityId: request.municipalityId || "",
                    size: 100,
                    sortBy: "username",
                    sortDir: "asc"
                });
                setStaff(response.data.content || []);
            } finally {
                setLoadingStaff(false);
            }
        };

        void fetchStaff();
    }, [dialogOpen, request.departmentId, request.municipalityId]);

    const handleAssign = async () => {
        const result = await assignEmployee(request.id, {employeeId}, () => {
            setDialogOpen(false);
            setEmployeeId("");
            void fetchAssignmentsByRequest();
        });

        if (!result) return;
    };

    return (
        <Card variant="outlined" sx={{height: "100%", borderRadius: 2}}>
            <CardContent>
                <Stack spacing={2}>
                    <Stack direction="row" alignItems="center" justifyContent="space-between" spacing={2}>
                        <Stack direction="row" alignItems="center" spacing={1}>
                            <AssignmentIndIcon color="primary" />
                            <Typography variant="h6" fontWeight={700}>
                                Assignment
                            </Typography>
                        </Stack>

                        {canManage && (
                            <Button
                                size="small"
                                variant="contained"
                                startIcon={<PersonAddIcon />}
                                onClick={() => setDialogOpen(true)}
                            >
                                Assign New
                            </Button>
                        )}
                    </Stack>

                    {loading ? (
                        <Box sx={{display: "flex", justifyContent: "center", py: 2}}>
                            <CircularProgress size={24} />
                        </Box>
                    ) : assignments.length === 0 ? (
                        <Typography variant="body2" color="text.secondary">
                            No assignee yet.
                        </Typography>
                    ) : (
                        <Stack spacing={1}>
                            {assignments.map((assignment) => (
                                <Stack
                                    key={assignment.id}
                                    direction="row"
                                    alignItems="center"
                                    justifyContent="space-between"
                                    spacing={2}
                                    sx={{border: "1px solid", borderColor: "divider", borderRadius: 1, p: 1.25}}
                                >
                                    <Box sx={{minWidth: 0}}>
                                        <Typography variant="body2" fontWeight={700} noWrap>
                                            {formatPerson(assignment)}
                                        </Typography>
                                        <Typography variant="caption" color="text.secondary">
                                            @{assignment.employeeUsername}
                                        </Typography>
                                    </Box>

                                    {canManage && (
                                        <IconButton
                                            size="small"
                                            color="error"
                                            onClick={() => removeAssignment(request.id, assignment.id, fetchAssignmentsByRequest)}
                                            disabled={isExecuting}
                                        >
                                            <PersonRemoveIcon fontSize="small" />
                                        </IconButton>
                                    )}
                                </Stack>
                            ))}
                        </Stack>
                    )}

                    {canManage && assignments.length > 1 && (
                        <Button
                            color="error"
                            variant="outlined"
                            startIcon={<PersonRemoveIcon />}
                            onClick={() => removeAllAssignments(request.id, fetchAssignmentsByRequest)}
                            disabled={isExecuting}
                        >
                            Remove All Assignees
                        </Button>
                    )}
                </Stack>
            </CardContent>

            <Dialog open={dialogOpen} onClose={() => setDialogOpen(false)} fullWidth maxWidth="sm">
                <DialogTitle>Assign Request</DialogTitle>
                <DialogContent>
                    <FormControl fullWidth sx={{mt: 1}}>
                        <InputLabel>Employee</InputLabel>
                        <Select
                            value={employeeId}
                            label="Employee"
                            onChange={(event) => setEmployeeId(event.target.value)}
                            disabled={loadingStaff}
                        >
                            {loadingStaff ? (
                                <MenuItem disabled value="">
                                    <CircularProgress size={18} sx={{mr: 1}} /> Loading employees...
                                </MenuItem>
                            ) : availableStaff.length === 0 ? (
                                <MenuItem disabled value="">
                                    No available employees
                                </MenuItem>
                            ) : (
                                availableStaff.map((item) => (
                                    <MenuItem key={item.id} value={item.userId}>
                                        {item.name} {item.surname} (@{item.username})
                                    </MenuItem>
                                ))
                            )}
                        </Select>
                    </FormControl>
                </DialogContent>
                <DialogActions>
                    <Button onClick={() => setDialogOpen(false)}>Cancel</Button>
                    <Button
                        variant="contained"
                        onClick={handleAssign}
                        disabled={!employeeId || isExecuting}
                    >
                        Assign
                    </Button>
                </DialogActions>
            </Dialog>
        </Card>
    );
};

export default RequestAssignmentPanel;
