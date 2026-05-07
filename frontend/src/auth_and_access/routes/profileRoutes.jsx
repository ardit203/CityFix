import {Route} from "react-router";
import ProfilePage from "../ui/pages/ProfilePage.jsx";
import ProfileEditPage from "../ui/pages/ProfileEditPage.jsx";


export const profileRoutes = (
    <Route path="profile">
        <Route index element={<ProfilePage />} />
        <Route path="/profile/edit" element={<ProfileEditPage />} />
    </Route>
);