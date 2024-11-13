import React from 'react'
import { useEffect, useState } from 'react'
import { getTax, updateTax } from '../services/TaxService'
import NotificationPopup from './NotificationPopup'
import PriceInput from './PriceInput'

const TaxComponent = () => {

  const [tax, setTax] = useState(0.0)
  const [message, setMessage] = useState({type: "none", content:""})

  useEffect(() => {
    getTax().then((response) => {
      console.log(response.data)
      setTax(response.data)
    }).catch(error => {
      console.error(error)
    })
  }, []) //Empty array to make the effect run only once on page load (https://stackoverflow.com/questions/53120972/how-to-call-loading-function-with-react-useeffect-only-once)

  function saveNewTax(e) {
    e.preventDefault()
	if (tax > 50) {
	  setMessage({type:"error", content: "The largest allowed tax rate is 50%."});
      return
	}
    console.log("Setting tax to " + tax.toString() + "%")

    updateTax(tax).then((response) => {
      setMessage({type:"success", content: "Global sales tax rate set to " + tax.toString() + "%."})
      console.log(response.data)
    }).catch(error => {
      setMessage({type:"error", content: "Could not update tax. Check your connection."});
      console.error(error)
    })
  }

  return (
    <div className='container'>
      <br />
	  {message.type != "none" && <NotificationPopup type={message.type} content={message.content} setParentMessage={setMessage} />}
      <br />
      <div className='row'>
        <div className='card col-md-6 offset-md-3 offset-md-3'>
          <h2 className='text-center'>Update Sales Tax</h2>
          
          <div className='card-body'>
            <form onSubmit={(e) => saveNewTax(e)}>

              <div className='form-group mb-2'>
                <label className='form-label'>Rate (as a percentage, ex 2 for 2.0%)</label>
                <PriceInput
                  value={tax}
                  onChange={value => setTax(value)}
                  placeholder='Enter tax rate'
                  required
				  dontLimitDecimalPlaces
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

export default TaxComponent