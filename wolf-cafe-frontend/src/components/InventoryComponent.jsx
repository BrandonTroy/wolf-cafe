import { useEffect, useState } from 'react'
import { getAllItems } from '../services/ItemService';
import { getInventory, updateInventory } from '../services/InventoryService'

/** Creates the page for updating the inventory. */
const InventoryComponent = () => {

  const [items, setItems] = useState([])
  const [inventory, setInventory] = useState([])
  const [quantityAdded, setQuantityAdded] = useState({})
  const [successMessage, setSuccessMessage] = useState('')

  useEffect(() => {
    getAllItems().then((response) => {
      setItems(response.data)
    })
    
    getInventory().then((response) => {
      setInventory(response.data.itemQuantities);
      // Constructing map from each ingredient name to 0
      const initialQuantityAdded = Object.keys(response.data.itemQuantities).reduce((acc, id) => {
        acc[id] = 0
        return acc
      }, {})
      setQuantityAdded(initialQuantityAdded);      
    }).catch(error => {
      console.error(error)
    })
  }, [])


  function modifyInventory() {
    if (!validateForm()) return

    updateInventory({ itemQuantities: quantityAdded }).then((response) => {
      console.log(response.data)
      setInventory(response.data.itemQuantities)
      const resetQuantities = Object.keys(response.data.itemQuantities).reduce((acc, id) => {
        acc[id] = 0
        return acc
      }, {})
      setQuantityAdded(resetQuantities)
      setSuccessMessage('Addition Success!')
    }).catch(error => {
      console.error(error)
	  alert("Error: Input must be a number")
    })
  }

  function validateForm() {
    const errors = []

    for (const [key, value] of Object.entries(quantityAdded)) {
      if (value < 0) {
        errors.push(`Error: Cannot add a negative number.`)
      }
    }

    if (Object.values(quantityAdded).every(value => value === 0)) {
      errors.push("Error: Add at least one ingredient.")
    }

    if (errors.length) alert(errors.join("\n"))

    return errors.length == 0
  }


  return (
    <div className="container">
      <br /><br />
      {successMessage && (
        <p style={{ color: 'green', fontSize: '30px', fontWeight: 'bold' }}>
          {successMessage}
        </p>
      )}
      <h2 className="mb-4">Inventory</h2>

      <table className="table table-striped table-bordered text-start">
        <thead>
          <tr>
            <th>Item</th>
            <th>Current Amount</th>
            <th>Added Amount</th>
          </tr>
        </thead>
        <tbody>
          {
            items.map(item => 
            <tr key={item.name}>
              <td>{item.name}</td>
              <td>{inventory[item.id]}</td>
              <td>
                <input
                  className="form-control"
                  type="number"
                  name={item.name}
                  value={quantityAdded[item.id] || ''}
                  onChange={(e) => {
                    const value = e.target.value === '' ? '' : parseInt(e.target.value);
                    setQuantityAdded({ ...quantityAdded, [item.id]: value })
                    setSuccessMessage('')
                  }}
                />
              </td>
            </tr>)
          }
        </tbody>
      </table>
      <br />
      <button className="btn btn-success" onClick={modifyInventory}>Add Inventory</button>
    </div>
  )
}

export default InventoryComponent
