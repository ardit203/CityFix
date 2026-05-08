import React, {useState} from 'react';
import {Box, TextField} from "@mui/material";
import {useNavigate} from "react-router";
import useFilters from "../../../../common/hooks/useFilters.js";
import FilterBar from "../../../../common/ui/components/FilterBar.jsx";
import LoadingBar from "../../../../common/ui/components/LoadingBar.jsx";
import AdminTable from "../../../../common/ui/components/AdminTable.jsx";
import PaginatedDataView from "../../../../common/ui/components/PaginatedDataView.jsx";
import SortControls from "../../../../common/ui/components/SortControls.jsx";
import {
    municipalityColumns,
    municipalitySortOptions
} from "../../components/municipality/MunicipalityConfig.jsx";
import { emptyMunicipalityFilter } from "../../../dtos/filterDto.js";
import useMunicipalityActions from "../../../hooks/municipality/useMunicipalityActions.js";
import useMunicipalities from "../../../hooks/municipality/useMunicipalities.js";
import MunicipalityGrid from "../../components/municipality/MunicipalityGrid.jsx";

const MunicipalitiesPage = () => {
    const navigate = useNavigate();
    const {municipalities, loading, pagination, fetchMunicipalitiesPaged} = useMunicipalities();
    const {deleteMunicipality, deleteMunicipalitiesBulk} = useMunicipalityActions();
    const [viewMode, setViewMode] = useState('table');

    const {
        filters,
        handleFilterChange,
        handleSearch,
        handleClearFilters,
        handlePageChange,
        handleSizeChange,
        handleSortChange
    } = useFilters(emptyMunicipalityFilter, fetchMunicipalitiesPaged);

    const handleViewChange = (event, nextView) => {
        if (nextView !== null) {
            setViewMode(nextView);
        }
    };

    return (
        <Box>
            <FilterBar
                onSearch={handleSearch}
                onClear={handleClearFilters}
                onAdd={() => navigate("/municipalities/create")}
                addLabel="Add Municipality"
                viewMode={viewMode}
                onViewChange={handleViewChange}
            >
                <TextField label="ID" name="id" value={filters.id} onChange={handleFilterChange} size="small"/>
                <TextField label="Code" name="code" value={filters.code} onChange={handleFilterChange} size="small"/>
                <TextField label="Name" name="name" value={filters.name} onChange={handleFilterChange} size="small"/>
                <SortControls
                    sortByValue={filters.sortBy}
                    sortDirValue={filters.sortDir}
                    onSortChange={handleSortChange}
                    options={municipalitySortOptions}
                />
            </FilterBar>

            <LoadingBar loading={loading}/>

            <PaginatedDataView
                loading={loading}
                data={municipalities}
                pagination={pagination}
                onPageChange={handlePageChange}
                onSizeChange={handleSizeChange}
                emptyMessage="No Municipalities found."
                emptySubMessage="Try clearing your filters or creating a new municipality."
            >
                {viewMode === 'table' ? (
                    <AdminTable
                        data={municipalities}
                        columns={municipalityColumns}
                        onInfo={(municipality) => navigate(`/municipalities/${municipality.id}`)}
                        onEdit={(municipality) => navigate(`/municipalities/${municipality.id}/edit`)}
                        onDelete={async (municipality) => {
                            await deleteMunicipality(municipality.id, () => fetchMunicipalitiesPaged(filters));
                        }}
                        onBulkDelete={async (selectedIds) => {
                            await deleteMunicipalitiesBulk(selectedIds, () => fetchMunicipalitiesPaged(filters));
                        }}
                    />
                ) : (
                    <MunicipalityGrid
                        municipalities={municipalities}
                        onDelete={() => fetchMunicipalitiesPaged(filters)}
                    />
                )}
            </PaginatedDataView>
        </Box>
    );


};

export default MunicipalitiesPage;
