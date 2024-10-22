import './App.css'
import { BrowserRouter, Routes, Route, Navigate } from 'react-router-dom'
import { isUserLoggedIn } from './services/AuthService'
import LoginComponent from './components/LoginComponent'
import RegisterComponent from './components/RegisterComponent'
import HeaderComponent from './components/HeaderComponent'
import FooterComponent from './components/FooterComponent'
import ListItemsComponent from './components/ListItemsComponent'
import ItemComponent from './components/ItemComponent'
import InventoryComponent from './components/InventoryComponent'
import OrderComponent from './components/OrderComponent'

function App() {

  function AuthenticatedRoute({ children }) {
    const isAuth = isUserLoggedIn() || true
    if (isAuth) {
      return children
    }
    return <Navigate to='/' />
  }

  return (
    <>
      <BrowserRouter>
	    <HeaderComponent />
	    <Routes>
        <Route path='/' element={<LoginComponent />}></Route>
        <Route path='/register' element={<RegisterComponent />}></Route>
        <Route path='/login' element={<LoginComponent />}></Route>
        <Route path='/items' element={<AuthenticatedRoute><ListItemsComponent /></AuthenticatedRoute>}></Route>
        <Route path='/add-item' element={<AuthenticatedRoute><ItemComponent /></AuthenticatedRoute>}></Route>
        <Route path='/update-item/:id' element={<AuthenticatedRoute><ItemComponent /></AuthenticatedRoute>}></Route>
        <Route path='/inventory' element={<AuthenticatedRoute><InventoryComponent /></AuthenticatedRoute>}></Route>
        <Route path='/order' element={<AuthenticatedRoute><OrderComponent /></AuthenticatedRoute>}></Route>
      </Routes>
      <FooterComponent />
      </BrowserRouter>
    </>
  )
}

export default App
