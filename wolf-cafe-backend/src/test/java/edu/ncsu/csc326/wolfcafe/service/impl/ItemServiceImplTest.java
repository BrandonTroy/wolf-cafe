package edu.ncsu.csc326.wolfcafe.service.impl;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.transaction.annotation.Transactional;

import edu.ncsu.csc326.wolfcafe.dto.ItemDto;
import edu.ncsu.csc326.wolfcafe.repository.ItemRepository;
import edu.ncsu.csc326.wolfcafe.service.ItemService;

/**
 * Tests for user service implementation
 *
 * @author Narim Lee
 */
@SpringBootTest
@AutoConfigureMockMvc
public class ItemServiceImplTest {

    /** item service */
    @Autowired
    private ItemService         itemService;

    /** item repository */
    @Autowired
    private ItemRepository      itemRepository;

    /**
     * Name of test item
     */
    private static final String ITEM_NAME        = "Coffee";
    /**
     * Description of test item
     */
    private static final String ITEM_DESCRIPTION = "Coffee is life";
    /**
     * Price of test item
     */
    private static final double ITEM_PRICE       = 3.25;

    /** Deletes user repository before each test */
    @BeforeEach
    public void setUp () throws Exception {
        itemRepository.deleteAll();
    }

    /**
     * Tests for adding an item
     */
    @Test
    @Transactional
    @WithMockUser ( username = "manager", roles = "MANAGER" )
    void testAddItem () {
        final ItemDto itemDto = new ItemDto( 0L, ITEM_NAME, ITEM_DESCRIPTION, ITEM_PRICE );
        final ItemDto savedItem = itemService.addItem( itemDto );
        final ItemDto retrievedItem = itemService.getItem( savedItem.getId() );

        assertAll( "User contents", () -> assertEquals( ITEM_NAME, retrievedItem.getName() ),
                () -> assertEquals( ITEM_DESCRIPTION, retrievedItem.getDescription() ),
                () -> assertEquals( ITEM_PRICE, retrievedItem.getPrice() ),
                () -> assertEquals( savedItem.getId(), retrievedItem.getId() ) );
    }

    /**
     * Tests for retrieving an item
     */
    @Test
    @Transactional
    @WithMockUser ( username = "manager", roles = "MANAGER" )
    void testGetItem () {
        final ItemDto itemDto1 = new ItemDto( 0L, ITEM_NAME, ITEM_DESCRIPTION, ITEM_PRICE );
        final ItemDto savedItem1 = itemService.addItem( itemDto1 );
        final ItemDto itemDto2 = new ItemDto( 1L, "Water", "This is water", 4.5 );
        final ItemDto savedItem2 = itemService.addItem( itemDto2 );

        final ItemDto retrievedItem1 = itemService.getItem( savedItem1.getId() );
        assertAll( "User contents", () -> assertEquals( ITEM_NAME, retrievedItem1.getName() ),
                () -> assertEquals( ITEM_DESCRIPTION, retrievedItem1.getDescription() ),
                () -> assertEquals( ITEM_PRICE, retrievedItem1.getPrice() ),
                () -> assertEquals( savedItem1.getId(), retrievedItem1.getId() ) );

        final ItemDto retrievedItem2 = itemService.getItem( savedItem2.getId() );
        assertAll( "User contents", () -> assertEquals( "Water", retrievedItem2.getName() ),
                () -> assertEquals( "This is water", retrievedItem2.getDescription() ),
                () -> assertEquals( 4.5, retrievedItem2.getPrice() ),
                () -> assertEquals( savedItem2.getId(), retrievedItem2.getId() ) );
    }

    /**
     * Tests for retrieving a list of all the items
     */
    @Test
    @Transactional
    @WithMockUser ( username = "manager", roles = "MANAGER" )
    void testGetAllItems () {
        final ItemDto itemDto1 = new ItemDto( 0L, ITEM_NAME, ITEM_DESCRIPTION, ITEM_PRICE );
        final ItemDto savedItem1 = itemService.addItem( itemDto1 );
        final ItemDto itemDto2 = new ItemDto( 1L, "Water", "This is water", 4.5 );
        final ItemDto savedItem2 = itemService.addItem( itemDto2 );
        final ItemDto itemDto3 = new ItemDto( 2L, "Matcha", "Tastes like grass", 5 );
        final ItemDto savedItem3 = itemService.addItem( itemDto3 );

        List<ItemDto> items = itemService.getAllItems();

        final ItemDto retrievedItem1 = items.get( 0 );
        final ItemDto retrievedItem2 = items.get( 1 );
        final ItemDto retrievedItem3 = items.get( 2 );

        assertAll( "User contents", () -> assertEquals( ITEM_NAME, retrievedItem1.getName() ),
                () -> assertEquals( ITEM_DESCRIPTION, retrievedItem1.getDescription() ),
                () -> assertEquals( ITEM_PRICE, retrievedItem1.getPrice() ),
                () -> assertEquals( savedItem1.getId(), retrievedItem1.getId() ) );

        assertAll( "User contents", () -> assertEquals( "Water", retrievedItem2.getName() ),
                () -> assertEquals( "This is water", retrievedItem2.getDescription() ),
                () -> assertEquals( 4.5, retrievedItem2.getPrice() ),
                () -> assertEquals( savedItem2.getId(), retrievedItem2.getId() ) );

        assertAll( "User contents", () -> assertEquals( "Matcha", retrievedItem3.getName() ),
                () -> assertEquals( "Tastes like grass", retrievedItem3.getDescription() ),
                () -> assertEquals( 5, retrievedItem3.getPrice() ),
                () -> assertEquals( savedItem3.getId(), retrievedItem3.getId() ) );
    }

    /**
     * Tests for updating an item.
     */
    @Test
    @Transactional
    @WithMockUser ( username = "manager", roles = "MANAGER" )
    void testUpdateItem () {
        final ItemDto itemDto = new ItemDto( 0L, ITEM_NAME, ITEM_DESCRIPTION, ITEM_PRICE );
        final ItemDto savedItem = itemService.addItem( itemDto );
        final ItemDto newItem = itemService.getItem( savedItem.getId() );
        newItem.setDescription( "Coffee is not life" );
        newItem.setName( "Not Coffee" );
        newItem.setPrice( 10 );

        final ItemDto updatedItem = itemService.updateItem( savedItem.getId(), newItem );

        assertAll( "User contents", () -> assertEquals( "Not Coffee", updatedItem.getName() ),
                () -> assertEquals( "Coffee is not life", updatedItem.getDescription() ),
                () -> assertEquals( 10, updatedItem.getPrice() ),
                () -> assertEquals( newItem.getId(), updatedItem.getId() ) );
    }

}
