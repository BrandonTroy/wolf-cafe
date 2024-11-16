import axios from "axios"

/** Base URL for the Order API - Correspond to methods in Backend's OrderController. */
const REST_API_BASE_URL = "http://localhost:8080/api/order"

/** POST Order - orders an item */
export const placeOrder = (orderDto) => axios.post(REST_API_BASE_URL, orderDto)
//{
//  headers: {
//    'Content-Type': 'application/json'
//  }
//}
