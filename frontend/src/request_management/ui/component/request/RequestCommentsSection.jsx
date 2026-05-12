import React, {useMemo, useState} from "react";
import {
    Alert,
    Box,
    Button,
    Card,
    CardContent,
    Checkbox,
    Chip,
    CircularProgress,
    Divider,
    FormControlLabel,
    IconButton,
    Stack,
    TextField,
    Tooltip,
    Typography
} from "@mui/material";
import {
    AddComment,
    Delete,
    Edit,
    Lock,
    Public,
    Save,
    Close
} from "@mui/icons-material";

import useAuth from "../../../../auth_and_access/hooks/auth/useAuth.js";
import useRequestComments from "../../../hooks/request/useRequestComments.js";
import useRequestCommentActions from "../../../hooks/request/useRequestCommentActions.js";
import {emptyRequestCommentDto} from "../../../dtos/requestCommentDto.js";

const staffRoles = ["ADMINISTRATOR", "MANAGER", "EMPLOYEE"];

const getUserRoles = (user) => {
    const roles = user?.roles || [];

    return roles.map((role) => {
        const roleName = typeof role === "string" ? role : role.authority;
        return roleName?.replace("ROLE_", "");
    }).filter(Boolean);
};

const formatDate = (value) => {
    if (!value) return "Unknown date";

    return new Intl.DateTimeFormat("en", {
        dateStyle: "medium",
        timeStyle: "short"
    }).format(new Date(value));
};

const RequestCommentsSection = ({requestId}) => {
    const {user} = useAuth();
    const currentUserId = Number(user?.sub);
    const isStaff = getUserRoles(user).some((role) => staffRoles.includes(role));
    const isAdmin = getUserRoles(user).includes("ADMINISTRATOR");

    const {comments, loading, error, fetchComments} = useRequestComments(requestId);
    const {createComment, updateComment, deleteComment, isExecuting} = useRequestCommentActions();

    const [form, setForm] = useState(emptyRequestCommentDto);
    const [editingId, setEditingId] = useState(null);
    const [editingForm, setEditingForm] = useState(emptyRequestCommentDto);

    const sortedComments = useMemo(() => {
        return [...comments].sort((a, b) => new Date(b.createdAt || 0) - new Date(a.createdAt || 0));
    }, [comments]);

    const handleSubmit = async (event) => {
        event.preventDefault();

        if (!form.content.trim()) return;

        await createComment(requestId, form, () => {
            setForm(emptyRequestCommentDto);
            fetchComments();
        });
    };

    const startEditing = (comment) => {
        setEditingId(comment.id);
        setEditingForm({
            content: comment.content,
            internal: comment.internal
        });
    };

    const cancelEditing = () => {
        setEditingId(null);
        setEditingForm(emptyRequestCommentDto);
    };

    const handleUpdate = async (commentId) => {
        if (!editingForm.content.trim()) return;

        await updateComment(requestId, commentId, editingForm, () => {
            cancelEditing();
            fetchComments();
        });
    };

    const handleDelete = async (commentId) => {
        await deleteComment(requestId, commentId, fetchComments);
    };

    return (
        <Card variant="outlined" sx={{borderRadius: 2}}>
            <CardContent>
                <Stack direction="row" alignItems="center" justifyContent="space-between" sx={{mb: 2}}>
                    <Box>
                        <Typography variant="h6" fontWeight={700}>
                            Comments
                        </Typography>
                        <Typography variant="body2" color="text.secondary">
                            Public discussion and staff-only notes for this request.
                        </Typography>
                    </Box>

                    <Chip label={`${comments.length} total`} size="small" variant="outlined" />
                </Stack>

                <Box component="form" onSubmit={handleSubmit} sx={{mb: 3}}>
                    <Stack spacing={2}>
                        <TextField
                            label="Add a comment"
                            value={form.content}
                            onChange={(event) => setForm((prev) => ({...prev, content: event.target.value}))}
                            multiline
                            minRows={3}
                            inputProps={{maxLength: 2000}}
                            disabled={isExecuting}
                            fullWidth
                        />

                        <Stack direction={{xs: "column", sm: "row"}} spacing={2} justifyContent="space-between" alignItems={{xs: "stretch", sm: "center"}}>
                            {isStaff ? (
                                <FormControlLabel
                                    control={
                                        <Checkbox
                                            checked={form.internal}
                                            onChange={(event) => setForm((prev) => ({...prev, internal: event.target.checked}))}
                                            disabled={isExecuting}
                                        />
                                    }
                                    label="Internal staff note"
                                />
                            ) : (
                                <Typography variant="body2" color="text.secondary">
                                    Your comment will be visible to staff handling the request.
                                </Typography>
                            )}

                            <Button
                                type="submit"
                                variant="contained"
                                startIcon={<AddComment />}
                                disabled={isExecuting || !form.content.trim()}
                            >
                                Add Comment
                            </Button>
                        </Stack>
                    </Stack>
                </Box>

                <Divider sx={{mb: 2}} />

                {loading && (
                    <Stack alignItems="center" sx={{py: 4}}>
                        <CircularProgress />
                    </Stack>
                )}

                {error && <Alert severity="error">{error}</Alert>}

                {!loading && !error && sortedComments.length === 0 && (
                    <Alert severity="info">No comments yet.</Alert>
                )}

                <Stack spacing={2}>
                    {sortedComments.map((comment) => {
                        const canManage = isAdmin || comment.authorId === currentUserId;
                        const isEditing = editingId === comment.id;

                        return (
                            <Box key={comment.id} sx={{p: 2, border: "1px solid", borderColor: "divider", borderRadius: 2}}>
                                <Stack direction="row" spacing={1.5} justifyContent="space-between" alignItems="flex-start">
                                    <Box sx={{minWidth: 0, flexGrow: 1}}>
                                        <Stack direction="row" spacing={1} alignItems="center" flexWrap="wrap" useFlexGap sx={{mb: 1}}>
                                            <Typography variant="subtitle2" fontWeight={700}>
                                                {comment.authorUsername}
                                            </Typography>
                                            <Typography variant="caption" color="text.secondary">
                                                {formatDate(comment.createdAt)}
                                            </Typography>
                                            <Chip
                                                icon={comment.internal ? <Lock /> : <Public />}
                                                label={comment.internal ? "Internal" : "Public"}
                                                color={comment.internal ? "warning" : "default"}
                                                size="small"
                                                variant="outlined"
                                            />
                                        </Stack>

                                        {isEditing ? (
                                            <Stack spacing={1.5}>
                                                <TextField
                                                    value={editingForm.content}
                                                    onChange={(event) => setEditingForm((prev) => ({...prev, content: event.target.value}))}
                                                    multiline
                                                    minRows={3}
                                                    inputProps={{maxLength: 2000}}
                                                    disabled={isExecuting}
                                                    fullWidth
                                                />

                                                {isStaff && (
                                                    <FormControlLabel
                                                        control={
                                                            <Checkbox
                                                                checked={editingForm.internal}
                                                                onChange={(event) => setEditingForm((prev) => ({...prev, internal: event.target.checked}))}
                                                                disabled={isExecuting}
                                                            />
                                                        }
                                                        label="Internal staff note"
                                                    />
                                                )}
                                            </Stack>
                                        ) : (
                                            <Typography variant="body2" sx={{whiteSpace: "pre-wrap"}}>
                                                {comment.content}
                                            </Typography>
                                        )}
                                    </Box>

                                    {canManage && (
                                        <Stack direction="row" spacing={0.5}>
                                            {isEditing ? (
                                                <>
                                                    <Tooltip title="Save">
                                                        <span>
                                                            <IconButton
                                                                color="primary"
                                                                onClick={() => handleUpdate(comment.id)}
                                                                disabled={isExecuting || !editingForm.content.trim()}
                                                            >
                                                                <Save />
                                                            </IconButton>
                                                        </span>
                                                    </Tooltip>
                                                    <Tooltip title="Cancel">
                                                        <IconButton onClick={cancelEditing} disabled={isExecuting}>
                                                            <Close />
                                                        </IconButton>
                                                    </Tooltip>
                                                </>
                                            ) : (
                                                <>
                                                    <Tooltip title="Edit">
                                                        <IconButton onClick={() => startEditing(comment)} disabled={isExecuting}>
                                                            <Edit />
                                                        </IconButton>
                                                    </Tooltip>
                                                    <Tooltip title="Delete">
                                                        <IconButton color="error" onClick={() => handleDelete(comment.id)} disabled={isExecuting}>
                                                            <Delete />
                                                        </IconButton>
                                                    </Tooltip>
                                                </>
                                            )}
                                        </Stack>
                                    )}
                                </Stack>
                            </Box>
                        );
                    })}
                </Stack>
            </CardContent>
        </Card>
    );
};

export default RequestCommentsSection;
