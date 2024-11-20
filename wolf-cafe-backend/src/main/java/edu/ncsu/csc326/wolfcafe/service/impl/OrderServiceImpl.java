package edu.ncsu.csc326.wolfcafe.service.impl;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import edu.ncsu.csc326.wolfcafe.dto.*;
import edu.ncsu.csc326.wolfcafe.entity.*;
import edu.ncsu.csc326.wolfcafe.mapper.*;
import edu.ncsu.csc326.wolfcafe.service.InventoryService;
import edu.ncsu.csc326.wolfcafe.service.OrderService;
import edu.ncsu.csc326.wolfcafe.repository.InventoryRepository;
import edu.ncsu.csc326.wolfcafe.repository.OrderRepository;
import edu.ncsu.csc326.wolfcafe.repository.UserRepository;

/**
 * Interface defining the ordering behaviors.
 */
@Service
public class OrderServiceImpl implements OrderService {
	
	@Autowired
	private InventoryService inventoryService;
	
	@Autowired
	private OrderRepository orderRepository;
	
	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private InventoryRepository inventoryRepository;
	
	private Map<Long, OrderDto> orderHistory = new HashMap<>();

	@Override
	public OrderDto addOrder(OrderDto orderDto) {
		orderHistory.put(orderDto.getId(), orderDto);
		Order order = OrderMapper.mapToOrder( orderDto );
        Order savedOrder = orderRepository.save( order );
        return OrderMapper.mapToOrderDto( savedOrder );
	}

	@Override
	public OrderDto editOrder(OrderDto orderDto, Status status) {
		if (orderDto.getStatus() == Status.PLACED && status == Status.FULFILLED && enoughIngredients(orderDto)) {
			fulfillOrder(orderDto);
			orderDto.setStatus(status);
			orderHistory.get(orderDto.getId()).setStatus(status);
		}
		else if (orderDto.getStatus() == Status.FULFILLED && status == Status.PICKEDUP) {
			orderDto.setStatus(status);
			orderHistory.get(orderDto.getId()).setStatus(status);
		}
		Order updatedOrder = OrderMapper.mapToOrder(orderDto);
		Order savedOrder = orderRepository.save(updatedOrder);
		return OrderMapper.mapToOrderDto(savedOrder);
	}

	@Override
	public Map<Long, OrderDto> getOrderHistory(Long id) {
		Optional<User> user = userRepository.findById(id);
		UserDto userDto = UserMapper.mapToUserDto(user.get());
		if (userDto.getRole() == Role.CUSTOMER || userDto.getRole() == Role.GUEST) {
			Map<Long, OrderDto> orders = new HashMap<>();
			for (Entry<Long, OrderDto> orderDto : orderHistory.entrySet()) {
				if (orderDto.getValue().getCustomerId() == id) {
					orders.put(orderDto.getKey(), orderDto.getValue());
				}
			}
			return orders;
		}
		else {
			return orderHistory;
		}
	}
	
	private boolean enoughIngredients(OrderDto orderDto) {
		InventoryDto inventoryDto = inventoryService.getInventory();
		Map<Long, Integer> itemQuantities = inventoryDto.getItemQuantities();
		for (Entry<Long, Integer> item : orderDto.getItemList().entrySet()) {
			if (item.getValue() > itemQuantities.get(item.getKey())) {
				return false;
			}
		}
		return true;
	}
	
	private void fulfillOrder(OrderDto orderDto) {
		InventoryDto inventoryDto = inventoryService.getInventory();
		Map<Long, Integer> itemQuantities = inventoryDto.getItemQuantities();
		InventoryEntry entry;
		
		for (Entry<Long, Integer> item : orderDto.getItemList().entrySet()) {
			entry = inventoryRepository.findByItemId(item.getKey()).get();
			entry.setQuantity(itemQuantities.get(item.getKey()) - item.getValue());
			inventoryRepository.save(entry);
		}
	}
}