package edu.ncsu.csc326.wolfcafe.service;

import edu.ncsu.csc326.wolfcafe.dto.OrderDto;
import edu.ncsu.csc326.wolfcafe.entity.Status;
import java.util.Map;


/**
 * Interface defining the ordering behaviors.
 * 
 * @author Ryan Hinshaw (rthinsha)
 */
public interface OrderService {

	/**
	 * Adds an Order to the database of Orders, so long as the Inventory has enough Items 
	 * to possibly fulfill the Order. 
	 * @param orderDto The Order to be added to the database of Orders and subtract from 
	 * the Inventory
	 * @return The orderDto if successfully added, and throws an IllegalArgumentException if not
	 */
	OrderDto addOrder(OrderDto orderDto);
	
	/**
	 * Updates the OrderDto passed in as a parameter and in the database to the status passed in. 
	 * Used when staff members fulfill and order and customers pickup or cancel an order.
	 * @param orderDto The Order to be updated
	 * @param status The status to update the Order to
	 * @return The updated Order
	 */
	OrderDto editOrder(OrderDto orderDto, Status status);

	/**
	 * Returns the history of Orders in the database, but is dependent on the user who calls 
	 * this method. Staff members can see every Order in the system, while customers (and guests) 
	 * can only see their orders. Since usernames are unique, they're verified against the 
	 * user repository
	 * @param username The username of the user calling this method 
	 * @return The Order History appropriate to the role of the caller
	 */
	Map<Long, OrderDto> getOrderHistory(String username);
}
