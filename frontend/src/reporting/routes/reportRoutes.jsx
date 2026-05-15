import React from "react";
import {Route} from "react-router";
import ReportsPage from "../ui/pages/ReportsPage.jsx";
import ProtectedRoute from "../../auth_and_access/ui/components/auth/ProtectedRoute.jsx";

export const reportRoutes = (
    <Route
        path="reports"
        element={<ProtectedRoute allowedRoles={["ADMINISTRATOR", "MANAGER"]} />}
    >
        <Route index element={<ReportsPage />} />
    </Route>
);