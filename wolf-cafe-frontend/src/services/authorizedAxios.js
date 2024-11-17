import axios from 'axios'
import { getToken } from './AuthService'

axios.interceptors.request.use(function (config) {
  config.headers['Authorization'] = getToken()
  return config
}, function (error) {
  return Promise.reject(error)
});

export default axios