package edu.ncsu.csc326.wolfcafe.dto;

import java.util.Map;

import edu.ncsu.csc326.wolfcafe.entity.Status;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * The Data Transfer Object for Order's in the system, database, and front end
 * 
 * @author Ryan Hinshaw (rthinsha)
 */
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class OrderDto {
	
	/** The unique id of the OrderDto */
    private Long               id;
    
    /** The list of items in the OrderDto mapping item ids to a count of that item */
    private Map<Long, Integer> itemList;
    
    /** The customer id of the customer who placed the Order */
    private Long               customerId;
    
    /** The price of the Order */
    private double             price;
    
    /** The tax of the Order */
    private double             tax;
    
    /** The tip of the Order */
    private double             tip;
    
    /** The status of the Order, starts as placed, then fulfilled, then picked up, but can 
     * be cancelled at any time */
    private Status             status;
    
    /** The time and date of the Order */
    private String             date;

    /**
     * Constructor for making an orderDto that looks like the ones sent by the
     * frontend when an order is placed (other fields are filled in by
     * OrderController during the POST call)
     *
     * @param itemList
     *            list of all items in the order; used to calculate price and
     *            tax
     * @param tip
     *            tip in dollars specified by the customer
     */
    public OrderDto ( final Map<Long, Integer> itemList, final double tip ) {
        this.itemList = itemList;
        this.tip = tip;
    }

    /**
     * Changing the status of the Order, from placed to fulfilled, fulfilled to picked up, 
     * or any of them to cancelled.
     * @param status the status to change the Order to
     */
    public void setStatus ( final Status status ) {
        this.status = status;
    }
}
