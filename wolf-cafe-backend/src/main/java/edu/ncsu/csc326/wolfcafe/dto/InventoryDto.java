package edu.ncsu.csc326.wolfcafe.dto;

import java.util.HashMap;
import java.util.Map;

import lombok.Data;

/**
 * An object storing a map of item ids to their quantities in the inventory (or
 * the quantity that should be added to the inventory)
 */
@Data
public class InventoryDto {
    
    /** The map of items and counts for each item, mapping item id to count of item */
    private Map<Long, Integer> itemQuantities = new HashMap<>();
}
