package edu.ncsu.csc326.wolfcafe.dto;

import java.util.Map;

import edu.ncsu.csc326.wolfcafe.entity.Status;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class OrderDto {
    private Long               id;
    private Map<Long, Integer> itemList;
    private Long               customerId;
    private double             price;
    private double             tax;
    private double             tip;
    private Status             status;
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

    public void setStatus ( final Status status ) {
        this.status = status;
    }
}
