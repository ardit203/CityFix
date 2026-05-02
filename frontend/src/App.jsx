import React from 'react';
import {BrowserRouter, Routes, Route} from "react-router";
import Layout from "./common/ui/components/layout/Layout/Layout.jsx";
import HomePage from "./common/ui/pages/HomePage/HomePage.jsx";
import UsersPage from "./auth_and_access/ui/pages/UserPage/UsersPage.jsx";
import UserDetailsPage from "./auth_and_access/ui/pages/UserDetailsPage/UserDetailsPage.jsx";



function App() {
  return (
      <BrowserRouter>
        <Routes>
          <Route path='/' element={<Layout/>}>
            <Route index element={<HomePage/>}/>
            <Route path='users' element={<UsersPage/>}/>
            <Route path='users/:id' element={<UserDetailsPage/>}/>
          </Route>
        </Routes>
      </BrowserRouter>

  )
}

export default App
