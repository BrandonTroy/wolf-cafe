package edu.ncsu.csc326.wolfcafe.mapper;

import edu.ncsu.csc326.wolfcafe.dto.OrderDto;
import edu.ncsu.csc326.wolfcafe.entity.Order;

/**
 * Converts between Orders and OrderDtos
 * 
 * @author Ryan Hinshaw (rthinsha)
 */
public class OrderMapper {

    /**
     * Converts an Order entity to an OrderDto
     *
     * @param order Order to convert
     * @return newly created OrderDto
     */
    public static OrderDto mapToOrderDto ( final Order order ) {
        final OrderDto o = new OrderDto( order.getId(), order.getItemList(), order.getCustomerId(), order.getPrice(),
                order.getTax(), order.getTip(), order.getStatus(), order.getDate() );
        return o;
    }

    /**
     * Converts an OrderDto entity to an Order
     *
     * @param orderDto OrderDto to convert
     * @return newly created Order
     */
    public static Order mapToOrder ( final OrderDto orderDto ) {
        final Order o = new Order( orderDto.getId(), orderDto.getItemList(), orderDto.getCustomerId(),
                orderDto.getPrice(), orderDto.getTax(), orderDto.getTip(), orderDto.getStatus(), orderDto.getDate() );
        return o;
    }
}
