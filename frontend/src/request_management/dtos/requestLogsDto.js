export const mapToDisplayRequestLogBasicDto = (backendData) => {
    if (!backendData) return null;

    return {
        id: backendData.id,
        action: backendData.action,
        createdAt: backendData.createdAt,
    };
};


export const mapToDisplayRequestLogDto = (backendData) => {
    if (!backendData) return null;

    return {
        id: backendData.id,
        requestId: backendData.requestId,
        userId: backendData.userId,
        action: backendData.action,
        newValue: backendData.newValue,
        oldValue: backendData.oldValue,
        note: backendData.note,
        createdAt: backendData.createdAt,
    };
};


/**
 * Maps the paginated backend response (used in your Grids/Tables).
 * Matches the backend: DisplayCategoryPageableDto
 */
export const mapToDisplayCategoryPageableDto = (backendData) => {
    if (!backendData) return null;

    return {
        id: backendData.id,
        action: backendData.action,
        newValue: backendData.newValue,
        oldValue: backendData.oldValue,
        createdAt: backendData.createdAt,
    };
};

/**
 * Utility function to map an entire array of paginated results for the grid in one go.
 */
export const mapRequestLogPageableList = (contentArray) => {
    if (!Array.isArray(contentArray)) return [];
    return contentArray.map(mapToDisplayCategoryPageableDto);
};