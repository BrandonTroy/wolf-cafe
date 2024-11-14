import React, { useEffect, useState } from 'react'
//import React, { useEffect, useState, useContext } from 'react'
import { useNavigate } from 'react-router-dom'
import { getUsersList, deleteUserById } from '../services/UserService'
import NotificationPopup from './NotificationPopup'


const ListUsersComponent = () => {
  const [users, setUsers] = useState([])
  const navigate = useNavigate()
  const [message, setMessage] = useState({type: "none", content:""})
	
	useEffect(() => {
      console.log("Users List:")
	  listUsers()
	}, [])

	function listUsers() {
	  getUsersList().then((response) => {
		console.log(response.data)
	    setUsers(response.data)
	  }).catch(error => {
	    console.error(error)
	  })
	}
	
	function addNewUser() {
		console.log("Adding User")
		navigate('/add-user')
	}
	
	function updateUser(id) {
		console.log("Updating User " + id)
		navigate(`/update-user/${id}`)
	}
	
	function deleteUser(id) {
		deleteUserById(id).then(() => {
			setMessage({type:"success", content:"User " + id + " deleted successfully."})
		    console.log("User " + id + " deleted successfully.")
		    console.log("Users List:")
			listUsers()
		}).catch(error => {
			setMessage({type:"error", content:"Delete user failed. Check your network connection."})
			console.error(error)
		})
  }

	
	return (
		<div className='container'>
		<br />
		{message.type != "none" && <NotificationPopup type={message.type} content={message.content} setParentMessage={setMessage} />}
		<br />
      <div className='d-flex justify-content-between align-items-center'>
        <h2 className='text-center mx-auto mb-3'>Users</h2>
        {
          <button className='btn btn-primary mb-2 ml-auto' onClick={addNewUser}>Add User</button>
        }
      </div>
			<div>
				<table className='table table-bordered table-striped'>
					<thead>
						<tr>
							<th>Name</th>
							<th>Role</th>
							<th>Username</th>
							<th>Email</th>
							<th>Actions</th>
						</tr>
					</thead>
					<tbody>
						{
							users.map((user) => user.role != "ADMIN" &&
								<tr key={user.username}>
									<td>{user.name}</td>
									<td>{user.role[0] + user.role.substring(1).toLowerCase()}</td>
									<td>{user.username}</td>
									<td>{user.email}</td>
                  {<td>
										{
											<button className='btn btn-info' onClick={() => updateUser(user.id)}>Update</button>
										}
										{
											<button className='btn btn-danger' onClick={() => deleteUser(user.id)}style={{marginLeft: "10px"}}>Delete</button>
                    }
									</td>}
								</tr>
							)
						}
					</tbody>
				</table>
			</div>
		</div>
	)
}

export default ListUsersComponent