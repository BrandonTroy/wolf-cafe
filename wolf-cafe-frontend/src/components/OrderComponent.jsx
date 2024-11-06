import React, { useEffect, useState, useContext } from 'react'
import { getAllItems } from '../services/ItemService'
import { order } from '../services/OrderService'
import { OrderContext } from '../OrderContext'
import PriceInput from './PriceInput'

/** Provides functionality to order an item, pay for it, and receive change.*/
const OrderComponent = () => {
  const [items, setItems] = useState([])
  const { order, setOrder } = useContext(OrderContext)
  const [subTotal, setSubtotal] = useState(0)
  const [tax, setTax] = useState(0.02)
  const [tip, setTip] = useState(0.15)
  const [isCustomTip, setIsCustomTip] = useState(false)

  useEffect(() => {
    getAllItems().then((response) => {
      setItems(response.data)
    }).catch(error => {
      console.error(error)
    })

    // TODO: fetch tax rate from backend
  }, [])

  useEffect(() => {
    if (items.length == 0) return
    setSubtotal(Object.keys(order).reduce((acc, id) => {
      const item = items.find(item => item.id === parseInt(id))
      return acc + item.price * order[id]
    }, 0))
  }, [items, order])

  function makeOrder(id, amtPaid) {
    console.log("Placing Order - TODO")
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
      <br /><br />
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
            <option value="0.1">10%</option>
            <option value="0.15">15%</option>
            <option value="0.2">20%</option>
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
          onClick={() => makeOrder(order, subTotal * (1 + tax + (tip || 0)))}
        >
          Place Order
        </button>
      </form>
      <br /><br />
    </div>
  )

}

export default OrderComponent