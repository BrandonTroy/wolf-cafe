import axios from './authorizedAxios'

const BASE_REST_API_URL = 'http://localhost:8080/api/users'

export const saveUser = (user) => axios.post(BASE_REST_API_URL, user)

export const getUser = (id) => axios.get(BASE_REST_API_URL + '/' + id)

export const getUsersList = () => axios.get(BASE_REST_API_URL)

export const updateUser = (id, user) => axios.put(BASE_REST_API_URL + '/' + id, user)

export const deleteUserById = (id) => axios.delete(BASE_REST_API_URL + '/' + id)