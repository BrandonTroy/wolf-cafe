import axios from './authorizedAxios';

const BASE_REST_API_URL = 'http://localhost:8080/api/tax'

export const getTax = () => axios.get(BASE_REST_API_URL)

export const updateTax = (tax) => axios.put(BASE_REST_API_URL, tax, {
  headers: {
    'Content-Type': 'application/json'
  }
})