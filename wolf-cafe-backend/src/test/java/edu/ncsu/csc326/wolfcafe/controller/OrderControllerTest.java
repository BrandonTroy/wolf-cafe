package edu.ncsu.csc326.wolfcafe.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

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
import edu.ncsu.csc326.wolfcafe.dto.OrderDto;
import edu.ncsu.csc326.wolfcafe.dto.UserDto;
import edu.ncsu.csc326.wolfcafe.entity.Role;
import edu.ncsu.csc326.wolfcafe.repository.UserRepository;
import edu.ncsu.csc326.wolfcafe.service.InventoryService;
import edu.ncsu.csc326.wolfcafe.service.ItemService;
import edu.ncsu.csc326.wolfcafe.service.TaxService;
import edu.ncsu.csc326.wolfcafe.service.UserService;

@SpringBootTest
@AutoConfigureMockMvc
class OrderControllerTest {

    /** Mock MVC for testing controller */
    @Autowired
    private MockMvc          mvc;

    /** user service for creating a user to place an order */
    @Autowired
    private UserService      userService;

    /** user repository for deleting all at start of test */
    @Autowired
    private UserRepository   userRepository;

    /** tax service for setting tax rate for tests */
    @Autowired
    private TaxService       taxService;

    /** inventory service for ensuring there are enough ingredients */
    @Autowired
    private InventoryService inventoryService;

    /** item service for adding items */
    @Autowired
    private ItemService      itemService;

    // /**
    // * Api endpoint under test
    // */
    // private static final String API_PATH = "/api/orders";
    // /**
    // * Encoding for performing api calls
    // */
    // private static final String ENCODING = "utf-8";

    @Test
    @Transactional
    @WithMockUser ( username = "rthinsha", roles = "CUSTOMER" )
    public void testAddAndPickupOrder () throws Exception {
        userRepository.deleteAll();
        taxService.setTaxRate( 2.0 );

        final ItemDto bread = new ItemDto( 1L, "bread", "bread item", 1.50 );
        final ItemDto ham = new ItemDto( 2L, "ham", "ham item", 3.25 );
        final ItemDto savedBread = itemService.addItem( bread );
        final ItemDto savedHam = itemService.addItem( ham );

        final InventoryDto inventoryDto = new InventoryDto();
        final Map<Long, Integer> items = new HashMap<>();
        items.put( savedBread.getId(), 20 );
        items.put( savedHam.getId(), 20 );
        inventoryDto.setItemQuantities( items );
        assertEquals( 2, inventoryDto.getItemQuantities().size() );
        inventoryService.addInventory( inventoryDto );

        final UserDto ryan = new UserDto( 4L, "Ryan", "rthinsha", "rthinsha@ncsu.edu", "password", Role.CUSTOMER );
        final UserDto savedUser = userService.createUser( ryan );
        final UserDto ryanUser = userService.getUserById( savedUser.getId() );

        final UserDto baristaDto = new UserDto( 1, "Karthik Nandakumar", "knandak", "knandak@ncsu.edu", "cqhavhhv",
                Role.BARISTA );
        userService.createUser( baristaDto );

        final Date date = new Date();
        final SimpleDateFormat formatter = new SimpleDateFormat( "yyyy-MM-dd HH:mm:ss" );
        final String d = formatter.format( date );

        final Map<Long, Integer> itemList = new HashMap<>();
        itemList.put( savedBread.getId(), 2 );
        itemList.put( savedHam.getId(), 3 );

        final OrderDto orderDto = new OrderDto( itemList, 0.9 );

        mvc.perform( post( "/api/order" ).contentType( MediaType.APPLICATION_JSON )
                .content( TestUtils.asJsonString( orderDto ) ).accept( MediaType.APPLICATION_JSON ) )
                .andExpect( status().isCreated() );

        // GET for orders should return all the orders available to the user
        // with that specified id
        // GET for this customer should return the newly created order above
        final String orders = mvc.perform( get( "/api/order" ) ).andDo( print() ).andExpect( status().isOk() )
                .andReturn().getResponse().getContentAsString();
        assertTrue( orders.contains( "bread" ) );
    }

}
