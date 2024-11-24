import React, { useState } from 'react'
import { registerAPICall } from '../services/AuthService'
import NotificationPopup from './NotificationPopup'

const RegisterComponent = () => {

  const [name, setName] = useState('')
  const [username, setUsername] = useState('')
  const [email, setEmail] = useState('')
  const [password, setPassword] = useState('')
  const [message, setMessage] = useState({ type: "none", content: "" })

  function handleRegistrationForm(e) {
    e.preventDefault();

    //Help with regular expression syntax from Google search AI overview
    if (name.search(/[^a-zA-Z\s\-\'\.]/) > -1) {
      setMessage({ type: "error", content: "Name can only contain letters, spaces, hyphens, apostrophes and periods." })
      return
    }
    if (username.search(/[^a-zA-Z0-9\.]/) > -1) {
      setMessage({ type: "error", content: "Username can only contain letters, numbers, and periods." })
      return
    }
    if (password.length < 8) {
      setMessage({ type: "error", content: "Password must be at least 8 characters long." })
      return
    }

    const register = { name, username, email, password }

    console.log(register)

    registerAPICall(register).then((response) => {
      setMessage({ type: "success", content: "Registration successful. Proceed to the login page." })
      console.log(response.data)
    }).catch(error => {
      if (error.status === 409) {
        setMessage({ type: "error", content: "Username is already taken. Please choose another." })
      } else if (error.status === 400) {
        setMessage({ type: "error", content: "Email contains a disallowed character or invalid format. Please try another email." })
      } else {
        setMessage({ type: "error", content: "Registration failed. Check your network connection." })
      }
      console.error(error)
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
              <h2 className='text-center'>User Registration Form</h2>
            </div>
            <div className='card-body'>
              <form>
                <div className='row mb-3'>
                  <label className='col-md-3 control-label'>Name</label>
                  <div className='col-md-9'>
                    <input
                      type='text'
                      name='name'
                      className='form-control'
                      placeholder='Enter name'
                      value={name}
                      onChange={(e) => setName(e.target.value)}
                    >
                    </input>
                  </div>
                </div>

                <div className='row mb-3'>
                  <label className='col-md-3 control-label'>Username</label>
                  <div className='col-md-9'>
                    <input
                      type='text'
                      name='username'
                      className='form-control'
                      placeholder='Enter username'
                      value={username}
                      onChange={(e) => setUsername(e.target.value)}
                    >
                    </input>
                  </div>
                </div>

                <div className='row mb-3'>
                  <label className='col-md-3 control-label'>Email</label>
                  <div className='col-md-9'>
                    <input
                      type='text'
                      name='email'
                      className='form-control'
                      placeholder='Enter email'
                      value={email}
                      onChange={(e) => setEmail(e.target.value)}
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
                  <button className='btn btn-primary' onClick={(e) => setMessage({ type: 'none', content: '' }) || handleRegistrationForm(e)}>Submit</button>
                </div>
              </form>
            </div>
          </div>
        </div>
      </div>
    </div>
  )
}

export default RegisterComponent