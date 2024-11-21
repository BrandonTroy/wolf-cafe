import React, { useEffect, useState } from 'react'
import { isBaristaUser, isManagerUser, isCustomerUser, isGuestUser, getLoggedInUser } from '../services/AuthService'
import { getItemById } from '../services/ItemService'
import { getUser } from '../services/UserService'
import { getAllOrders, updateOrder } from '../services/OrderService'
import NotificationPopup from './NotificationPopup'

const OrderHistoryComponent = () => {
  const [orders, setOrders] = useState([])
  const [message, setMessage] = useState({type: "none", content:""})
	
	useEffect(() => {
	  listOrders()
	}, [])

	function listOrders() {
	  if ( isBaristaUser || isManagerUser ) {
	    getAllOrders().then((response) => {
		  tempOrders = response.data
		  for (let i = 0; i < tempOrders.length; i++) {
		    getUser(tempOrders[i].customerId).then((response) => {
			  //Replace customerId with username of the user with that id
			  tempOrders[i].customerId = response.data.username
		    }).catch(error => {
			  console.error(error)
		    })
		  }
	      setOrders(tempOrders)
	    }).catch(error => {
	      console.error(error)
	    })
	  }
	  if ( isCustomerUser || isGuestUser ) {
		getAllOrders().then((response) => {
		  tempOrders = response.data
		  let userOrders = []
		  let j = 0
		  for (let i = 0; i < tempOrders.length; i++) {
		    getUser(tempOrders[i].customerId).then((response) => {
			  //Replace customerId with username of the user with that id
			  tempOrders[i].customerId = response.data.username
			  if ( tempOrders[i].customerId == getLoggedInUser.username ) {
				userOrders[j] = tempOrders[i]
				j++;
			  }
		    }).catch(error => {
			  console.error(error)
		    })
		  }
		  setOrders(userOrders)
		}).catch(error => {
		  console.error(error)
		})
	  }
	}
	
	function fulfillOrder(id) {
	  const status = {status: "FULFILLED"}
	  updateOrder(id, status).then(() => {
		listOrders()
		setMessage({type: "success", content: "Order fulfilled (#" + response.data.id + ")."})
	  }).catch(error => {
		if (error.status === 409) {
		  setMessage({type: "error", content: "The order you selected has already been fulfilled."})
		} else if (error.status === 404) {
		  setMessage({type: "error", content: "The order you are trying to fulfill could not be found in the system."})
		} else {
		  setMessage({type: "error", content: "Could not fulfill order. Check your network connection."})
		}
		console.error(error)
	  })
	}
	
	function pickupOrder(id) {
	  const status = {status: "PICKEDUP"}
	  updateOrder(id, status).then(() => {
		listOrders()
		setMessage({type: "success", content: "Your order has been picked up (#" + response.data.id + "). Thank you!"})
	  }).catch(error => {
		if (error.status === 409) {
		  setMessage({type: "error", content: "The order you selected has already been picked up."})
		} else if (error.status === 404) {
		  setMessage({type: "error", content: "The order you are trying to pick up could not be found in the system."})
		} else {
		  setMessage({type: "error", content: "Could not pick up order. Check your network connection."})
		}
		console.error(error)
	  })
	}
	
	function cancelOrder(id) {
	  const status = {status: "CANCELED"}
	  updateOrder(id, status).then(() => {
		listOrders()
		setMessage({type: "success", content: "Your order has been canceled (#" + response.data.id + ")."})
	  }).catch(error => {
		if (error.status === 409) {
		  setMessage({type: "error", content: "The order you selected has already been canceled."})
		} else if (error.status === 404) {
		  setMessage({type: "error", content: "The order you are trying to cancel could not be found in the system."})
		} else {
		  setMessage({type: "error", content: "Could not cancel order. Check your network connection."})
		}
		console.error(error)
	  })
	}
	
	function remakeOrder(id) {
	  const status = {status: "PLACED"}
	  updateOrder(id, status).then(() => {
		listOrders()
		setMessage({type: "success", content: "Your order has been placed (#" + response.data.id + "). Thank you!"})
	  }).catch(error => {
		if (error.status === 409) {
		  setMessage({type: "error", content: "The order you selected has already been placed."})
		} else if (error.status === 404) {
		  setMessage({type: "error", content: "The order you are trying to place could not be found in the system."})
		} else {
		  setMessage({type: "error", content: "Could not place order. Check your network connection."})
		}
		console.error(error)
	  })
	}
	
	function displayItems(itemList) {
	  let itemString = ""
      for (const [key, value] in Object.entries(itemList)) {
		getItemById(parseInt(key)).then((response) => {
          if (itemString.length === 0) {
			itemString.concat(response.data.name, " x", toString(value))
	      } else {
			itemString.concat(", ", response.data.name, " x", toString(value))
		  }
		}).catch(error => {
          console.error(error)
		})
	  }
	  return itemString
	}
	
	return (
		<div className='container'>
      <br />
	  {message.type != "none" && <NotificationPopup type={message.type} content={message.content} setParentMessage={setMessage} />}
	  <br />
      <div className='d-flex justify-content-between align-items-center'>
        <h2 className='text-center mx-auto mb-3'>Past Orders</h2>
      </div>
			<div>
				<table className='table table-bordered table-striped'>
					<thead>
						<tr>
							<th>Date</th>
							{ (isManagerUser() || isBaristaUser()) &&
							    <th>User</th>
							}
							<th>Items</th>
							<th>Price</th>
							<th>Tip</th>
							<th>Status</th>
                            <th>Actions</th>
						</tr>
					</thead>
					<tbody>
						{
							orders.map((order) =>
								<tr key={order.id}>
									<td>{order.date}</td>
									{ (isManagerUser() || isBaristaUser()) &&
									    <td>{order.customerId}</td>
									}
									<td>{displayItems(order.itemList)}</td>
									<td>{order.price}</td>
									<td>{order.tip}</td>
									{
										order.status != "PICKEDUP" ?
									    <td>{order.status[0] + order.status.substring(1).toLowerCase()}</td> :
										<td>Picked Up</td>
									}
                                    <td>
									    {/* Manager and Barista Actions */}
										{
											(isManagerUser() || isBaristaUser()) && order.status === "PLACED" ?
											<button className='btn btn-info' onClick={() => setMessage({ type: 'none', content: '' })
											                                            || fulfillOrder(order.id)}>Fulfill</button> :
											<button className='btn btn-info' disabled>Fulfilled</button>
										}
										{/* Customer and Guest Actions */}
										{
											(isCustomerUser() || isGuestUser()) && order.status === "FULFILLED" &&
											<button className='btn btn-info' onClick={() => setMessage({ type: 'none', content: '' })
											                                            || pickupOrder(order.id)}>Pick Up</button>
										}
										{
											(isCustomerUser() || isGuestUser()) && order.status === "PLACED" &&
											<button className='btn btn-info' onClick={() => setMessage({ type: 'none', content: '' })
											                                            || cancelOrder(order.id)}>Cancel</button>
										}
										{
											(isCustomerUser() || isGuestUser()) && order.status === "PICKEDUP" &&
											<button className='btn btn-info' onClick={() => setMessage({ type: 'none', content: '' })
											                                            || remakeOrder(order.id)}>Place Again</button>
										}
									</td>
								</tr>
							)
						}
					</tbody>
				</table>
			</div>
		</div>
	)
}

export default OrderHistoryComponent