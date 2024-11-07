import React from 'react'
import { useEffect, useState } from 'react'
import { getUser, saveUser, updateUser } from '../services/UserService'
import { useNavigate, useParams } from 'react-router-dom'

const UserComponent = () => {

  // Role has to be a Role object with Long id and String name
  // Each User has a private collection of roles, i.e. Collection<Role> roles
  const [role, setRole] = useState( {name: ''} )
  const [roles, setRoles] = useState( [{name: ''}] )
  const [name, setName] = useState('')
  const [username, setUsername] = useState('')
  const [email, setEmail] = useState('')
  const [password, setPassword] = useState('')
  //const [newId, setNewId] = useState(0)
  const { id } = useParams()

  const navigate = useNavigate()
  
  let rolesList = [
	  { id: 1, name: "Barista" },
	  { id: 2, name: "Manager" },
      { id: 3, name: "Customer" },
	  { id: 4, name: "Guest"}
  ];

  useEffect(() => {
    if(id) {
      getUser(id).then((response) => {
        console.log(response.data)
        setName(response.data.name)
		setUsername(response.data.username)
		setEmail(response.data.email)
        setPassword(response.data.password)
		setRoles(response.data.roles)
      }).catch(error => {
        console.error(error)
      })
    }
  }, [id])
  
  function updateRole() {
	// This was pretty complicated so here are some sources regarding HTML select (drop down menus):
	// Source: https://developer.mozilla.org/en-US/docs/Web/HTML/Element/select
	// Source: https://developer.mozilla.org/en-US/docs/Web/API/HTMLSelectElement/selectedIndex
	const selectElement = document.getElementById('selectRole');
	const selectRole = {name: selectElement.options[selectElement.selectedIndex].textContent}
	setRole(selectRole);
	//console.log(role);
  }

  function saveOrUpdateUser(e) {
    e.preventDefault()
	const selectRoles = [ role.name.toUpperCase() ]
	/*if (!id) {*/
	/*const currentNewId = newId;*/
    const user = {name, username, email, password, roles: selectRoles }
    console.log(user)

    if (id) {
      updateUser(id, user).then((response) => {
        console.log(response.data)
        navigate('/users')
      }).catch(error => {
        console.error(error)
      })
    } else {
      saveUser(user).then((response) => {
        console.log(response.data)
        navigate('/users')
      }).catch(error => {
        console.error(error)
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
  
  {/*
  function setPriceWithErrorChecks(new_price) {
	if (new_price.includes('.')) {
		new_price = new_price.substring(0, new_price.indexOf('.') + 3);
	}
	if (new_price.startsWith('-')) {
		new_price = new_price.substring(1);
	}
	setPrice(parseFloat(new_price));
  }
  */}

  return (
    <div className='container'>
      <br />
      <br />
      <div className='row'>
        <div className='card col-md-6 offset-md-3 offset-md-3'>
          { pageTitle() }
          
          <div className='card-body'>
            <form>
			  <div className='form-group mb-2'>
			  <label className='form-label'>Role:</label>
			    <select id="selectRole" name="roles" onChange={(e) => updateRole()}>
				  <option value="0">(Select)</option>
			      <option value="1">Barista</option>
			      <option value="2">Manager</option>
			      <option value="3">Customer</option>
			      <option value="4">Guest</option>
				</select>
			  </div>
			  
              <div className='form-group mb-2'>
                <label className='form-label'>Name:</label>
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
			  
			  <div className='form-group mb-2'>
			    <label className='form-label'>Username:</label>
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
			  
			  <div className='form-group mb-2'>
			    <label className='form-label'>Email:</label>
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

              <div className='form-group mb-2'>
                <label className='form-label'>Password:</label>
                <input 
                  type='password'
				  /*required*/
				  /*required={true}*/
				  /*minLength={3}*/
                  className='form-control'
                  placeholder='Enter Password'
                  name='password'
                  value={password}
                  onChange={(e) => setPassword(e.target.value)}
                >
                </input>
              </div>
			  
			  {/*
              <div className='form-group mb-2'>
                <label className='form-label'>User Price:</label>
                <input 
                  type='number'
				  step='0.01'
                  className='form-control'
                  placeholder='Enter User Price'
                  name='price'
                  value={price}
                  onChange={(e) => setPriceWithErrorChecks(e.target.value)}
                >
                </input>
              </div>
			  */}

              <button type='submit' className='btn btn-success' onClick={(e) => saveOrUpdateUser(e)}>Submit</button>
            </form>
          </div>
        </div>
      </div>
    </div>
  )
}

export default UserComponent