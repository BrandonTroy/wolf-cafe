package edu.ncsu.csc326.wolfcafe.mapper;

import edu.ncsu.csc326.wolfcafe.dto.OrderDto;
import edu.ncsu.csc326.wolfcafe.entity.Order;

public class OrderMapper {

	/**
	 * Converts a User entity to UserDto
	 * 
	 * @param user User to convert
	 * @return UserDto object
	 */
	public static OrderDto mapToOrderDto(Order order) {
		OrderDto o = new OrderDto(order.getId(), order.getItemList(), order.getCustomerId(), order.getTip(), order.getTotalPrice(), order.getStatus(), order.getDate());
		return o;
	}

	/**
	 * Converts a UserDto object to a User entity.
	 * 
	 * @param userDto UserDto to convert
	 * @return User entity
	 */
	public static Order mapToOrder(OrderDto orderDto) {
		Order o = new Order(orderDto.getId(), orderDto.getItemList(), orderDto.getCustomerId(), orderDto.getTip(), orderDto.getTotalPrice(), orderDto.getStatus(), orderDto.getDate());
		return o;
	}
	
}
