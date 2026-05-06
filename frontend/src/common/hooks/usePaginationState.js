import {useState, useCallback} from "react";

const usePaginationState = () => {
    const [pagination, setPagination] = useState({
        totalPages: 0,
        totalElements: 0,
        page: 0,
        size: 10,
        first: true,
        last: true
    });

    const updatePagination = useCallback((data) => {
        setPagination({
            totalPages: data.totalPages,
            totalElements: data.totalElements,
            page: data.number,
            size: data.size,
            first: data.first,
            last: data.last
        });
    }, []);

    return {pagination, updatePagination};
};
export default usePaginationState;
