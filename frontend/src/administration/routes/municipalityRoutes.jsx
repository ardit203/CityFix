import React from 'react';
import { Route } from "react-router";
import MunicipalitiesPage from "../ui/pages/municipality/MunicipalitiesPage.jsx";
import MunicipalityDetailsPage from "../ui/pages/municipality/MunicipalityDetailsPage.jsx";
import MunicipalityCreatePage from "../ui/pages/municipality/MunicipalityCreatePage.jsx";
import MunicipalityEditPage from "../ui/pages/municipality/MunicipalityEditPage.jsx";
import ProtectedRoute from "../../auth_and_access/ui/components/auth/ProtectedRoute.jsx";

export const municipalityRoutes = (
    <Route path="municipalities" element={<ProtectedRoute allowedRoles={["ADMINISTRATOR"]} />}>
        <Route index element={<MunicipalitiesPage />} />
        <Route path="create" element={<MunicipalityCreatePage />} />
        <Route path=":id" element={<MunicipalityDetailsPage />} />
        <Route path=":id/edit" element={<MunicipalityEditPage />} />
    </Route>
);
