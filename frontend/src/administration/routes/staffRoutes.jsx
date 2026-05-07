import {Route} from "react-router";
import CategoriesPage from "../ui/pages/category/CategoriesPage.jsx";
import CategoryCreatePage from "../ui/pages/category/CategoryCreatePage.jsx";
import CategoryDetailsPage from "../ui/pages/category/CategoryDetailsPage.jsx";
import CategoryEditPage from "../ui/pages/category/CategoryEditPage.jsx";
import React from "react";
import StaffPage from "../ui/pages/staff/StaffPage.jsx";
import StaffCreatePage from "../ui/pages/staff/StaffCreatePage.jsx";
import StaffDetailsPage from "../ui/pages/staff/StaffDetailsPage.jsx";
import StaffEditPage from "../ui/pages/staff/StaffEditPage.jsx";

export const staffRoutes = (
    <Route path="staff">
        <Route index element={<StaffPage />} />
        <Route path="create" element={<StaffCreatePage />} />
        <Route path=":id" element={<StaffDetailsPage />} />
        <Route path=":id/edit" element={<StaffEditPage />} />
    </Route>
);