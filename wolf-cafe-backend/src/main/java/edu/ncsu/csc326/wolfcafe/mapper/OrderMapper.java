package edu.ncsu.csc326.wolfcafe.mapper;

import edu.ncsu.csc326.wolfcafe.dto.OrderDto;
import edu.ncsu.csc326.wolfcafe.entity.Order;

/**
 * Converts between Orders and OrderDtos
 */
public class OrderMapper {

    /**
     * Converts a User entity to UserDto
     *
     * @param order
     *            Order to convert
     * @return OrderDto object
     */
    public static OrderDto mapToOrderDto ( final Order order ) {
        final OrderDto o = new OrderDto( order.getId(), order.getItemList(), order.getCustomerId(), order.getPrice(),
                order.getTax(), order.getTip(), order.getStatus(), order.getDate() );
        return o;
    }

    /**
     * Converts a UserDto object to a User entity.
     *
     * @param orderDto
     *            OrderDto to convert
     * @return Order entity
     */
    public static Order mapToOrder ( final OrderDto orderDto ) {
        final Order o = new Order( orderDto.getId(), orderDto.getItemList(), orderDto.getCustomerId(),
                orderDto.getPrice(), orderDto.getTax(), orderDto.getTip(), orderDto.getStatus(), orderDto.getDate() );
        return o;
    }

}
