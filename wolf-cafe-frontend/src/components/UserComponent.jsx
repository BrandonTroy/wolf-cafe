//import '../UserComponent.css'
import React from 'react'
import { useEffect, useState } from 'react'
import { getUser, saveUser, updateUser } from '../services/UserService'
import { useNavigate, useParams } from 'react-router-dom'
import NotificationPopup from './NotificationPopup'

const UserComponent = () => {

  const [role, setRole] = useState( '' )
  //before backend change: const [roles, setRoles] = useState( [{name: ''}] )
  const [name, setName] = useState('')
  const [username, setUsername] = useState('')
  const [email, setEmail] = useState('')
  const [password, setPassword] = useState('')
  const { id } = useParams()
  const [message, setMessage] = useState({type: "none", content:""})

  const navigate = useNavigate()

  useEffect(() => {
    if(id) {
	  console.log("Current User " + id + " Values:")
      getUser(id).then((response) => {
        setName(response.data.name)
	    setRole(response.data.role)
	    setUsername(response.data.username)
	    setEmail(response.data.email)
        setPassword(response.data.password)
	    console.log(response.data)
      }).catch(error => {
      console.error("ERROR: " + error)
      })
    }
  }, [id])
  
  function updateRole() {
	// This was pretty complicated so here are some sources regarding HTML select (drop down menus):
	// Source: https://developer.mozilla.org/en-US/docs/Web/HTML/Element/select
	// Source: https://developer.mozilla.org/en-US/docs/Web/API/HTMLSelectElement/selectedIndex
	const selectElement = document.getElementById('selectRole');
	const selectRole = selectElement.options[selectElement.selectedIndex].textContent
	//console.log("role: " + selectRole)
	setRole(selectRole);
  }
  
  function resetMessage() {
	setMessage({type:"none", content:""})
  }
  
  function returnToUserList(e) {
	e.preventDefault()
	console.log("Returning to Users List")
	navigate('/users', { state: message })
  }

  function saveOrUpdateUser(e) {
    e.preventDefault()
	// Backend was changed from multiple roles to a single role
	//const selectRoles = [ role.name.toUpperCase() ]
	
	// Update role in case it was not previously updated (dropdown option not changed)
	updateRole()
	
	// Some regex syntax copied/pasted from teammate that used Google search AI overview to help with it
	if (name == "") {
	  setMessage({type:"error", content:"Name required. Please enter a name."})
	  return
	}
	if (name.search(/[^a-zA-Z\s\-\'\.]/) > -1) {
	  setMessage({type:"error", content:"Name can only contain letters, spaces, hyphens, apostrophes and periods."})
	  return
	}
	if (username == "") {
	  setMessage({type:"error", content:"Username required. Please enter a username."})
	  return
	}
	if (username.search(/[^a-zA-Z0-9\.]/) > -1) {
	  setMessage({type:"error", content:"Username can only contain letters, numbers, and periods."})
	  return
	}
	if (email == "") {
	  setMessage({type:"error", content:"Email required. Please enter an email."})
	  return
	}
	if (email.indexOf('@') != email.lastIndexOf('@') || !email.includes('@')) {
	  setMessage({type:"error", content:"Email must contain one @ symbol."})
	  return
	}
	// For some reason this check for email does not appear to work
	// It is alright though because the backend still catches this before the process is over
    //if (email == "" || email.search(/^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+.[a-zA-Z]{2,}$]/) > -1) {
	//  setMessage({type:"error", content:"Invalid email format."})
	//  return
	//}
	if (password.length < 8) {
	  setMessage({type:"error", content:"Password must be at least 8 characters long."})
	  return
	}
	
    const user = {name, username, email, password, role: role.toUpperCase() }
    //console.log(user)

    if (id) {
      updateUser(id, user).then((response) => {
		setMessage({type:"success", content:"User updated successfully. Click Cancel to return to users page."})
		console.log("Updated User " + id + " Values:")
		console.log(response.data)
		return
		//navigate('/users', { state: { key: message }})
      }).catch(error => {
		if (error.status === 409) {
			setMessage({type:"error", content:"Username or email is already taken. Please choose another."})
		} else if (error.status === 400) {
			setMessage({type:"error", content:"Email contains a disallowed character or invalid format. Please try another email."})
		} else if (error.status === 401) {
			setMessage({type:"error", content:"Improper syntax. Confirm all fields are entered correctly."})
		} else {
			setMessage({type:"error", content:"Update user failed. Check your network connection."})
		}
        console.error("ERROR: " + error)
      })
    } else {
      saveUser(user).then((response) => {
		setMessage({type:"success", content:"User added successfully. Click Cancel to return to users page."})
		console.log("New User Values:")
		console.log(response.data)
        return
      }).catch(error => {
		if (error.status === 409) {
			setMessage({type:"error", content:"Username or email is already taken. Please choose another."})
		} else if (error.status === 400) {
			setMessage({type:"error", content:"Email contains a disallowed character or invalid format. Please try another email."})
		} else if (error.status === 401) {
			setMessage({type:"error", content:"Improper syntax. Confirm all fields are entered correctly."})
		} else {
			setMessage({type:"error", content:"Add user failed. Check your network connection."})
		}
        console.error("ERROR: " + error)
      })
    }
  }

  function pageTitle() {
    if (id) {
      return <h2 className='text-center'>Update User</h2>
    } else {
      return <h2 className='text-center'>Add User</h2>
    }
  }
  
  // This section is to make sure errors do not mess up the dropdown role menu
  // Without this section, an error upon editing a Customer role will allow access to Barista/Manager options
  if (id && message.type != "none") {
    getUser(id).then((response) => {
	  if ( role != response.data.role && response.data.role == "CUSTOMER" )
        setRole(response.data.role)
    }).catch(error => {
      console.error("ERROR: " + error)
    })
  }

  return (
    <div className='container'>
	  <br />
	  {message.type != "none" && <NotificationPopup type={message.type} content={message.content} setParentMessage={setMessage} />}
	  <br />
      <div className='row'>
        <div className='card col-md-6 offset-md-3 offset-md-3'>
          { pageTitle() }
          
          <div className='card-body'>
            <form>
			<div className='row mb-3'>
			  <label className='col-md-3 form-label'>Role:</label>
			<div className='col-md-2'>
				{/* Customer Edit Case */}
			    {
				  role == "CUSTOMER" && <select id="selectRole" name="roles" value="3" onChange={(e) => updateRole()}>
				  <option value="3" disabled hidden>Customer</option>
				  </select>
				}
				{/* Add Use Case */}
				{
				  !id && <select id="selectRole" name="roles" defaultValue="0" onChange={(e) => updateRole()}>
				  <option value="0" disabled hidden>(Select)</option>
				  <option value="1">Barista</option>
				  <option value="2">Manager</option>
				  </select>
				}
				{/* Non-Customer Edit Case */}
				{
				  role == "BARISTA" && <select id="selectRole" name="roles" value="1" onChange={(e) => updateRole()}>
				  <option value="1">Barista</option>
				  <option value="2">Manager</option>
				  </select>
				  || role == "MANAGER" && <select id="selectRole" name="roles" value="2" onChange={(e) => updateRole()}>
				  <option value="1">Barista</option>
				  <option value="2">Manager</option>
				  </select>
				  || role != "CUSTOMER" && id && <select id="selectRole" name="roles" onChange={(e) => updateRole()}>
				  <option value="1">Barista</option>
				  <option value="2">Manager</option>
				  </select>
				}
		    </div>
			</div>
			  
              <div className='row mb-3'>
                <label className='col-md-3 form-label'>Name:</label>
				<div className='col-md-9'>
                <input 
                  type='text'
                  className='form-control'
                  placeholder='Enter Name'
                  name='name'
                  value={name}
                  onChange={(e) => setName(e.target.value)}
                >
                </input>
				</div>
              </div>
			  
			  <div className='row mb-3'>
			    <label className='col-md-3 form-label'>Username:</label>
			  <div className='col-md-9'>
			    <input 
			      type='text'
			      className='form-control'
			      placeholder='Enter Username'
			      name='username'
			      value={username}
			      onChange={(e) => setUsername(e.target.value.substring(0, Math.min(300, e.target.value.length)))}
			    >
			    </input>
				</div>
			  </div>
			  
			  <div className='row mb-3'>
			    <label className='col-md-3 form-label'>Email:</label>
			  <div className='col-md-9'>
			    <input 
			      type='text'
			      className='form-control'
			      placeholder='Enter Email'
			      name='email'
			      value={email}
			      onChange={(e) => setEmail(e.target.value.substring(0, Math.min(300, e.target.value.length)))}
			    >
			    </input>
				</div>
			  </div>

			  <div className='row mb-3'>
			    <label className='col-md-3 form-label'>Password:</label>
			  <div className='col-md-9'>
                {!id && <input 
                  /*originally was type='password', but admin is only user so it's fine to display as text*/
				  type='text'
				  /*required*/
				  /*required={true}*/
				  /*minLength={3}*/
                  className='form-control'
                  placeholder='Enter Password'
                  name='password'
                  value={password}
                  onChange={(e) => setPassword(e.target.value)}
                >
                </input>}
				</div>
              </div>
			  
			  <button type='reset' className='btn btn-secondary' onClick={(e) => returnToUserList(e)}>Cancel</button>
              <button type='submit' className='btn btn-success' onClick={(e) => resetMessage() || saveOrUpdateUser(e)}style={{marginLeft: "10px"}}>Submit</button>
            </form>
          </div>
        </div>
      </div>
    </div>
  )
}

export default UserComponent