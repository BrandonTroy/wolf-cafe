package edu.ncsu.csc326.wolfcafe.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import edu.ncsu.csc326.wolfcafe.dto.InventoryDto;
import edu.ncsu.csc326.wolfcafe.dto.OrderDto;
import edu.ncsu.csc326.wolfcafe.dto.UserDto;
import edu.ncsu.csc326.wolfcafe.entity.InventoryEntry;
import edu.ncsu.csc326.wolfcafe.entity.Order;
import edu.ncsu.csc326.wolfcafe.entity.Role;
import edu.ncsu.csc326.wolfcafe.entity.Status;
import edu.ncsu.csc326.wolfcafe.entity.User;
import edu.ncsu.csc326.wolfcafe.mapper.OrderMapper;
import edu.ncsu.csc326.wolfcafe.mapper.UserMapper;
import edu.ncsu.csc326.wolfcafe.repository.InventoryRepository;
import edu.ncsu.csc326.wolfcafe.repository.OrderRepository;
import edu.ncsu.csc326.wolfcafe.repository.UserRepository;
import edu.ncsu.csc326.wolfcafe.service.InventoryService;
import edu.ncsu.csc326.wolfcafe.service.OrderService;

/**
 * Interface defining the ordering behaviors.
 * 
 * @author Ryan Hinshaw (rthinsha)
 */
@Service
public class OrderServiceImpl implements OrderService {

	/** Instance of InventoryService to get check if there's enough items */
    @Autowired
    private InventoryService          inventoryService;

    /** 
     * Connection to the repository of orders so that they can be placed, updated, 
     * or retrieved 
     */
    @Autowired
    private OrderRepository           orderRepository;

    /** Connection to repository of users to check for usernames and roles */
    @Autowired
    private UserRepository            userRepository;

    /** Connection to repository of inventory entries to update the counts of each item */
    @Autowired
    private InventoryRepository       inventoryRepository;

    /**
	 * Adds an Order to the database of Orders, so long as the Inventory has enough Items 
	 * to possibly fulfill the Order. 
	 * @param orderDto The Order to be added to the database of Orders and subtract from 
	 * the Inventory
	 * @return The orderDto if successfully added, and throws an IllegalArgumentException if not
	 */
    @Override
    public OrderDto addOrder ( final OrderDto orderDto ) {
        if ( !enoughIngredients( orderDto ) ) {
            throw new IllegalArgumentException( "Not enough inventory." );
        }
        if (orderDto.getStatus() != Status.PLACED) {
        	throw new IllegalArgumentException( "Placing an order must have status of placed" );
        }
        List<Order> orders = orderRepository.findAll();
        Order order = OrderMapper.mapToOrder( orderDto );
        Order savedOrder = orderRepository.save( order );
        orders.add(order);
        placeOrder(orderDto);
        return OrderMapper.mapToOrderDto( savedOrder );
    }

    /**
	 * Updates the OrderDto passed in as a parameter and in the database to the status passed in. 
	 * Used when staff members fulfill and order and customers pickup or cancel an order.
	 * @param orderDto The Order to be updated
	 * @param status The status to update the Order to
	 * @return The updated Order
	 */
    @Override
    public OrderDto editOrder ( final OrderDto orderDto, final Status status ) {
        if ( orderDto.getStatus() == Status.PLACED && status == Status.FULFILLED) {
            orderDto.setStatus( status );
            Order order = OrderMapper.mapToOrder(orderDto);
            order.setStatus(status);
            orderRepository.save(order);
        }
        else if ( orderDto.getStatus() == Status.FULFILLED && status == Status.PICKEDUP ) {
        	orderDto.setStatus( status );
        	Order order = OrderMapper.mapToOrder(orderDto);
            order.setStatus(status);
            orderRepository.save(order);
        }
        final Order updatedOrder = OrderMapper.mapToOrder( orderDto );
        final Order savedOrder = orderRepository.save( updatedOrder );
        return OrderMapper.mapToOrderDto( savedOrder );
    }

    /**
	 * Returns the history of Orders in the database, but is dependent on the user who calls 
	 * this method. Staff members can see every Order in the system, while customers (and guests) 
	 * can only see their orders. Since usernames are unique, they're verified against the 
	 * user repository
	 * @param username The username of the user calling this method 
	 * @return The Order History appropriate to the role of the caller
	 */
    @Override
    public Map<Long, OrderDto> getOrderHistory ( final String username ) {
        final Optional<User> user = userRepository.findByUsername( username );
        final UserDto userDto = UserMapper.mapToUserDto( user.get() );
        List<Order> orderList = orderRepository.findAll();
        Map<Long, OrderDto> orderHistory = new HashMap<>();
        for (int i = 0; i < orderList.size(); i++) {
        	OrderDto orderDtoAdd = OrderMapper.mapToOrderDto(orderList.get(i));
        	orderHistory.put(orderDtoAdd.getId(), orderDtoAdd);
        }
        if ( userDto.getRole() == Role.CUSTOMER || userDto.getRole() == Role.GUEST ) {
            final Map<Long, OrderDto> orders = new HashMap<>();
            for ( final Entry<Long, OrderDto> orderDto : orderHistory.entrySet() ) {
                if ( orderDto.getValue().getCustomerId().equals( userDto.getId() ) ) {
                    orders.put( orderDto.getKey(), orderDto.getValue() );
                }
            }
            return orders;
        }
        else {
            return orderHistory;
        }
    }

    /**
     * Private helper method to determine if the Inventory has enough of the necessary 
     * Items to place the passed in Order
     * @param orderDto The Order to check if there's enough items to place
     * @return True if enough, false otherwise
     */
    private boolean enoughIngredients ( final OrderDto orderDto ) {
        final InventoryDto inventoryDto = inventoryService.getInventory();
        final Map<Long, Integer> itemQuantities = inventoryDto.getItemQuantities();
        for ( final Entry<Long, Integer> item : orderDto.getItemList().entrySet() ) {
            if ( item.getValue() > itemQuantities.get( item.getKey() ) ) {
                return false;
            }
        }
        return true;
    }

    /**
     * Private method that "places" an Order by removing the necessary counts from the Inventory
     * @param orderDto The Order to place and update the Inventory
     */
    private void placeOrder ( final OrderDto orderDto ) {
        final InventoryDto inventoryDto = inventoryService.getInventory();
        final Map<Long, Integer> itemQuantities = inventoryDto.getItemQuantities();
        InventoryEntry entry;

        for ( final Entry<Long, Integer> item : orderDto.getItemList().entrySet() ) {
            entry = inventoryRepository.findByItemId( item.getKey() ).get();
            entry.setQuantity( itemQuantities.get( item.getKey() ) - item.getValue() );
            inventoryRepository.save( entry );
        }
    }
}
