import React from "react";
import {Route} from "react-router";
import ReportsPage from "../ui/pages/ReportsPage.jsx";

export const reportRoutes = (
    <Route path="reports" element={<ReportsPage />} />
);
