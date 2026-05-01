import './App.css'

function App() {
  return (
      <BrowserRouter>
        <Routes>
          <Route path='/' element={<Layout/>}>
            <Route index element={<HomePage/>}/>
            <Route path='products' element={<ProductsPage/>}/>
            <Route path='products/:id' element={<ProductDetailsPage/>}/>
          </Route>
        </Routes>
      </BrowserRouter>

  )
}

export default App
