import {useState, useCallback} from "react";

const useFilters = (initialFilters, fetchFunction) => {
    const [filters, setFilters] = useState(initialFilters);

    const handleFilterChange = useCallback((event) => {
        const {name, value} = event.target;
        setFilters((prev) => ({...prev, [name]: value}));
    }, []);

    const handleSearch = useCallback(() => {
        const newFilters = {...filters, page: 0}; // Reset to page 0 on new search
        setFilters(newFilters);
        void fetchFunction(newFilters);
    }, [filters, fetchFunction]);

    const handleClearFilters = useCallback(() => {
        setFilters(initialFilters);
        void fetchFunction(initialFilters);
    }, [initialFilters, fetchFunction]);

    const handlePageChange = useCallback((event, value) => {
        const newFilters = {...filters, page: value - 1}; // MUI Pagination is 1-indexed, Spring Boot is 0-indexed
        setFilters(newFilters);
        void fetchFunction(newFilters);
    }, [filters, fetchFunction]);

    const handleSizeChange = useCallback((event) => {
        const newSize = parseInt(event.target.value, 10);
        // Reset to page 0 when changing size!
        const newFilters = { ...filters, size: newSize, page: 0 };
        setFilters(newFilters);
        void fetchFunction(newFilters);
    }, [filters, fetchFunction]);

    const handleSortChange = useCallback((event) => {
        const { name, value } = event.target;

        // Reset to page 0 when sorting changes!
        const newFilters = { ...filters, [name]: value, page: 0 };
        setFilters(newFilters);

        // Fetch immediately without waiting for them to click "Search"
        void fetchFunction(newFilters);
    }, [filters, fetchFunction]);


    return {
        filters,
        handleFilterChange,
        handleSearch,
        handleClearFilters,
        handlePageChange,
        handleSizeChange,
        handleSortChange
    };
};

export default useFilters;
