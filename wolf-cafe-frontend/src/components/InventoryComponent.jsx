import { useEffect, useState } from 'react'
import { getAllItems } from '../services/ItemService';
import { getInventory, updateInventory } from '../services/InventoryService'
import NotificationPopup from './NotificationPopup'

/** Creates the page for updating the inventory. */
const InventoryComponent = () => {

  const [items, setItems] = useState([])
  const [inventory, setInventory] = useState([])
  const [quantityAdded, setQuantityAdded] = useState({})
  const [message, setMessage] = useState({type: "none", content:""})

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
	  setMessage({type:"success", content: "Inventory added."});
    }).catch(error => {
      console.error(error)
	  setMessage({type:"error", content: "Input must be a number."});
    })
  }

  function validateForm() {
    for (const value of Object.values(quantityAdded)) {
      if (value < 0) {
		setMessage({type:"error", content: "Cannot add a negative quantity."});
		return false;
      }
    }

    if (Object.values(quantityAdded).every(value => value === 0)) {
		setMessage({type:"error", content: "Add at least one item."});
		return false;
    }

    return true;
  }


  return (
    <div className="container">
      <br />
	  {message.type != "none" && <NotificationPopup type={message.type} content={message.content} setParentMessage={setMessage} />}
	  <br />
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
                  }}
                />
              </td>
            </tr>)
          }
        </tbody>
      </table>
      <br />
      <button className="btn btn-success" onClick={() => setMessage({ type: 'none', content: '' }) || modifyInventory()}>Add Inventory</button>
    </div>
  )
}

export default InventoryComponent
