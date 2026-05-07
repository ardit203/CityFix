import {Route} from "react-router";
import UsersPage from "../ui/pages/UsersPage.jsx";
import UserDetailsPage from "../ui/pages/UserDetailsPage.jsx";
import UserEditPage from "../ui/pages/UserEditPage.jsx";


export const userRoutes = (
    <Route path="users">
        <Route index element={<UsersPage />} />
        <Route path=":id" element={<UserDetailsPage />} />
        <Route path=":id/edit" element={<UserEditPage />} />
    </Route>
);