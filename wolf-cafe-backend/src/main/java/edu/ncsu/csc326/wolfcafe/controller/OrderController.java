//package edu.ncsu.csc326.wolfcafe.controller;
//
//import java.util.List;
//
//import org.springframework.http.HttpStatus;
//import org.springframework.http.ResponseEntity;
//import org.springframework.security.access.prepost.PreAuthorize;
//import org.springframework.web.bind.annotation.CrossOrigin;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.PathVariable;
//import org.springframework.web.bind.annotation.PostMapping;
//import org.springframework.web.bind.annotation.PutMapping;
//import org.springframework.web.bind.annotation.RequestBody;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RestController;
//
//import edu.ncsu.csc326.wolfcafe.dto.OrderDto;
//import edu.ncsu.csc326.wolfcafe.service.OrderService;
//import lombok.AllArgsConstructor;
//
///**
// * MakeItemController provides the endpoint for ordering a item.
// */
//@RestController
//@AllArgsConstructor
//@RequestMapping ( "/api/order" )
//@CrossOrigin ( "*" )
//public class OrderController {
//
//    /** Connection to OrderService */
//    private final OrderService orderService;
//
//    /**
//     * REST API method to create a new order
//     *
//     * @param orderDto
//     * @return
//     */
//    @PreAuthorize ( "hasRole('CUSTOMER')" )
//    @PostMapping
//    public ResponseEntity<OrderDto> addOrder ( @RequestBody OrderDto orderDto ) {
//        if ( orderDto.getId() != null ) {
//            return ResponseEntity.status( HttpStatus.CONFLICT ).build();
//        }
//        OrderDto addedOrder = orderService.addOrder(orderDto);
//        return ResponseEntity.ok( addedOrder );
//    }
//
////    /**
////     * REST API method to return a list of all the orders.
////     *
////     * @return
////     */
////    @PreAuthorize ( "hasRole('CUSTOMER')" )
////    @GetMapping
////    public ResponseEntity<List<OrderDto>> getOrders () {
////        List<OrderDto> orders = orderService.getOrders();
////        return ResponseEntity.ok( orders );
////    }
////
////    @PreAuthorize ( "hasRole('CUSTOMER')" )
////    @PutMapping ( "{id}" )
////    public ResponseEntity<OrderDto> editOrder ( @PathVariable ( "id" ) final Long id, @RequestBody OrderDto orderDto ) {
////        final OrderDto updatedOrderDto = orderService.updateOrder( id, orderDto );
////        return ResponseEntity.ok( updatedOrderDto );
////    }
////
////    @PreAuthorize ( "hasRole('CUSTOMER')" )
////    @GetMapping ( "{id}" )
////    public ResponseEntity<OrderDto> getOrder ( @PathVariable ( "id" ) final Long id ) {
////        final OrderDto returnOrder = orderService.getOrder( id );
////        if ( returnOrder == null ) {
////            return ResponseEntity.status( HttpStatus.NOT_FOUND ).build();
////        }
////        return ResponseEntity.ok( returnOrder );
////    }
////
////    @PreAuthorize ( "hasRole('CUSTOMER')" )
////    @PutMapping ( "{id}/tip" )
////    public ResponseEntity<OrderDto> addTip ( int tip, @PathVariable ( "id" ) final Long id ) {
////        if ( tip < 0 ) {
////            return ResponseEntity.status( HttpStatus.BAD_REQUEST ).build();
////        }
////        final OrderDto orderDto = orderService.getOrder( id );
////        final OrderDto returnOrder = orderService.addTip( tip, orderDto );
////
////        return ResponseEntity.ok( returnOrder );
////    }
//
//}