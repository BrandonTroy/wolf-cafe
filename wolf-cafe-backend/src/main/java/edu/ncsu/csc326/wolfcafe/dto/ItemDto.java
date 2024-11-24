package edu.ncsu.csc326.wolfcafe.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Item for data transfer.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ItemDto {
    
    /** The unique id of the item */
    private Long id;
    
    /** The name of the item */
    private String name;
    
    /** The description of the item */
    private String description;
    
    /** The price of one of these Items */
    private double price;
}
