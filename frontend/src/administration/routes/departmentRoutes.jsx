import React from 'react';
import { Route } from "react-router";
import DepartmentsPage from "../ui/pages/department/DepartmentsPage.jsx";
import DepartmentDetailsPage from "../ui/pages/department/DepartmentDetailsPage.jsx";
import DepartmentCreatePage from "../ui/pages/department/DepartmentCreatePage.jsx";
import DepartmentEditPage from "../ui/pages/department/DepartmentEditPage.jsx";
import ProtectedRoute from "../../auth_and_access/ui/components/auth/ProtectedRoute.jsx";

export const departmentRoutes = (
    <Route path="departments" element={<ProtectedRoute allowedRoles={["ADMINISTRATOR"]} />}>
        <Route index element={<DepartmentsPage />} />
        <Route path="create" element={<DepartmentCreatePage />} />
        <Route path=":id" element={<DepartmentDetailsPage />} />
        <Route path=":id/edit" element={<DepartmentEditPage />} />
    </Route>
);
