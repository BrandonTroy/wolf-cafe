package edu.ncsu.csc326.wolfcafe.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import edu.ncsu.csc326.wolfcafe.dto.InventoryDto;
import edu.ncsu.csc326.wolfcafe.dto.ItemDto;
import edu.ncsu.csc326.wolfcafe.service.InventoryService;
import edu.ncsu.csc326.wolfcafe.service.ItemService;
import edu.ncsu.csc326.wolfcafe.service.OrderService;
import lombok.AllArgsConstructor;

/**
 * MakeItemController provides the endpoint for ordering a item.
 */
@RestController
@AllArgsConstructor
@RequestMapping ("/api/order")
@CrossOrigin("*")
public class OrderController {

    /** Connection to InventoryService */
    private final InventoryService inventoryService;

    /** Connection to ItemService */
    private final ItemService itemService;

    /** Connection to OrderService */
    private final OrderService orderService;

    // TODO: both of these methods will need to changed significantly, coffee maker implementation is a starting point

    /**
     * REST API method to make coffee by completing a POST request with the ID
     * of the item as the path variable and the amount that has been paid as
     * the body of the response
     *
     * @param id item id
     * @param amtPaid amount paid
     * @return The change the customer is due if successful
     */
    @PostMapping ("{id}")
    public ResponseEntity<Double> order( @PathVariable ( "id" ) final Long id,
            @RequestBody final Double amtPaid ) {
        final ItemDto itemDto = itemService.getItem(id);

        final double change = makeItem(itemDto, amtPaid);
        if (change == amtPaid) {
            if (amtPaid < itemDto.getPrice()) {
                return new ResponseEntity<>( amtPaid, HttpStatus.CONFLICT );
            }
            else {
                return new ResponseEntity<>( amtPaid, HttpStatus.BAD_REQUEST );
            }
        }
        return ResponseEntity.ok( change );
    }

    /**
     * Helper method to make coffee
     *
     * @param toPurchase item that we want to make
     * @param amtPaid money that the user has given the machine
     * @return change if there was enough money to buy the item, throws
     *         exceptions if not
     */
    private double makeItem ( final ItemDto toPurchase, final double amtPaid ) {
        double change = amtPaid;
        final InventoryDto inventoryDto = inventoryService.getInventory();

        if (toPurchase.getPrice() <= amtPaid) {
            if ( orderService.order( inventoryDto, toPurchase ) ) {
                change = amtPaid - toPurchase.getPrice();
                return change;
            }
            else {
                // not enough inventory
                return change;
            }
        }
        else {
            // not enough money
            return change;
        }

    }

}
