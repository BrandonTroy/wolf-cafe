package edu.ncsu.csc326.wolfcafe.service.impl;

import java.util.HashMap;
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
 */
@Service
public class OrderServiceImpl implements OrderService {

    @Autowired
    private InventoryService          inventoryService;

    @Autowired
    private OrderRepository           orderRepository;

    @Autowired
    private UserRepository            userRepository;

    @Autowired
    private InventoryRepository       inventoryRepository;

    private final Map<Long, OrderDto> orderHistory = new HashMap<>();

    @Override
    public OrderDto addOrder ( final OrderDto orderDto ) {
        if ( !enoughIngredients( orderDto ) ) {
            throw new IllegalArgumentException( "Not enough inventory." );
        }
        orderHistory.put( orderDto.getId(), orderDto );
        final Order order = OrderMapper.mapToOrder( orderDto );
        final Order savedOrder = orderRepository.save( order );
        return OrderMapper.mapToOrderDto( savedOrder );
    }

    @Override
    public OrderDto editOrder ( final OrderDto orderDto, final Status status ) {
        if ( orderDto.getStatus() == Status.PLACED && status == Status.FULFILLED && enoughIngredients( orderDto ) ) {
            fulfillOrder( orderDto );
            orderDto.setStatus( status );
            orderHistory.get( orderDto.getId() ).setStatus( status );
        }
        else if ( orderDto.getStatus() == Status.FULFILLED && status == Status.PICKEDUP ) {
            orderDto.setStatus( status );
            orderHistory.get( orderDto.getId() ).setStatus( status );
        }
        final Order updatedOrder = OrderMapper.mapToOrder( orderDto );
        final Order savedOrder = orderRepository.save( updatedOrder );
        return OrderMapper.mapToOrderDto( savedOrder );
    }

    @Override
    public Map<Long, OrderDto> getOrderHistory ( final Long id ) {
        final Optional<User> user = userRepository.findById( id );
        final UserDto userDto = UserMapper.mapToUserDto( user.get() );
        if ( userDto.getRole() == Role.CUSTOMER || userDto.getRole() == Role.GUEST ) {
            final Map<Long, OrderDto> orders = new HashMap<>();
            for ( final Entry<Long, OrderDto> orderDto : orderHistory.entrySet() ) {
                if ( orderDto.getValue().getCustomerId().equals( id ) ) {
                    orders.put( orderDto.getKey(), orderDto.getValue() );
                }
            }
            return orders;
        }
        else {
            return orderHistory;
        }
    }

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

    private void fulfillOrder ( final OrderDto orderDto ) {
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
