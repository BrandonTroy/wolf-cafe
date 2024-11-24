import React, { useState } from 'react'
import { logout, loginAPICall, saveLoggedInUser, storeToken } from '../services/AuthService'
import NotificationPopup from './NotificationPopup'
import { useNavigate } from 'react-router-dom'

const LoginComponent = () => {

  const [usernameOrEmail, setUsernameOrEmail] = useState('')
  const [password, setPassword] = useState('')
  const [message, setMessage] = useState({ type: "none", content: "" })

  const navigator = useNavigate()

  async function handleLoginForm(e) {
    e.preventDefault()
    if (usernameOrEmail === "guest-user") return

    logout(); // Backend won't authorize request if we already have a token
    await loginAPICall(usernameOrEmail, password).then((response) => {
      console.log(response.data)

      // const token = 'Basic ' + window.btoa(usernameOrEmail + ':' + password);
      const token = 'Bearer ' + response.data.accessToken

      const role = response.data.role

      storeToken(token)
      saveLoggedInUser(usernameOrEmail, role)

      navigator('/')

      window.location.reload(false)
    }).catch(error => {
      console.error('ERROR1' + error)
      setMessage({ type: "error", content: "Your username and password do not match any user in our records. Make sure the spelling is correct." })
    })
  }

  async function coninueAsGuest() {
    console.log("guest login")

    await loginAPICall("guest-user", "guest").then((response) => {
      console.log(response.data)

      // const token = 'Basic ' + window.btoa(usernameOrEmail + ':' + password);
      const token = 'Bearer ' + response.data.accessToken

      const role = response.data.role

      storeToken(token)
      saveLoggedInUser(usernameOrEmail, role)

      navigator('/items')

      window.location.reload(false)
    }).catch(error => {
      console.error('ERROR1' + error)
    })
  }

  return (
    <div className='container'>
      <br />
      {message.type != "none" && <NotificationPopup type={message.type} content={message.content} setParentMessage={setMessage} />}
      <br />
      <div className='row'>
        <div className='col-md-6 offset-md-3 offset-md-3'>
          <div className='card'>
            <div className='card-header'>
              <h2 className='text-center'>Login Form</h2>
            </div>
            <div className='card-body'>
              <form>
                <div className='row mb-3'>
                  <label className='col-md-3 control-label'>Username</label>
                  <div className='col-md-9'>
                    <input
                      type='text'
                      name='usernameOrEmail'
                      className='form-control'
                      placeholder='Enter username or email'
                      value={usernameOrEmail}
                      onChange={(e) => setUsernameOrEmail(e.target.value)}
                    >
                    </input>
                  </div>
                </div>

                <div className='row mb-3'>
                  <label className='col-md-3 control-label'>Password</label>
                  <div className='col-md-9'>
                    <input
                      type='password'
                      name='password'
                      className='form-control'
                      placeholder='Enter password'
                      value={password}
                      onChange={(e) => setPassword(e.target.value)}
                    >
                    </input>
                  </div>
                </div>

                <div className='form-group mb-3'>
                  <button className='btn btn-primary' onClick={(e) => setMessage({ type: 'none', content: '' }) || handleLoginForm(e)}>Submit</button>
                </div>
              </form>
              <button className='btn btn-secondary' onClick={() => coninueAsGuest()}>Continue as Guest</button>
            </div>
          </div>
        </div>
      </div>
    </div>
  )
}

export default LoginComponent