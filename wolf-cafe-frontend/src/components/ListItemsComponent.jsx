import React, { useEffect, useState, useContext } from 'react'
import { useNavigate } from 'react-router-dom'
import { isBaristaUser, isManagerUser, isCustomerUser, isGuestUser } from '../services/AuthService'
import { getAllItems, deleteItemById } from '../services/ItemService'
import { getInventory } from '../services/InventoryService'
import { OrderContext } from '../OrderContext'
import NotificationPopup from './NotificationPopup'

const ListItemsComponent = () => {
  const [items, setItems] = useState([])
  const [message, setMessage] = useState({ type: "none", content: "" })
  const [inventory, setInventory] = useState([])
  const { order, setOrder } = useContext(OrderContext)
  const navigate = useNavigate()

  useEffect(() => {
    listItems()
    getInventory().then((response) => {
      setInventory(response.data.itemQuantities);
    })
  }, [])

  function listItems() {
    getAllItems().then((response) => {
      setItems(response.data)
    }).catch(error => {
      console.error(error)
    })
  }

  function addNewItem() {
    navigate('/add-item')
  }

  function updateItem(id) {
    console.log(id)
    navigate(`/update-item/${id}`)
  }

  function deleteItem(id) {
    console.log(id)
    deleteItemById(id).then(() => {
      listItems()
    }).catch(error => {
      setMessage({ type: "error", content: "Failed to delete item; bad connection or item was already deleted." });
      console.error(error)
    })
  }


  return (
    <div className='container'>
      <br />
      {message.type != "none" && <NotificationPopup type={message.type} content={message.content} setParentMessage={setMessage} />}
      <br />
      <div className='d-flex justify-content-between align-items-center'>
        <h2 className='text-center mx-auto mb-3'>Items</h2>
        {
          isManagerUser() &&
          <button className='btn btn-primary mb-2 ml-auto' onClick={addNewItem}>Add Item</button>
        }
      </div>
      <div>
        <table className='table table-bordered table-striped'>
          <thead>
            <tr>
              <th>Item Name</th>
              <th>Description</th>
              <th>Price</th>
              {!isBaristaUser() && <th>Actions</th>}
            </tr>
          </thead>
          <tbody>
            {
              items.map((item) =>
                <tr key={item.id}>
                  <td>{item.name}</td>
                  <td>{item.description}</td>
                  <td>{item.price}</td>
                  {!isBaristaUser() && <td>
                    {
                      isManagerUser() && <button className='btn btn-info' onClick={() => updateItem(item.id)}>Update</button>
                    }
                    {
                      isManagerUser() && <button className='btn btn-danger' onClick={() => deleteItem(item.id)} style={{ marginLeft: "10px" }}>Delete</button>
                    }
                    {(isCustomerUser() || isGuestUser()) &&
                      (inventory[item.id] > 0 ?
                        <button
                          className='btn btn-primary'
                          onClick={() => setOrder({ ...order, [item.id]: 1 })}
                          disabled={Object.keys(order).includes(item.id.toString())}
                        >
                          Add to Order
                        </button>
                        :
                        <div className='text-danger my-1'>Out of stock</div>
                      )
                    }
                  </td>}
                </tr>
              )
            }
          </tbody>
        </table>
      </div>
    </div>
  )
}

export default ListItemsComponent