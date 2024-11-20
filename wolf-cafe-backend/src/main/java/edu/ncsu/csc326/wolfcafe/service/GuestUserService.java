package edu.ncsu.csc326.wolfcafe.service;

import java.util.List;

import edu.ncsu.csc326.wolfcafe.dto.GuestDto;
import edu.ncsu.csc326.wolfcafe.dto.UserDto;
import edu.ncsu.csc326.wolfcafe.entity.User;
import edu.ncsu.csc326.wolfcafe.exception.ResourceNotFoundException;
import edu.ncsu.csc326.wolfcafe.service.impl.Order;

public interface GuestUserService {
	/**
     * Creates a new guest user with a unique username.
     * The username is generated as "guest-" + (current user count + 1).
     * 
     * @return The unique guest username.
     */
	public String createGuestUser();
	
	/**
     * Creates a new order for a specific guest.
     * 
     * @param guestUser   The guest user placing the order.
     * @param orderDetails The details of the order.
     */
	public void createOrderForGuest(User guestUser, String orderDetails);
	
	/**
     * Retrieves the order history for a specific guest user.
     * 
     * @param guestUser The guest user whose order history is being retrieved.
     * @return A list of orders associated with the guest user.
     */
	public List<OrderDto> getOrderHistoryForGuest(User guestUser);
	
	/**
     * Returns the user with the given id.
     *
     * @param userId
     *            user's id
     * @return the user with the given id
     * @throws ResourceNotFoundException
     *             if the user doesn't exist
     */
    GuestDto getUserById ( Long userId );
}
