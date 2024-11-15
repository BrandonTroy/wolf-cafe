import './App.css'
import { BrowserRouter, Routes, Route, Navigate, useNavigate } from 'react-router-dom'
import { isUserAuthenticated, isAdminUser, isManagerUser, isBaristaUser, isCustomerUser, isGuestUser } from './services/AuthService'
import LoginComponent from './components/LoginComponent'
import RegisterComponent from './components/RegisterComponent'
import HeaderComponent from './components/HeaderComponent'
import FooterComponent from './components/FooterComponent'
import ListItemsComponent from './components/ListItemsComponent'
import ListUsersComponent from './components/ListUsersComponent'
import ItemComponent from './components/ItemComponent'
import UserComponent from './components/UserComponent'
import InventoryComponent from './components/InventoryComponent'
import OrderComponent from './components/OrderComponent'
import { OrderProvider } from './OrderContext'

function App() {

  function AuthenticatedRoute({ roleCheck = true, children }) {
    const navigate = useNavigate()
    
    if (!isUserAuthenticated()) return <LoginComponent />
    if (!roleCheck) return (
      <div className='my-5'>
        <p>Unauthorized. If you have an account with access to this page log in to that account.</p>
        <button className='btn btn-primary mx-2' onClick={() => navigate('/')}>Return Home</button>
        <button className='btn btn-secondary mx-2' onClick={() => navigate('/login')}>Return to Login</button>
      </div>
    )
    
    return children
  }

  function HomeRoute() {
    if (isAdminUser()) {
      return <Navigate to='/users' />
    } else {
      return <Navigate to='/items' />
    }
  }

  return (
    <OrderProvider>
      <BrowserRouter>
	    <HeaderComponent />
	    <Routes>
        <Route path='/' element={<AuthenticatedRoute><HomeRoute /></AuthenticatedRoute>} />
        <Route path='/register' element={<RegisterComponent />} />
        <Route path='/login' element={<LoginComponent />} />
        <Route path='/items' element={<AuthenticatedRoute roleCheck={!isAdminUser()}><ListItemsComponent /></AuthenticatedRoute>} />
        <Route path='/add-item' element={<AuthenticatedRoute roleCheck={isManagerUser()}><ItemComponent /></AuthenticatedRoute>} />
        <Route path='/update-item/:id' element={<AuthenticatedRoute roleCheck={isManagerUser()}><ItemComponent /></AuthenticatedRoute>} />
        <Route path='/inventory' element={<AuthenticatedRoute roleCheck={isManagerUser() || isBaristaUser()}><InventoryComponent /></AuthenticatedRoute>} />
        <Route path='/order' element={<AuthenticatedRoute roleCheck={isCustomerUser() || isGuestUser()}><OrderComponent /></AuthenticatedRoute>} />
		<Route path='/users' element={<AuthenticatedRoute roleCheck={isAdminUser()}><ListUsersComponent /></AuthenticatedRoute>} />
		<Route path='/add-user' element={<AuthenticatedRoute roleCheck={isAdminUser()}><UserComponent /></AuthenticatedRoute>} />
		<Route path='/update-user/:id' element={<AuthenticatedRoute roleCheck={isAdminUser()}><UserComponent /></AuthenticatedRoute>} />
        <Route path='/tax' element={<AuthenticatedRoute roleCheck={isAdminUser()}><div>TODO</div></AuthenticatedRoute>} />
      </Routes>
      <FooterComponent />
      </BrowserRouter>
    </OrderProvider>
  )
}

export default App
