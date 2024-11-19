import React, { useEffect, useState } from 'react';
import { getUser, saveUser, updateUser } from '../services/UserService';
import { useNavigate, useParams } from 'react-router-dom';
import NotificationPopup from './NotificationPopup';

const UserComponent = () => {
  const [role, setRole] = useState('');
  const [name, setName] = useState('');
  const [username, setUsername] = useState('');
  const [email, setEmail] = useState('');
  const [password, setPassword] = useState('');
  const { id } = useParams();
  const [message, setMessage] = useState({ type: 'none', content: '' });

  const navigate = useNavigate();

  useEffect(() => {
    if (id) {
      getUser(id)
        .then((response) => {
          setName(response.data.name);
          setRole(response.data.role);
          setUsername(response.data.username);
          setEmail(response.data.email);
          setPassword(response.data.password);
        })
        .catch((error) => {
          console.error('ERROR: ' + error);
        });
    }
  }, [id]);

  function updateRole(e) {
    const selectRole = e.target.value;
    setRole(selectRole);
  }

  function resetMessage() {
    setMessage({ type: 'none', content: '' });
  }

  function returnToUserList(e) {
    e.preventDefault();
    navigate('/users', { state: message });
  }

  function resetForm() {
    setRole('');
    setName('');
    setUsername('');
    setEmail('');
    setPassword('');
  }

  function saveOrUpdateUser(e) {
    e.preventDefault();
    
    if (name === '') {
      setMessage({ type: 'error', content: 'Name required. Please enter a name.' });
      return;
    }
    if (name.search(/[^a-zA-Z\s\-\'\.]/) > -1) {
      setMessage({ type: 'error', content: 'Name can only contain letters, spaces, hyphens, apostrophes and periods.' });
      return;
    }
    if (username === '') {
      setMessage({ type: 'error', content: 'Username required. Please enter a username.' });
      return;
    }
    if (username.search(/[^a-zA-Z0-9\.]/) > -1) {
      setMessage({ type: 'error', content: 'Username can only contain letters, numbers, and periods.' });
      return;
    }
    if (email === '') {
      setMessage({ type: 'error', content: 'Email required. Please enter an email.' });
      return;
    }
    if (email.indexOf('@') !== email.lastIndexOf('@') || !email.includes('@')) {
      setMessage({ type: 'error', content: 'Email must contain one @ symbol.' });
      return;
    }
    if (password.length < 8) {
      setMessage({ type: 'error', content: 'Password must be at least 8 characters long.' });
      return;
    }

    const user = { name, username, email, password, role: role.toUpperCase() };

    if (id) {
      updateUser(id, user)
        .then((response) => {
          setMessage({ type: 'success', content: 'User updated successfully. Click Cancel to return to users page.' });
        })
        .catch((error) => {
          handleError(error);
        });
    } else {
      saveUser(user)
        .then((response) => {
          setMessage({ type: 'success', content: 'User added successfully. Click Cancel to return to users page.' });
          resetForm();
        })
        .catch((error) => {
          handleError(error);
        });
    }
  }

  function handleError(error) {
    if (error.status === 409) {
      setMessage({ type: 'error', content: 'Username or email is already taken. Please choose another.' });
    } else if (error.status === 400) {
      setMessage({ type: 'error', content: 'Email contains a disallowed character or invalid format. Please try another email.' });
    } else if (error.status === 401) {
      setMessage({ type: 'error', content: 'Improper syntax. Confirm all fields are entered correctly.' });
    } else {
      setMessage({ type: 'error', content: 'Operation failed. Check your network connection.' });
    }
    console.error('ERROR: ' + error);
  }

  function pageTitle() {
    if (id) {
      return <h2 className="text-center">Update User</h2>;
    } else {
      return <h2 className="text-center">Add User</h2>;
    }
  }

  return (
    <div className="container">
      <br />
      {message.type !== 'none' && <NotificationPopup type={message.type} content={message.content} setParentMessage={setMessage} />}
      <br />
      <div className="row">
        <div className="card col-md-6 offset-md-3 offset-md-3">
          {pageTitle()}
          <div className="card-body">
            <form onSubmit={(e) => resetMessage() || saveOrUpdateUser(e)}>
              <div className="row mb-3">
                <label className="col-md-3 form-label">Role:</label>
                <div className="col-md-2">
                  {role === 'CUSTOMER' && (
                    <select id="selectRole" name="roles" value={role} onChange={updateRole} className="form-control" style={{ width: 'auto' }} disabled>
                      <option value="CUSTOMER">Customer</option>
                    </select>
                  )}
                  {(!id || role === 'BARISTA' || role === 'MANAGER') && (
                    <select id="selectRole" name="roles" value={role} onChange={updateRole} className="form-control" style={{ width: 'auto' }} placeholder="Select">
                      <option value="" hidden disabled>(Select)</option>
                      <option value="BARISTA">Barista</option>
                      <option value="MANAGER">Manager</option>
                    </select>
                  )}
                </div>
              </div>

              <div className="row mb-3">
                <label className="col-md-3 form-label">Name:</label>
                <div className="col-md-9">
                  <input
                    type="text"
                    className="form-control"
                    placeholder="Enter Name"
                    name="name"
                    value={name}
                    onChange={(e) => setName(e.target.value)}
                  />
                </div>
              </div>

              <div className="row mb-3">
                <label className="col-md-3 form-label">Username:</label>
                <div className="col-md-9">
                  <input
                    type="text"
                    className="form-control"
                    placeholder="Enter Username"
                    name="username"
                    value={username}
                    onChange={(e) => setUsername(e.target.value.substring(0, Math.min(300, e.target.value.length)))}
                  />
                </div>
              </div>

              <div className="row mb-3">
                <label className="col-md-3 form-label">Email:</label>
                <div className="col-md-9">
                  <input
                    type="text"
                    className="form-control"
                    placeholder="Enter Email"
                    name="email"
                    value={email}
                    onChange={(e) => setEmail(e.target.value.substring(0, Math.min(300, e.target.value.length)))}
                  />
                </div>
              </div>

              <div className="row mb-3">
                {!id && <label className="col-md-3 form-label">Password:</label>}
                <div className="col-md-9">
                  {!id && (
                    <input
                      type="text"
                      className="form-control"
                      placeholder="Enter Password"
                      name="password"
                      value={password}
                      onChange={(e) => setPassword(e.target.value)}
                    />
                  )}
                </div>
              </div>

              <button type="reset" className="btn btn-secondary" onClick={returnToUserList}>
                Cancel
              </button>
              <button type="submit" className="btn btn-success" style={{ marginLeft: '10px' }}>
                Submit
              </button>
            </form>
          </div>
        </div>
      </div>
    </div>
  );
};

export default UserComponent;