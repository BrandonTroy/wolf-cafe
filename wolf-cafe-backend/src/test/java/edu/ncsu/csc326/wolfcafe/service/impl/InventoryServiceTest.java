package edu.ncsu.csc326.wolfcafe.service.impl;

import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.transaction.annotation.Transactional;

import edu.ncsu.csc326.wolfcafe.dto.InventoryDto;
import edu.ncsu.csc326.wolfcafe.dto.ItemDto;
import edu.ncsu.csc326.wolfcafe.repository.InventoryRepository;
import edu.ncsu.csc326.wolfcafe.repository.ItemRepository;
import edu.ncsu.csc326.wolfcafe.service.InventoryService;
import edu.ncsu.csc326.wolfcafe.service.ItemService;

@SpringBootTest
class InventoryServiceTest {

    /**
     * Connection to the table of items to delete all items
     */
    @Autowired
    private ItemRepository      itemRepository;

    /**
     * Connection to the ItemService to add items that should appear in the
     * inventory
     */
    @Autowired
    private ItemService         itemService;

    /**
     * Connection to the inventory table to delete all items
     */
    @Autowired
    private InventoryRepository inventoryRepository;

    /**
     * Connection to inventory service for manipulating the Inventory model.
     */
    @Autowired
    private InventoryService    inventoryService;

    /**
     * Tests the PUT /api/inventory endpoint.
     *
     * @throws Exception
     *             if issue when running the test.
     */
    @Test
    @Transactional
    @WithMockUser ( username = "barista", roles = "BARISTA" )
    public void testAddInventoryErrors () throws Exception {
        itemRepository.deleteAll();
        // inventoryRepository.deleteAll();
        final InventoryDto expectedInventory = new InventoryDto();
        final InventoryDto nullInventory = null;

        assertThrows( IllegalArgumentException.class, () -> inventoryService.addInventory( nullInventory ) );

        final ItemDto newItem = itemService.addItem( new ItemDto( 0L, "Water", "H2O", 3.50 ) );
        final ItemDto newItem2 = itemService.addItem( new ItemDto( 0L, "Coffee", "Brown liquid", 5.50 ) );
        expectedInventory.getItemQuantities().put( newItem.getId(), 5 );
        expectedInventory.getItemQuantities().put( newItem2.getId(), 3 );

        final InventoryDto updates = new InventoryDto();

        final ItemDto newItem3 = itemService.addItem( new ItemDto( 0L, "Error", "Negative amount", 7.50 ) );
        updates.getItemQuantities().put( newItem3.getId(), -2 );

        assertThrows( IllegalArgumentException.class, () -> inventoryService.addInventory( updates ) );
    }

}
