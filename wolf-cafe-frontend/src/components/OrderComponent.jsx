import React, { useEffect, useState, useContext } from 'react'
import { getAllItems } from '../services/ItemService'
import { placeOrder } from '../services/OrderService'
import { OrderContext } from '../OrderContext'
import { getTax } from '../services/TaxService'
import PriceInput from './PriceInput'
import NotificationPopup from './NotificationPopup'

/** Provides functionality to order an item, pay for it, and receive change.*/
const OrderComponent = () => {
  const [items, setItems] = useState([])
  const { order, setOrder } = useContext(OrderContext)
  const [subTotal, setSubtotal] = useState(0)
  const [tax, setTax] = useState(0)
  const [tip, setTip] = useState(0.15)
  const [isCustomTip, setIsCustomTip] = useState(false)
  const [message, setMessage] = useState({ type: "none", content: "" })

  useEffect(() => {
    getAllItems().then((response) => {
      setItems(response.data)
    }).catch(error => {
      console.error(error)
    })

    getTax().then((response) => {
      setTax(response.data / 100)
    }).catch(error => {
      console.error(error)
    })
  }, [])

  useEffect(() => {
    if (items.length == 0) return
    setSubtotal(Object.keys(order).reduce((acc, id) => {
      const item = items.find(item => item.id === parseInt(id))
      return acc + item.price * order[id]
    }, 0))
  }, [items, order])

  /** From Google AI overview for the search "checking for empty object javascript" */
  function isEmpty(obj) {
    return Object.keys(obj).length === 0;
  }

  function makeOrder(amtTipped) {
    console.log(order.toString())
    if (isEmpty(order)) {
      setMessage({ type: "error", content: "Go to the items page to add items to your order." })
      return
    }

    const orderDto = { itemList: order, tip: amtTipped }
    placeOrder(orderDto).then((response) => {
      setOrder({})
      setMessage({ type: "success", content: "Your order is being prepared (#" + response.data.id + "). Thank you!\nGo to the Order History tab to see when your order is ready and mark it as picked up." })
    }).catch(error => {
      if (error.status === 409) {
        setMessage({ type: "error", content: items[error.response.data.id].name + " is currently sold out, or the quantity available is less than the quantity you specified in your order. We apologize for the inconvenience." })
      } else {
        setMessage({ type: "error", content: "Could not place an order. Check your network connection." })
      }
      console.error(error)
    })
  }

  function removeItemFromOrder(id) {
    const newOrder = { ...order };
    delete newOrder[id];
    setOrder(newOrder);
  }

  function handleTipChange(e) {
    const value = e.target.value;
    if (value === 'custom') {
      setTip(0);
      setIsCustomTip(true);
    } else {
      setTip(parseFloat(value));
      setIsCustomTip(false);
    }
  }

  return (
    <div className="container">
      <br />
      {message.type != "none" && <NotificationPopup type={message.type} content={message.content} setParentMessage={setMessage} />}
      <br />
      <h2 className='text-center mx-auto mb-3'>Your Order</h2>

      <table className="table table-striped table-bordered mt-4">
        <thead>
          <tr>
            <th>Name</th>
            <th>Price</th>
            <th>Quantity</th>
            <th>Actions</th>
          </tr>
        </thead>
        <tbody>
          {
            items
              .filter(item => Object.keys(order).includes(item.id.toString()))
              .map(item =>
                <tr key={item.id}>
                  <td>{item.name}</td>
                  <td>{item.price}</td>
                  <td>
                    <input
                      type="number"
                      className="form-control"
                      step="1"
                      min="1"
                      style={{ width: 100, margin: 'auto' }}
                      value={order[item.id]}
                      onChange={(e) => setOrder({ ...order, [item.id]: parseInt(e.target.value) })}
                      onBlur={() => setOrder({ ...order, [item.id]: order[item.id] || 1 })}
                    />
                  </td>
                  <td>
                    <button className="btn btn-danger" onClick={() => removeItemFromOrder(item.id)}>Remove</button>
                  </td>
                </tr>)
          }
        </tbody>
      </table>

      <form className='mt-5'>
        <div className="d-flex justify-content-center gap-4">
          <div>Subtotal: <strong>${subTotal.toFixed(2)}</strong></div>
          <div>Tax: <strong>${(subTotal * tax).toFixed(2)}</strong></div>
          <div>Total: <strong>${(subTotal * (1 + tax)).toFixed(2)}</strong></div>
        </div>

        <div className="d-flex justify-content-center align-items-center mt-4">
          <label htmlFor="tip" className="me-3">Add Tip:</label>
          <select
            id="tip"
            className="form-select me-3"
            style={{ width: 'auto' }}
            value={isCustomTip ? 'custom' : tip}
            onChange={handleTipChange}
          >
            <option value="0">No Tip</option>
            <option value="0.15">15%</option>
            <option value="0.2">20%</option>
            <option value="0.25">25%</option>
            <option value="custom">Custom</option>
          </select>
          <PriceInput
            value={Math.round(tip * subTotal * 100) / 100}
            onChange={(value) => setTip(value / subTotal)}
            disabled={!isCustomTip}
            style={{ width: 100 }}
          />

        </div>

        <div className='mt-4'>Final Total: <strong>${(subTotal * (1 + tax + (tip || 0))).toFixed(2)}</strong></div>

        <button
          type="button"
          className="btn btn-primary mt-4"
          onClick={() => setMessage({ type: 'none', content: '' }) || makeOrder(subTotal * tip)}
          disabled={isEmpty(order)}
        >
          Place Order
        </button>
      </form>
      <br /><br />
    </div>
  )

}

export default OrderComponent