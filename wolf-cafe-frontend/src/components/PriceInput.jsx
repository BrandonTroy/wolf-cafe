import React, { useState, useEffect } from 'react';

const PriceInput = ({ 
  value, 
  onChange, 
  disabled = false,
  style = {},
  min = 0,
  placeholder = "0.00"
}) => {
  const [displayValue, setDisplayValue] = useState('');

  useEffect(() => {
    if (value !== undefined && value !== null) {
      setDisplayValue(value.toString());
    }
  }, [value]);

  const handleChange = (e) => {
    let rawValue = e.target.value;

    // Remove all non-digit and non-decimal characters
    let cleaned = rawValue.replace(/[^\d.]/g, '');

    // Ensure only one decimal point
    const parts = cleaned.split('.');
    if (parts.length > 2) {
      cleaned = `${parts[0]}.${parts.slice(1, 2).join('')}`;
    }

    // Limit to 2 decimal places
    if (parts.length === 2 && parts[1].length > 2) {
      cleaned = `${parts[0]}.${parts[1].slice(0, 2)}`;
    }

    setDisplayValue(cleaned);

    const numericValue = parseFloat(cleaned);
    if (!isNaN(numericValue) && numericValue >= min) {
      onChange(numericValue);
    } else if (cleaned === '') {
      onChange(min);
    }
  };

  const handleBlur = () => {
    let formattedValue = displayValue;
    if (displayValue === '' || isNaN(parseFloat(displayValue))) {
      formattedValue = min.toFixed(2);
    } else {
      const numericValue = parseFloat(displayValue);
      formattedValue = numericValue.toFixed(2);
    }
    setDisplayValue(formattedValue);
    onChange(parseFloat(formattedValue));
  };

  return (
    <input
      type="number"
      className="form-control"
      min={min}
      style={style}
      value={displayValue}
      onChange={handleChange}
      onBlur={handleBlur}
      disabled={disabled}
      placeholder={placeholder}
      inputMode="decimal"
    />
  );
};

export default PriceInput;