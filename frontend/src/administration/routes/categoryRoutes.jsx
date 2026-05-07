import React from 'react';
import { Route } from "react-router";
import CategoriesPage from "../ui/pages/category/CategoriesPage.jsx";
import CategoryDetailsPage from "../ui/pages/category/CategoryDetailsPage.jsx";
import CategoryCreatePage from "../ui/pages/category/CategoryCreatePage.jsx";
import CategoryEditPage from "../ui/pages/category/CategoryEditPage.jsx";

export const categoryRoutes = (
    <Route path="categories">
        <Route index element={<CategoriesPage />} />
        <Route path="create" element={<CategoryCreatePage />} />
        <Route path=":id" element={<CategoryDetailsPage />} />
        <Route path=":id/edit" element={<CategoryEditPage />} />
    </Route>
);
