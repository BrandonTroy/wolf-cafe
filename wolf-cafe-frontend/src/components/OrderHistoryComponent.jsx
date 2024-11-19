import React, { useEffect, useState } from 'react'
import { isBaristaUser, isManagerUser, isCustomerUser, isGuestUser } from '../services/AuthService'
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
	
	function fulfillOrder(id) {
	  const status = {status: "FULFILLED"}
	  updateOrder(id, status).then(() => {
		listOrders()
		setMessage()
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
							<th>User</th>
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
									<td>{order.customerId}</td>
									<td>{displayItems(order.itemList)}</td>
									<td>{order.price}</td>
									<td>{order.tip}</td>
									<td>{order.status}</td>
                                    <td>
										{
											(isManagerUser() || isBaristaUser()) && order.status === "PLACED" ?
											<button className='btn btn-info' onClick={() => setMessage({ type: 'none', content: '' })
											                                            || fulfillOrder(order.id)}>Fulfill</button> :
											<button className='btn btn-info' disabled>Fulfilled</button>
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