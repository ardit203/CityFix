import React from 'react';
import { FormControl, InputLabel, Select, MenuItem, Box } from "@mui/material";

const SortControls = ({
                          sortByValue,
                          // sortDirValue,
                          onSortChange,
                          options = []
                      }) => {
    return (
        <Box sx={{ display: 'flex', gap: 2 }}>
            <FormControl size="small" sx={{ minWidth: 140 }}>
                <InputLabel>Sort By</InputLabel>
                <Select
                    name="sortBy"
                    value={sortByValue || ""}
                    label="Sort By"
                    onChange={onSortChange}
                >
                    {options.map((option) => (
                        <MenuItem key={option.value} value={option.value}>
                            {option.label}
                        </MenuItem>
                    ))}
                </Select>
            </FormControl>

            {/*<FormControl size="small" sx={{ minWidth: 120 }}>*/}
            {/*    <InputLabel>Order</InputLabel>*/}
            {/*    <Select*/}
            {/*        name="sortDir"*/}
            {/*        value={sortDirValue || "asc"}*/}
            {/*        label="Order"*/}
            {/*        onChange={onSortChange}*/}
            {/*    >*/}
            {/*        <MenuItem value="asc">Ascending</MenuItem>*/}
            {/*        <MenuItem value="desc">Descending</MenuItem>*/}
            {/*    </Select>*/}
            {/*</FormControl>*/}
        </Box>
    );
};

export default SortControls;
