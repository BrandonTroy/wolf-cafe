package edu.ncsu.csc326.wolfcafe.service.impl;

import java.util.List;
import java.util.Map.Entry;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import edu.ncsu.csc326.wolfcafe.dto.InventoryDto;
import edu.ncsu.csc326.wolfcafe.dto.OrderDto;
import edu.ncsu.csc326.wolfcafe.dto.ItemDto;
import edu.ncsu.csc326.wolfcafe.entity.InventoryEntry;
import edu.ncsu.csc326.wolfcafe.entity.Order;
import edu.ncsu.csc326.wolfcafe.exception.ResourceNotFoundException;
import edu.ncsu.csc326.wolfcafe.mapper.OrderMapper;
import edu.ncsu.csc326.wolfcafe.repository.InventoryRepository;
import edu.ncsu.csc326.wolfcafe.repository.ItemRepository;
import edu.ncsu.csc326.wolfcafe.repository.OrderRepository;
import edu.ncsu.csc326.wolfcafe.service.OrderService;
import lombok.AllArgsConstructor;

/**
 * Interface defining the ordering behaviors.
 */
@Service
@AllArgsConstructor
public abstract class OrderServiceImpl implements OrderService {

    /** Repository for order operations */
    private final OrderRepository orderRepository;

    /** Repository for item operations */
    private final ItemRepository  itemRepository;
    
    private final InventoryRepository inventoryRepository;

    /**
     * Mapper for wrapping order entries up into a dto
     */
//    private final OrderMapper     orderMapper;

    public OrderDto addOrder ( OrderDto orderDto ) {
        if ( orderDto == null || orderDto.getItems().size() < 1 ) {
            throw new IllegalArgumentException( "Order DTO cannot be null" );
        }

        final Order order = OrderMapper.mapToOrder( orderDto );
        final Order savedOrder = orderRepository.save( order );

        return OrderMapper.mapToOrderDto( savedOrder );
    }

//    @Override
//    public List<OrderDto> getOrders () {
//        final List<Order> orders = orderRepository.findAll();
//        return orders.stream().map( ( order ) -> OrderMapper.mapToOrderDto( orders ) ).collect( Collectors.toList() );
//    }
//
//    @Override
//    public OrderDto editOrder ( Long id, OrderDto orderDto ) {
//        final Order order = orderRepository.findById( id );
//        if ( order == null ) {
//            throw new ResourceNotFoundException( "Order not found with id " + id );
//        }
//
//        if ( !order.getCustomerId.equals( orderDto.getCustomerId() ) ) {
//            throw new IllegalArgumentException( "The order can not be edited." );
//        }
//
//        order.setItemList( orderDto.getItemsList() );
//        order.setPrice( orderDto.getPrice() );
//        order.setTip( orderDto.getTip() );
//    }
//
//    @Override
//    public OrderDto getOrder ( Long id ) {
//
//    }
//
//    @Override
//    public OrderDto addTip ( int tip, Long id ) {
//
//    }
//
//    @Override
//    public boolean submitOrder ( OrderDto orderDto ) {
//
//    }
//
//    @Override
//    private boolean enoughItems ( OrderDto orderDto ) {
//
//    }
//
//	@Override
//	public boolean order(InventoryDto inventoryDto, ItemDto itemDto) {
//		// TODO Auto-generated method stub
//		return false;
//	}
//
//	@Override
//	public org.springframework.data.domain.jaxb.SpringDataJaxb.OrderDto addOrder(
//			org.springframework.data.domain.jaxb.SpringDataJaxb.OrderDto orderDto) {
//		// TODO Auto-generated method stub
//		return null;
//	}
    
	private double calculateTotalPrice(OrderDto orderDto) {
	double total = 0.0;
	for (Entry<Long, Integer> itemEntry : orderDto.getItems().entrySet()) {
		Long id = itemEntry.getKey();
		Optional<InventoryEntry> item = inventoryRepository.findByItemId(id);
		double itemPrice = item.get().getItem().getPrice();
		total += itemPrice;
	}
	total = total + orderDto.getTip();
	return total;
}

}