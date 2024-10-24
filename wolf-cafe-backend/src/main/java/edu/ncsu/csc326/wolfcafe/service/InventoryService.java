package edu.ncsu.csc326.wolfcafe.service;

import edu.ncsu.csc326.wolfcafe.dto.InventoryDto;

/**
 * Interface defining the inventory behaviors.
 */
public interface InventoryService {
    /**
     * Returns the single inventory.
     *
     * @return the single inventory
     */
    InventoryDto getInventory();

    /**
     * Updates the inventory by adding the given quantities
     *
     * @param inventoryDto values to add to the inventory
     * @return updated inventory
     */
    InventoryDto addInventory(InventoryDto inventoryDto);

}
