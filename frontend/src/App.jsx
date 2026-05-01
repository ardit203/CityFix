import React from 'react';
import {BrowserRouter, Routes, Route} from "react-router";
import Layout from "./common/ui/components/layout/Layout/Layout.jsx";
import HomePage from "./common/ui/pages/HomePage/HomePage.jsx";
import UsersPage from "./auth_and_access/ui/pages/UserPage/UsersPage.jsx";



function App() {
  return (
      <BrowserRouter>
        <Routes>
          <Route path='/' element={<Layout/>}>
            <Route index element={<HomePage/>}/>
            <Route path='users' element={<UsersPage/>}/>
            {/*<Route path='products/:id' element={<ProductDetailsPage/>}/>*/}
          </Route>
        </Routes>
      </BrowserRouter>

  )
}

export default App
