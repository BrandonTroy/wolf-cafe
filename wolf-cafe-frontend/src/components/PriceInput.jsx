import React, { useState, useEffect } from 'react';

const PriceInput = ({
  value,
  onChange,
  disabled = false,
  style = {},
  min = 0,
  placeholder = "0.00",
  required = false,
  dontLimitDecimalPlaces = false
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
    if (!dontLimitDecimalPlaces && parts.length === 2 && parts[1].length > 2) {
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

  const handleKeyDown = (e) => {
    // Prevent the browser's built-in validation from clearing the input
    if (e.key === 'e' || e.key === 'E' || e.key === '-' || e.key === '+') {
      e.preventDefault();
    }
  };

  const handleBlur = () => {
    let formattedValue = displayValue;
    if (displayValue === '' || isNaN(parseFloat(displayValue))) {
      formattedValue = min.toFixed(2);
    } else {
      const numericValue = parseFloat(displayValue);
      formattedValue = dontLimitDecimalPlaces ? numericValue : numericValue.toFixed(2);
    }
    setDisplayValue(formattedValue);
    onChange(parseFloat(formattedValue));
  };

  return (
    <input
      type="number"
      inputMode="decimal"
      min={min}
      step={dontLimitDecimalPlaces ? "any" : "0.01"}
      className="form-control"
      style={style}
      value={displayValue}
      onChange={handleChange}
      onKeyDown={handleKeyDown}
      onBlur={handleBlur}
      disabled={disabled}
      placeholder={placeholder}
      required={required}
    />
  );
};

export default PriceInput;