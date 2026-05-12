export const emptyRequestCommentDto = {
    content: "",
    internal: false
};

export const mapToRequestCommentDto = (data) => ({
    content: data.content?.trim() || "",
    internal: Boolean(data.internal)
});

export const mapToDisplayRequestCommentDto = (backendData) => {
    if (!backendData) return null;

    return {
        id: backendData.id,
        requestId: backendData.requestId,
        authorId: backendData.authorId,
        authorUsername: backendData.authorUsername || "Unknown user",
        content: backendData.content || "",
        internal: Boolean(backendData.internal),
        createdAt: backendData.createdAt || null,
        updatedAt: backendData.updatedAt || null
    };
};

export const mapRequestCommentList = (comments) => {
    if (!Array.isArray(comments)) return [];
    return comments.map(mapToDisplayRequestCommentDto);
};
