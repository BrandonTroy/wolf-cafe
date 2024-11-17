import axios from "./authorizedAxios"

/** Base URL for the Order API - Correspond to methods in Backend's OrderController. */
const REST_API_BASE_URL = "http://localhost:8080/api/order"

/** POST Order - orders an item */
export const order = (id, amtPaid) => axios.post(REST_API_BASE_URL + '/' + id, amtPaid, {
  headers: {
    'Content-Type': 'application/json'
  }
})
