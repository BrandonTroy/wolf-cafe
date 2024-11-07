import React, { useEffect, useState, useContext } from 'react'
import { useNavigate } from 'react-router-dom'
import { isBaristaUser, isManagerUser, isCustomerUser, isGuestUser } from '../services/AuthService'
import { getUsersList, deleteUserById } from '../services/UserService'


const ListUsersComponent = () => {
  const [users, setUsers] = useState([])
  const navigate = useNavigate()
	
	useEffect(() => {
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
		navigate('/add-user')
	}
	
	function updateUser(id) {
		console.log(id)
		navigate(`/update-user/${id}`)
	}
	
	function deleteUser(id) {
		console.log(id)
		deleteUserById(id).then(() => {
			listUsers()
		}).catch(error => {
			console.error(error)
		})
  }

	
	return (
		<div className='container'>
      <br /> <br />
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
							users.map((user) =>
								<tr key={user.name}>
									<td>{user.role}</td>
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