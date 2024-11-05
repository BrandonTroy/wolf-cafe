import React from 'react'
import { useEffect, useState } from 'react'
import { getItemById, saveItem, updateItem } from '../services/ItemService'
import { useNavigate, useParams } from 'react-router-dom'

const ItemComponent = () => {

  const [name, setName] = useState('')
  const [description, setDescription] = useState('')
  const [price, setPrice] = useState(0)
  const { id } = useParams()

  const navigate = useNavigate()

  useEffect(() => {
    if(id) {
      getItemById(id).then((response) => {
        console.log(response.data)
        setName(response.data.name)
        setDescription(response.data.description)
				setPrice(response.data.price)
      }).catch(error => {
        console.error(error)
      })
    }
  }, [id])

  function saveOrUpdateItem(e) {
    e.preventDefault()
    const item = {name, description, price}
    console.log(item)

    if (id) {
      updateItem(id, item).then((response) => {
        console.log(response.data)
        navigate('/items')
      }).catch(error => {
        console.error(error)
      })
    } else {
      saveItem(item).then((response) => {
        console.log(response.data)
        navigate('/items')
      }).catch(error => {
        console.error(error)
      })
    }
  }

  function pageTitle() {
    if (id) {
      return <h2 className='text-center'>Update Item</h2>
    } else {
      return <h2 className='text-center'>Add Item</h2>
    }
  }
  
  function setPriceWithErrorChecks(new_price) {
	if (new_price.includes('.')) {
		new_price = new_price.substring(0, new_price.indexOf('.') + 3);
	}
	if (new_price.startsWith('-')) {
		new_price = new_price.substring(1);
	}
	setPrice(parseFloat(new_price));
  }

  return (
    <div className='container'>
      <br />
      <br />
      <div className='row'>
        <div className='card col-md-6 offset-md-3 offset-md-3'>
          { pageTitle() }
          
          <div className='card-body'>
            <form>
              <div className='form-group mb-2'>
                <label className='form-label'>Item Name:</label>
                <input 
                  type='text'
                  className='form-control'
                  placeholder='Enter Item Name'
                  name='name'
                  value={name}
                  onChange={(e) => setName(e.target.value)}
                >
                </input>
              </div>

              <div className='form-group mb-2'>
                <label className='form-label'>Item Description:</label>
                <input 
                  type='text'
                  className='form-control'
                  placeholder='Enter Item Description'
                  name='description'
                  value={description}
                  onChange={(e) => setDescription(e.target.value.substring(0, Math.min(300, e.target.value.length)))}
                >
                </input>
              </div>

              <div className='form-group mb-2'>
                <label className='form-label'>Item Price:</label>
                <input 
                  type='number'
				  step='0.01'
                  className='form-control'
                  placeholder='Enter Item Price'
                  name='price'
                  value={price}
                  onChange={(e) => setPriceWithErrorChecks(e.target.value)}
                >
                </input>
              </div>

              <button type='submit' className='btn btn-success' onClick={(e) => saveOrUpdateItem(e)}>Submit</button>
            </form>
          </div>
        </div>
      </div>
    </div>
  )
}

export default ItemComponent