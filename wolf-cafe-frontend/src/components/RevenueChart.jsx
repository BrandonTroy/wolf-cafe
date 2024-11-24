import React, { useState, useMemo } from 'react';
import { BarChart, Bar, XAxis, YAxis, CartesianGrid, Tooltip, Legend, ResponsiveContainer } from 'recharts';
import { ChevronLeft, ChevronRight } from 'lucide-react';

const buttonStyles = {
  display: 'inline-flex',
  alignItems: 'center',
  padding: '0.5rem 0.75rem',
  fontSize: '0.875rem',
  fontWeight: 500,
  transition: 'all 0.2s',
};


const RevenueChart = ({ orders }) => {
  const [weekOffset, setWeekOffset] = useState(0);

  const earliestOrderDate = useMemo(() => {
    if (!orders.length) return null;
    return orders.reduce((earliest, order) => {
      return order.date < earliest ? order.date : earliest;
    }, orders[0].date);
  }, [orders]);

  function getDay(date) {
    return date.toISOString().split('T')[0]
  }

  const generateWeekDates = (offset) => {
    const today = new Date();
    const dates = [];
    
    for (let i = 6; i >= 0; i--) {
      const date = new Date(today);
      date.setDate(date.getDate() - i + (offset * 7));
      dates.push(getDay(date));
    }
    return dates;
  };

  const weekDates = generateWeekDates(weekOffset);
  const isCurrentWeek = weekOffset === 0;
  const isEarliestWeek = earliestOrderDate && weekDates[0] <= earliestOrderDate;

  const chartData = useMemo(() => {
    return weekDates.map(date => {
      const dayOrders = orders.filter(order => getDay(new Date(order.date)) === date);
      const dailyTotal = dayOrders.reduce((acc, order) => {
        return {
          revenue: acc.revenue + order.price,
          tips: acc.tips + order.tip
        };
      }, { revenue: 0, tips: 0 });

      return {
        date: new Date(date).toLocaleDateString('en-US', { 
          month: 'short', 
          day: 'numeric'
        }),
        fullDate: date,
        revenue: dailyTotal.revenue,
        tips: dailyTotal.tips
      };
    });
  }, [weekDates, orders]);

  // Just show month and year from the first date of the week
  const dateDisplay = new Date(weekDates[0]).toLocaleDateString('en-US', { 
    month: 'long',
    year: 'numeric'
  });

  return (
    <div>
      <div style={{
        display: 'flex',
        justifyContent: 'space-between',
        alignItems: 'center',
        marginBottom: '1rem',
        padding: '0 1rem',
        width: '100%'
      }}>
        <button
          onClick={() => setWeekOffset(prev => prev - 1)}
          disabled={isEarliestWeek}
          className="btn btn-outline-secondary"
          style={buttonStyles}
        >
          <ChevronLeft style={{ width: '1rem', height: '1rem', marginRight: '0.25rem' }} />
          Previous Week
        </button>
        
        <span style={{ fontWeight: 500 }}>
          {dateDisplay}
        </span>

        <button
          onClick={() => setWeekOffset(prev => prev + 1)}
          disabled={isCurrentWeek}
          className="btn btn-outline-secondary"
          style={buttonStyles}
        >
          Next Week
          <ChevronRight style={{ width: '1rem', height: '1rem', marginLeft: '0.25rem' }} />
        </button>
      </div>

      <div style={{ height: '400px' }}>
        <ResponsiveContainer width="100%" height="100%">
          <BarChart
            data={chartData}
            margin={{
              top: 20,
              right: 30,
              left: 20,
              bottom: 5,
            }}
          >
            <CartesianGrid strokeDasharray="3 3" />
            <XAxis dataKey="date" />
            <YAxis label={{ value: 'Amount ($)', angle: -90, position: 'insideLeft' }} />
            <Tooltip 
              formatter={(value, name) => [`$${value.toFixed(2)}`, name.charAt(0).toUpperCase() + name.slice(1)]}
              contentStyle={{ backgroundColor: '#f5f5f5' }}
              labelFormatter={(label) => `Date: ${label}`}
            />
            <Legend />
            <Bar dataKey="revenue" fill="#0d6efd" name="Revenue" />
            <Bar dataKey="tips" fill="#198754" name="Tips" />
          </BarChart>
        </ResponsiveContainer>
      </div>
    </div>
  );
};

export default RevenueChart;