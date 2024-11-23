import axios from "./authorizedAxios"

/** Base URL for the Order API - Correspond to methods in Backend's OrderController. */
const REST_API_BASE_URL = "http://localhost:8080/api/orders"

/** POST Order - orders an item */
export const placeOrder = (orderDto) => axios.post(REST_API_BASE_URL, orderDto, {
  headers: {
    'Content-Type': 'application/json'
  }
})

/** GET Orders - gets the order history */
export const getAllOrders = () => axios.get(REST_API_BASE_URL)

/** PUT Order - updates an order's status to fulfilled or picked up */
export const updateOrder = (id, status/*As an OrderDto with 1 field*/) => axios.put(REST_API_BASE_URL + "/" + id, status, {
  headers: {
    'Content-Type': 'application/json'
  }
})
