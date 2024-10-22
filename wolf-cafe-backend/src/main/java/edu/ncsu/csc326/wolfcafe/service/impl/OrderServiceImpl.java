package edu.ncsu.csc326.wolfcafe.service.impl;

import org.springframework.stereotype.Service;

import edu.ncsu.csc326.wolfcafe.dto.InventoryDto;
import edu.ncsu.csc326.wolfcafe.dto.ItemDto;
import edu.ncsu.csc326.wolfcafe.service.OrderService;
import lombok.AllArgsConstructor;

/**
 * Interface defining the ordering behaviors.
 */
@Service
@AllArgsConstructor
public class OrderServiceImpl implements OrderService {
    
	/**
     * Decreases the inventory of the specified item. Assumes that the user has
     * checked that the amount paid is sufficient
     *
     * @param inventoryDto current inventory
     * @param itemDto item to buy
     * @return updated inventory 
     */
    public boolean order(InventoryDto inventoryDto, ItemDto itemDto) {
        // TODO: implement
        
        return true;   
    }

}
