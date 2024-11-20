package edu.ncsu.csc326.wolfcafe.controller;

import java.security.NoSuchAlgorithmException;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import edu.ncsu.csc326.wolfcafe.dto.OrderDto;
import edu.ncsu.csc326.wolfcafe.dto.UserDto;
import edu.ncsu.csc326.wolfcafe.entity.Role;
import edu.ncsu.csc326.wolfcafe.entity.Status;
import edu.ncsu.csc326.wolfcafe.service.OrderService;
import edu.ncsu.csc326.wolfcafe.service.UserService;
import lombok.AllArgsConstructor;

/**
 * MakeItemController provides the endpoint for ordering a item.
 */
@RestController
@AllArgsConstructor
@RequestMapping ( "/api/order" )
@CrossOrigin ( "*" )
public class OrderController {

	/** Connection to OrderService */
	@Autowired
    private OrderService orderService;
	
	/** Connection to UserService */
	@Autowired
	private UserService userService;
    
//    public ResponseEntity<UserDto> createUser(@RequestBody final UserDto userDto,
//            @AuthenticationPrincipal UserDetails requester) throws NoSuchAlgorithmException {
//        
//        Role role = userService.getUserByUsername(requester.getUsername()).getRole();
//
//        if (role == Role.BARISTA || role == Role.MANAGER) {
//            System.out.println("Action for staff");
//        } else {
//            System.out.println("Action for customers");
//        }
//
//        return null;
//    }

    /**
     * REST API method to create a new order
     *
     * @param orderDto
     * @return
     */
    @PreAuthorize ( "hasRole('CUSTOMER')" )
    @PostMapping
    public ResponseEntity<OrderDto> addOrder ( @RequestBody OrderDto orderDto ) {
//        if ( orderDto.getId() == null ) {
//            return ResponseEntity.status( HttpStatus.CONFLICT ).build();
//        }
        OrderDto addedOrder = orderService.addOrder(orderDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(addedOrder);
    }

    /**
     * REST API method to return a map of all the orders.
     *
     * @return
     */
    @GetMapping
    public ResponseEntity<Map<Long, OrderDto>> getOrders (@AuthenticationPrincipal UserDetails requester) 
    		throws NoSuchAlgorithmException {
    	
    	Long id = userService.getUserByUsername(requester.getUsername()).getId();
    	
        Map<Long, OrderDto> orders = orderService.getOrderHistory(id);
        return ResponseEntity.ok( orders );
    }

    /**
     * REST API method to update the status of the order, either from placed to fulfilled, 
     * or fulfilled to picked up. Only managers and baristas can change to fulfilled, and only 
     * customers can change from fulfilled to picked up
     * @param requester the user requesting this call
     * @param id the id of the order to update
     * @param orderDto the order to update
     * @return the updated order or a bad request if admin tries calling it
     */
    @PutMapping ( "{id}" )
    public ResponseEntity<OrderDto> updateOrder (@AuthenticationPrincipal UserDetails requester,
    		@PathVariable ( "id" ) Long id, @RequestBody OrderDto orderDto ) {
    	
    	Role role = userService.getUserByUsername(requester.getUsername()).getRole();
    	
    	if (role == Role.BARISTA || role == Role.MANAGER) {
    		OrderDto updatedOrderDto = orderService.editOrder(orderDto, Status.FULFILLED);
    		return ResponseEntity.ok(updatedOrderDto);
    	}
    	else if (role == Role.CUSTOMER || role == Role.GUEST) {
    		OrderDto updatedOrderDto = orderService.editOrder(orderDto, Status.PICKEDUP);
    		return ResponseEntity.ok(updatedOrderDto);
    	}
    	else {
    		return ResponseEntity.status( HttpStatus.BAD_REQUEST ).body( orderDto );
    	}
    }
}
