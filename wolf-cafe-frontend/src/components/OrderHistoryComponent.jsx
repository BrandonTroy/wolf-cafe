import { useEffect, useState, useContext } from 'react'
import { isBaristaUser, isManagerUser, isCustomerUser, isGuestUser } from '../services/AuthService'
import { getItemById } from '../services/ItemService'
import { getUser } from '../services/UserService'
import { getAllOrders, updateOrder } from '../services/OrderService'
import { OrderContext } from '../OrderContext'
import NotificationPopup from './NotificationPopup'
import RevenueChart from './RevenueChart'

const OrderHistoryComponent = () => {
  const [orders, setOrders] = useState([])
  const [message, setMessage] = useState({ type: "none", content: "" })
  const [items, setItems] = useState({})
  const { setOrder } = useContext(OrderContext)
  const [showingTable, setShowingTable] = useState(true)

  useEffect(() => {
    listOrders()
  }, [])

  async function listOrders() {
    try {
      const response = await getAllOrders()
      let orders = Object.values(response.data).sort((a, b) => new Date(b.date) - new Date(a.date))

      if (isBaristaUser() || isManagerUser()) {
        orders = await Promise.all(orders.map(async (order) => {
          try {
            const userResponse = await getUser(order.customerId)
            order.username = userResponse.data.username
            return order
          } catch (error) {
            console.error(error)
            return order
          }
        }))
      }

      const itemDetails = {}
      await Promise.all(orders.map(async (order) => {
        await Promise.all(Object.entries(order.itemList).map(async ([key, value]) => {
          try {
            const response = await getItemById(parseInt(key))
            itemDetails[key] = response.data.name
          } catch (error) {
            console.error(error)
          }
        }))
      }))

      setItems(itemDetails)
      setOrders(orders)
    } catch (error) {
      console.error(error)
    }
  }

  function fulfillOrder(order) {
    order.status = "FULFILLED"
    updateOrder(order.id, order).then(() => {
      listOrders()
      setMessage({ type: "success", content: "Order fulfilled (#" + order.id + ")." })
    }).catch(error => {
      if (error.status === 409) {
        setMessage({ type: "error", content: "The order you selected has already been fulfilled." })
      } else if (error.status === 404) {
        setMessage({ type: "error", content: "The order you are trying to fulfill could not be found in the system." })
      } else {
        setMessage({ type: "error", content: "Could not fulfill order. Check your network connection." })
      }
      console.error(error)
    })
  }

  function pickupOrder(order) {
    order.status = "PICKEDUP"
    updateOrder(order.id, order).then(() => {
      listOrders()
      setMessage({ type: "success", content: "Your order has been picked up (#" + order.id + "). Thank you!" })
    }).catch(error => {
      if (error.status === 409) {
        setMessage({ type: "error", content: "The order you selected has already been picked up." })
      } else if (error.status === 404) {
        setMessage({ type: "error", content: "The order you are trying to pick up could not be found in the system." })
      } else {
        setMessage({ type: "error", content: "Could not pick up order. Check your network connection." })
      }
      console.error(error)
    })
  }

  function cancelOrder(order) {
    order.status = "CANCELED"
    updateOrder(order.id, order).then(() => {
      listOrders()
      setMessage({ type: "success", content: "Your order has been canceled (#" + order.id + ")." })
    }).catch(error => {
      if (error.status === 409) {
        setMessage({ type: "error", content: "The order you selected has already been canceled." })
      } else if (error.status === 404) {
        setMessage({ type: "error", content: "The order you are trying to cancel could not be found in the system." })
      } else {
        setMessage({ type: "error", content: "Could not cancel order. Check your network connection." })
      }
      console.error(error)
    })
  }

  function placeAgain(order) {
    setOrder(order.itemList)
    setMessage({ type: "success", content: "Order added to your cart with the same items and quantities." })
  }

  function displayItems(itemList) {
    return Object.entries(itemList).map(([key, value]) => {
      const itemName = items[key] || "Loading..."
      return `${itemName} (x${value})`
    }).join(", ")
  }

  return (
    <div className='container'>
      <br />
      {message.type != "none" && <NotificationPopup type={message.type} content={message.content} setParentMessage={setMessage} />}
      <br />
      <div className='d-flex justify-content-between align-items-center m-atuo'>
        <h2 className='text-center mx-auto mb-4'>Past Orders</h2>
      </div>
      {isManagerUser() &&
        <div className='d-flex justify-content-center align-items-center gap-1' style={{ marginTop: '-0.5rem', marginBottom: '2rem' }}>
          <button className={'btn ' + (showingTable ? 'btn-primary' : 'btn-outline-secondary')} disabled={showingTable} onClick={() => setShowingTable(true)}>Table</button>
          <button className={'btn ' + (!showingTable ? 'btn-primary' : 'btn-outline-secondary')} disabled={!showingTable} onClick={() => setShowingTable(false)}>Graph</button>
        </div>
      }

      {showingTable ?
        <div>
          <table className='table table-bordered table-striped'>
            <thead>
              <tr>
                <th>Date</th>
                {(isManagerUser() || isBaristaUser()) &&
                  <th>Username</th>
                }
                <th>Items</th>
                <th>Price</th>
                <th>Status</th>
                <th>Actions</th>
              </tr>
            </thead>
            <tbody>
              {
                orders.map((order) =>
                  <tr key={order.id}>
                    <td>{order.date}</td>
                    {(isManagerUser() || isBaristaUser()) &&
                      <td>{order.username}</td>
                    }
                    <td>{displayItems(order.itemList)}</td>
                    <td>{(order.price + order.tax + order.tip).toFixed(2)}</td>
                    {
                      order.status != "PICKEDUP" ?
                        <td>{order.status[0] + order.status.substring(1).toLowerCase()}</td> :
                        <td>Picked Up</td>
                    }
                    <td className='d-flex justify-content-center gap-2'>
                      {/* Manager and Barista Actions */}
                      {
                        (isManagerUser() || isBaristaUser()) && order.status === "PLACED" &&
                        <button className='btn btn-info' onClick={() => setMessage({ type: 'none', content: '' }) || fulfillOrder(order)}>Fulfill</button>
                      }
                      {
                        (isManagerUser() || isBaristaUser()) && (order.status === "FULFILLED" || order.status === "PICKEDUP") &&
                        <button className='btn btn-info' disabled>Fulfilled</button>
                      }
                      {/* Customer and Guest Actions */}
                      {
                        (isCustomerUser() || isGuestUser()) && order.status === "FULFILLED" &&
                        <button className='btn btn-info' onClick={() => setMessage({ type: 'none', content: '' }) || pickupOrder(order)}>Pick Up</button>
                      }
                      {
                        (isCustomerUser() || isGuestUser()) && order.status === "PICKEDUP" &&
                        <button className='btn btn-secondary' onClick={() => setMessage({ type: 'none', content: '' }) || placeAgain(order)}>Place Again</button>
                      }
                      {/* Both can cancel */}
                      {
                        order.status === "PLACED" &&
                        <button className='btn btn-danger' onClick={() => setMessage({ type: 'none', content: '' }) || cancelOrder(order)}>Cancel</button>
                      }
                    </td>
                  </tr>
                )
              }
            </tbody>
          </table>
        </div>
        :
        <RevenueChart orders={orders} />
      }
      <br />
      <br />
    </div>
  )
}

export default OrderHistoryComponent