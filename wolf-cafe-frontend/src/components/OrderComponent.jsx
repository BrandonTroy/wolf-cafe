import React, { useEffect, useState } from 'react'
import { getAllItems } from '../services/ItemService'
import { order } from '../services/OrderService'

/** Provides functionality to order an item, pay for it, and receive change.*/
const OrderComponent = () => {

  const [items, setItems] = useState([])
  const [amtPaid, setAmtPaid] = useState([])
  const [change, setChange] = useState(null)

  const [errors, setErrors] = useState({
    general: ""
  })

  useEffect(() => {
    fetchItems()
  }, [])

  function fetchItems() {
    getAllItems().then((response) => {
      setItems(response.data)
    }).catch(error => {
      console.error(error)
    })
  }

  function makeOrder(id, amtPaid) {

    if (validateForm()) {
      order(id, amtPaid).then((response) => {
        fetchItems()
        setAmtPaid(0)
        setChange(response.data)
      }).catch(error => {
        console.error(error)
        const errorsCopy = { ...errors }
        if (error.response.status == 409) {
          errorsCopy.general = "Insufficient funds to pay."
        }
        if (error.response.status == 400) {
          errorsCopy.general = "Insufficient inventory."
        }
        setErrors(errorsCopy)
        setChange(null)
      })
    }
  }

  function validateForm() {
    let valid = true
    const errorsCopy = { ...errors }

    if (amtPaid < 0) {
      errorsCopy.general = "Amount paid must be a positive integer."
    }

    setErrors(errorsCopy)
    return valid
  }

  function getGeneralErrors() {
    if (errors.general) {
      return <div className="p-3 mb-2 bg-danger text-white">{errors.general}</div>
    }
  }

  return (
    <div className="container">
      <h2 className="text-center">List of Items</h2>
      {getGeneralErrors()}
      {change && <div className="p-3 bg-success text-white">Order Success! Your change is: {change}</div>}
      <br /><br />
      <div className="card-body">
        <form>
          <div className="form-group mb-2">
            <label className="form-label">Amount Paid</label>
            <input
              type="number"
              step="0.01"
              name="amtPaid"
              placeholder="How much are you paying?"
              value={amtPaid}
              onChange={(e) => setAmtPaid(e.target.value)}
              className={`form-control ${errors.general ? "is-invalid" : ""}`}
            >
            </input>
          </div>
        </form>
      </div>

      <table className="table table-striped table-bordered mt-4">
        <thead>
          <tr>
            <th>Item Name</th>
            <th>Item Price</th>
            <th>Actions</th>
          </tr>
        </thead>
        <tbody>
          {
            items.map(item =>
              <tr key={item.id}>
                <td>{item.name}</td>
                <td>{item.price}</td>
                <td>
                  <button className="btn btn-primary" onClick={() => makeOrder(item.name, amtPaid)}
                    style={{ marginLeft: '10px' }}
                  >Order</button>
                </td>
              </tr>)
          }
        </tbody>
      </table>
    </div>
  )

}

export default OrderComponent