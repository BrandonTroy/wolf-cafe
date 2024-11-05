import React, { createContext, useState, useEffect } from 'react';

// Create the context
export const OrderContext = createContext();

// Create a provider component
export const OrderProvider = ({ children }) => {
  // Load order from local storage when the component mounts
  const [order, setOrder] = useState(() => JSON.parse(localStorage.getItem('order')) || []);

  // Save order to local storage whenever it changes
  useEffect(() => {
    localStorage.setItem('order', JSON.stringify(order));
  }, [order]);

  return (
    <OrderContext.Provider value={{ order, setOrder }}>
      {children}
    </OrderContext.Provider>
  );
};