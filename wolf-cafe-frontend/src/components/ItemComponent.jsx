import React from 'react'
import { useEffect, useState } from 'react'
import { getItemById, saveItem, updateItem } from '../services/ItemService'
import { useNavigate, useParams } from 'react-router-dom'
import PriceInput from './PriceInput'

const ItemComponent = () => {

  const [name, setName] = useState('')
  const [description, setDescription] = useState('')
  const [price, setPrice] = useState('')
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

  return (
    <div className='container'>
      <br />
      <br />
      <div className='row'>
        <div className='card col-md-6 offset-md-3 offset-md-3'>
          { pageTitle() }
          
          <div className='card-body'>
            <form onSubmit={(e) => saveOrUpdateItem(e)}>
              <div className='form-group mb-2'>
                <label className='form-label'>Item Name:</label>
                <input 
                  type='text'
                  className='form-control'
                  placeholder='Enter Item Name'
                  name='name'
                  value={name}
                  onChange={(e) => setName(e.target.value)}
                  required
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
                  required
                >
                </input>
              </div>

              <div className='form-group mb-2'>
                <label className='form-label'>Item Price:</label>
                <PriceInput
                  value={price}
                  onChange={value => setPrice(value)}
                  placeholder='Enter Item Price'
                  required
                />
              </div>

              <button type='submit' className='btn btn-success'>Submit</button>
            </form>
          </div>
        </div>
      </div>
    </div>
  )
}

export default ItemComponent