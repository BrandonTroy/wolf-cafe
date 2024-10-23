/**
 *
 */
package edu.ncsu.csc326.wolfcafe.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import edu.ncsu.csc326.wolfcafe.TestUtils;
import edu.ncsu.csc326.wolfcafe.dto.InventoryDto;
import edu.ncsu.csc326.wolfcafe.dto.ItemDto;
import edu.ncsu.csc326.wolfcafe.repository.InventoryRepository;
import edu.ncsu.csc326.wolfcafe.repository.ItemRepository;
import edu.ncsu.csc326.wolfcafe.service.ItemService;

/**
 * Tests InventoryController class
 */
@SpringBootTest
@AutoConfigureMockMvc
public class InventoryControllerTest {

    /** Mock MVC for testing controller */
    @Autowired
    private MockMvc             mvc;

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
     * Tests the GET /api/inventory endpoint.
     *
     * @throws Exception
     *             if issue when running the test.
     */
    @Test
    @Transactional
    @WithMockUser ( username = "joe schmoe" )
    public void testGetInventory () throws Exception {
        itemRepository.deleteAll();
        inventoryRepository.deleteAll();
        final InventoryDto expectedInventory = new InventoryDto();

        mvc.perform( get( "/api/inventory" ) )
                .andExpect( content().string( TestUtils.asJsonString( expectedInventory ) ) )
                .andExpect( status().isOk() );

        final ItemDto newItem = itemService.addItem( new ItemDto( 0L, "Water", "H2O", 3.50 ) );
        expectedInventory.getItemQuantities().put( newItem.getId(), 0 );

        mvc.perform( get( "/api/inventory" ) )
                .andExpect( content().string( TestUtils.asJsonString( expectedInventory ) ) )
                .andExpect( status().isOk() );
    }

    /**
     * Tests the PUT /api/inventory endpoint.
     *
     * @throws Exception
     *             if issue when running the test.
     */
    @Test
    @Transactional
    @WithMockUser ( username = "staff", roles = "STAFF" )
    public void testUpdateInventory () throws Exception {
        itemRepository.deleteAll();
        inventoryRepository.deleteAll();
        final InventoryDto expectedInventory = new InventoryDto();

        final ItemDto newItem = itemService.addItem( new ItemDto( 0L, "Water", "H2O", 3.50 ) );
        final ItemDto newItem2 = itemService.addItem( new ItemDto( 0L, "Coffee", "Brown liquid", 5.50 ) );
        expectedInventory.getItemQuantities().put( newItem.getId(), 5 );
        expectedInventory.getItemQuantities().put( newItem2.getId(), 3 );

        final InventoryDto updates = new InventoryDto();
        updates.getItemQuantities().put( newItem2.getId(), 3 );
        updates.getItemQuantities().put( newItem.getId(), 5 );
        mvc.perform( put( "/api/inventory" ).contentType( MediaType.APPLICATION_JSON )
                .content( TestUtils.asJsonString( updates ) ).accept( MediaType.APPLICATION_JSON ) )
                .andExpect( status().isOk() )
                .andExpect( content().string( TestUtils.asJsonString( expectedInventory ) ) );

        updates.getItemQuantities().put( newItem2.getId() + 1L, 3 );
        mvc.perform( put( "/api/inventory" ).contentType( MediaType.APPLICATION_JSON )
                .content( TestUtils.asJsonString( updates ) ).accept( MediaType.APPLICATION_JSON ) )
                .andExpect( status().isBadRequest() );
    }

    /**
     * Tests updating the inventory if the item exists but the inventory entry
     * got deleted
     *
     * @throws Exception
     *             if issue when running the test.
     */
    @Test
    @Transactional
    @WithMockUser ( username = "staff", roles = "STAFF" )
    public void testUpdateInventoryNoEntry () throws Exception {
        itemRepository.deleteAll();
        inventoryRepository.deleteAll();
        final InventoryDto expectedInventory = new InventoryDto();

        final ItemDto newItem = itemService.addItem( new ItemDto( 0L, "Water", "H2O", 3.50 ) );
        final ItemDto newItem2 = itemService.addItem( new ItemDto( 0L, "Coffee", "Brown liquid", 5.50 ) );
        expectedInventory.getItemQuantities().put( newItem.getId(), 5 );
        expectedInventory.getItemQuantities().put( newItem2.getId(), 3 );

        inventoryRepository.deleteAll();

        final InventoryDto updates = new InventoryDto();
        updates.getItemQuantities().put( newItem2.getId(), 3 );
        updates.getItemQuantities().put( newItem.getId(), 5 );
        mvc.perform( put( "/api/inventory" ).contentType( MediaType.APPLICATION_JSON )
                .content( TestUtils.asJsonString( updates ) ).accept( MediaType.APPLICATION_JSON ) )
                .andExpect( status().isOk() )
                .andExpect( content().string( TestUtils.asJsonString( expectedInventory ) ) );

        updates.getItemQuantities().put( newItem2.getId() + 1L, 3 );
        mvc.perform( put( "/api/inventory" ).contentType( MediaType.APPLICATION_JSON )
                .content( TestUtils.asJsonString( updates ) ).accept( MediaType.APPLICATION_JSON ) )
                .andExpect( status().isBadRequest() );
    }

}
