package edu.ncsu.csc326.wolfcafe.entity;

import java.util.Map;

import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.MapKeyColumn;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * System Order
 * 
 * @author Ryan Hinshaw (rthinsha)
 */
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Getter
@Table ( name = "orders" )
public class Order {

	/** Unique Id for the Order */
    @Id
    @GeneratedValue ( strategy = GenerationType.IDENTITY )
    private Long               id;

    /** a map of order items and amounts */
    @ElementCollection ( fetch = FetchType.EAGER )
    @CollectionTable ( name = "order_items", joinColumns = @JoinColumn ( name = "order_id" ) )
    @MapKeyColumn ( name = "item_id" )
    @Column ( name = "item_amount" )
    private Map<Long, Integer> itemList;

    /** The Customer Id for the Customer who placed this Order */
    @Column ( nullable = false )
    private Long               customerId;

    /** The price of the Order */
    @Column ( nullable = false )
    private double             price;

    /** The tax for the Order */
    @Column ( nullable = false )
    private double             tax;

    /** The tip for the Order */
    @Column
    private double             tip;

    /** The status of the Order, starts as placed, then fulfilled, then picked up, but can 
     * be cancelled at any time */
    @Column ( nullable = false )
    private Status             status;

    /** The time and date of the Order */
    @Column ( nullable = false )
    private String             date;

    /**
     * Changing the status of the Order, from placed to fulfilled, fulfilled to picked up, 
     * or any of them to cancelled.
     * @param status the status to change the Order to
     */
    public void setStatus ( final Status status ) {
        this.status = status;
    }
}
