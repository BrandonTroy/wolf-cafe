package edu.ncsu.csc326.wolfcafe.controller;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;
import java.util.Map.Entry;

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
import edu.ncsu.csc326.wolfcafe.entity.Role;
import edu.ncsu.csc326.wolfcafe.entity.Status;
import edu.ncsu.csc326.wolfcafe.service.ItemService;
import edu.ncsu.csc326.wolfcafe.service.OrderService;
import edu.ncsu.csc326.wolfcafe.service.TaxService;
import edu.ncsu.csc326.wolfcafe.service.UserService;
import lombok.AllArgsConstructor;

/**
 * Provides the end point for ordering, updating, or retrieving Orders.
 * 
 * @author Ryan Hinshaw
 */
@RestController
@AllArgsConstructor
@RequestMapping ( "/api/orders" )
@CrossOrigin ( "*" )
public class OrderController {

    /** Connection to OrderService */
    @Autowired
    private final OrderService orderService;

    /** Connection to ItemService */
    @Autowired
    private final ItemService  itemService;

    /** Connection to TaxService */
    @Autowired
    private final TaxService   taxService;

    /** Connection to UserService */
    @Autowired
    private final UserService  userService;

    /**
     * REST API method to create and place a new order
     *
     * @param orderDto
     *            dto containing the list of items in the order and the tip
     * @param requester
     *            info about the user requesting this call
     * @return OrderDto representing the created order
     */
    @PreAuthorize ( "hasAnyRole('CUSTOMER', 'GUEST')" )
    @PostMapping
    public ResponseEntity<OrderDto> addOrder ( @AuthenticationPrincipal final UserDetails requester,
            @RequestBody final OrderDto orderDto ) {
        final Long userId = userService.getUserByUsername( requester.getUsername() ).getId();
        double price = 0;
        for ( final Entry<Long, Integer> item : orderDto.getItemList().entrySet() ) {
            price += itemService.getItem( item.getKey() ).getPrice() * item.getValue();
        }
        final double tax = price * taxService.getTaxRate() / 100.0;
        final String date = new SimpleDateFormat( "yyyy-MM-dd HH:mm:ss" ).format( new Date() );
        final OrderDto completeOrder = new OrderDto( null, orderDto.getItemList(), userId, price, tax,
                orderDto.getTip(), Status.PLACED, date );
        OrderDto addedOrder;
        try {
            addedOrder = orderService.addOrder( completeOrder );
        }
        catch ( final IllegalArgumentException e ) {
            return ResponseEntity.status( HttpStatus.CONFLICT ).build(); // Insufficient
                                                                         // inventory
        }
        return ResponseEntity.status( HttpStatus.CREATED ).body( addedOrder );
    }

    /**
     * REST API method to return a map of all the orders.
     *
     * @param requester
     *            info about the user requesting this call
     * @return map of all orders placed by the requesting user, or all orders if
     *         the requesting user is a staff
     */
    @GetMapping
    public ResponseEntity<Map<Long, OrderDto>> getOrders ( @AuthenticationPrincipal final UserDetails requester ) {

        final String username = userService.getUserByUsername( requester.getUsername() ).getUsername();

        final Map<Long, OrderDto> orders = orderService.getOrderHistory( username );
        return ResponseEntity.ok( orders );
    }

    /**
     * REST API method to update the status of the order, either from placed to
     * fulfilled, or fulfilled to picked up. Only managers and baristas can
     * change to fulfilled, and only customers can change from fulfilled to
     * picked up
     *
     * @param requester
     *            the user requesting this call
     * @param id
     *            the id of the order to update
     * @param orderDto
     *            the order to update
     * @return the updated order or a bad request if admin tries calling it
     */
    @PutMapping ( "{id}" )
    public ResponseEntity<OrderDto> updateOrder ( @AuthenticationPrincipal final UserDetails requester,
            @PathVariable ( "id" ) final Long id, @RequestBody final OrderDto orderDto ) {

        final Role role = userService.getUserByUsername(requester.getUsername()).getRole();

        if ( orderDto.getStatus() == Status.CANCELED ) {
            final OrderDto updatedOrderDto = orderService.editOrder( orderDto, Status.CANCELED );
            return ResponseEntity.ok( updatedOrderDto );
        }
        else if ( role == Role.BARISTA || role == Role.MANAGER ) {
            final OrderDto updatedOrderDto = orderService.editOrder( orderDto, Status.FULFILLED );
            return ResponseEntity.ok( updatedOrderDto );
        }
        else if ( role == Role.CUSTOMER || role == Role.GUEST ) {
            final OrderDto updatedOrderDto = orderService.editOrder( orderDto, Status.PICKEDUP );
            return ResponseEntity.ok( updatedOrderDto );
        }
        else {
            return ResponseEntity.status( HttpStatus.BAD_REQUEST ).body( orderDto );
        }
    }
}
