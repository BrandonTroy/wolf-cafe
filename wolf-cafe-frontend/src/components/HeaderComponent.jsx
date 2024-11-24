import React from 'react'
import { NavLink } from 'react-router-dom'
import { useNavigate } from 'react-router-dom'
import { isUserAuthenticated, isAdminUser, isBaristaUser, isManagerUser, isCustomerUser, isGuestUser, logout } from '../services/AuthService'

const HeaderComponent = () => {

  const isAuth = isUserAuthenticated()
  const isLoggedIn = isAuth && !isGuestUser()

  function handleLogout() {
    logout()
    navigator('/login')
  }

  const navigator = useNavigate()

  return (
    <div>
      <header>
        <nav className='navbar navbar-expand-md navbar-dark bg-dark px-3'>
          <div>
            <a href='http://localhost:3000' className='navbar-brand'>
              WolfCafe
            </a>
          </div>
          <div className='collapse navbar-collapse'>
            {
              isAuth && <ul className='navbar-nav'>
                {!isAdminUser() &&
                  <li className='nav-item'>
                    <NavLink to='/items' className='nav-link'>Items</NavLink>
                  </li>
                }
                {(isManagerUser() || isBaristaUser()) &&
                  <li className='nav-item'>
                    <NavLink to='/inventory' className='nav-link'>Inventory</NavLink>
                  </li>
                }
                {(isCustomerUser() || isGuestUser()) &&
                  <li className='nav-item'>
                    <NavLink to='/order' className='nav-link'>Order</NavLink>
                  </li>
                }
                {(!isAdminUser()) &&
                  <li className='nav-item'>
                    <NavLink to='/orders' className='nav-link'>Order History</NavLink>
                  </li>
                }
                {
                  isAdminUser() &&
                  <li className='nav-item'>
                    <NavLink to='/users' className='nav-link'>Users</NavLink>
                  </li>
                }
                {
                  isAdminUser() &&
                  <li className='nav-item'>
                    <NavLink to='/tax' className='nav-link'>Tax</NavLink>
                  </li>
                }
              </ul>
            }
          </div>
          <ul className='navbar-nav'>
            {
              !isLoggedIn &&
              <li className='nav-item'>
                <NavLink to='/register' className='nav-link'>Register</NavLink>
              </li>
            }
            {
              !isLoggedIn &&
              <li className='nav-item'>
                <NavLink to='/login' className='nav-link'>Login</NavLink>
              </li>
            }
            {
              isLoggedIn &&
              <li className='nav-item'>
                <NavLink to='/login' className='nav-link' onClick={handleLogout}>Logout</NavLink>
              </li>
            }
          </ul>
        </nav>
      </header>
    </div>
  )
}

export default HeaderComponent