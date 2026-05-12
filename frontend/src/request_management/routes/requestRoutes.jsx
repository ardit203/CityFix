import React from 'react';
import { Route } from "react-router";
import RequestsPage from "../ui/pages/request/RequestsPage.jsx";
import RequestCreatePage from "../ui/pages/request/RequestCreatePage.jsx";
import RequestDetailsPage from "../ui/pages/request/RequestDetailsPage.jsx";
import RequestEditPage from "../ui/pages/request/RequestEditPage.jsx";
import RequestLogsPage from "../ui/pages/requestLogs/RequestLogsPage.jsx";
import RequestLogDetailsPage from "../ui/pages/requestLogs/RequestLogDetailsPage.jsx";

export const requestRoutes = (
    <Route path="requests">
        <Route index element={<RequestsPage />} />
        <Route path="create" element={<RequestCreatePage />} />
        <Route path=":id" element={<RequestDetailsPage />} />
        <Route path=":id/edit" element={<RequestEditPage />} />


        <Route path=":requestId/logs" element={<RequestLogsPage />} />
        <Route path=":requestId/logs/:id" element={<RequestLogDetailsPage />} />
    </Route>
);
