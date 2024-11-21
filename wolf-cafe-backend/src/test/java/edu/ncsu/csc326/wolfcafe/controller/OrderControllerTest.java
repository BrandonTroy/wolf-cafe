package edu.ncsu.csc326.wolfcafe.controller;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.event.annotation.AfterTestClass;
import org.springframework.test.context.event.annotation.BeforeTestClass;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import org.springframework.transaction.annotation.Transactional;

import edu.ncsu.csc326.wolfcafe.TestUtils;
import edu.ncsu.csc326.wolfcafe.dto.InventoryDto;
import edu.ncsu.csc326.wolfcafe.dto.ItemDto;
import edu.ncsu.csc326.wolfcafe.dto.OrderDto;
import edu.ncsu.csc326.wolfcafe.dto.UserDto;
import edu.ncsu.csc326.wolfcafe.entity.Role;
import edu.ncsu.csc326.wolfcafe.entity.Status;
import edu.ncsu.csc326.wolfcafe.repository.OrderRepository;
import edu.ncsu.csc326.wolfcafe.repository.UserRepository;
import edu.ncsu.csc326.wolfcafe.service.InventoryService;
import edu.ncsu.csc326.wolfcafe.service.ItemService;
import edu.ncsu.csc326.wolfcafe.service.OrderService;
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
    
    @Autowired
    private OrderService orderService;
    
    @Autowired
    private OrderRepository orderRepository;
    
	/**
	 * Clears the user repository before each test
	 *
	 * @throws Exception if error
	 */
	@BeforeTestClass
	public void setUp() throws Exception {
		userRepository.deleteAll();
	}

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

        final Date date = new Date();
        final SimpleDateFormat formatter = new SimpleDateFormat( "yyyy-MM-dd HH:mm:ss" );
        final String d = formatter.format( date );

        final Map<Long, Integer> itemList = new HashMap<>();
        itemList.put( savedBread.getId(), 2 );
        itemList.put( savedHam.getId(), 3 );

        OrderDto orderDto = new OrderDto(1L, itemList, ryanUser.getId(), 12.75, 0.26, 0.9, Status.PLACED, d );
        
        mvc.perform( post( "/api/orders" ).contentType( MediaType.APPLICATION_JSON )
                .content( TestUtils.asJsonString( orderDto ) ).accept( MediaType.APPLICATION_JSON ) )
                .andExpect( status().isCreated() );

        // GET for orders should return all the orders available to the user
        // with that specified id
        // GET for this customer should return the newly created order above
        final String orders = mvc.perform( get( "/api/orders" ) ).andDo( print() ).andExpect( status().isOk() )
                .andReturn().getResponse().getContentAsString();
        assertTrue( orders.contains("" + savedBread.getId()));
        assertTrue(orders.contains("" + orderDto.getCustomerId()));
        
        assertEquals(1, orderRepository.findAll().size());
        
        orderDto.setStatus(Status.FULFILLED);
        Long orderId = orderRepository.findAll().get(0).getId();
        
        final String update = mvc.perform(put("/api/orders/{id}", orderId)
				.contentType(MediaType.APPLICATION_JSON).content(TestUtils.asJsonString(orderDto))
				.accept(MediaType.APPLICATION_JSON)).andExpect(status().isOk()).andReturn().getResponse().getContentAsString();
				// .andExpect(jsonPath("$.id").value(orderId)).andReturn().getResponse().getContentAsString();
        assertTrue(update.contains("" + Status.PICKEDUP));
        
        
//		mvc.perform(put("/api/users/{id}", userRepository.findByUsername("knandak").get().getId())
//				.contentType(MediaType.APPLICATION_JSON).content(TestUtils.asJsonString(userDto))
//				.accept(MediaType.APPLICATION_JSON)).andExpect(status().isOk())
//				.andExpect(jsonPath("$.name").value("Karthik Nandakumar"))
        
        
//        OrderDto orderDto2 = new OrderDto( 2L, items, secondUser.getId(), 12.75, 0.26, 0.9, Status.PLACED, d);
    }
    
    @Test
    @Transactional
    @WithMockUser ( username = "suser2", roles = "CUSTOMER" )
    public void testOrderSecondUser() throws Exception {
//    	assertEquals(1, orderRepository.findAll().size());
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
        
        final Date date = new Date();
        final SimpleDateFormat formatter = new SimpleDateFormat( "yyyy-MM-dd HH:mm:ss" );
        final String d = formatter.format( date );

        final Map<Long, Integer> itemList = new HashMap<>();
        itemList.put( savedBread.getId(), 2 );
        itemList.put( savedHam.getId(), 3 );
        
        // Make a second customer user
        final UserDto second = new UserDto( 5L, "Second", "suser2", "second@ncsu.edu", "password2", Role.CUSTOMER );
        final UserDto savedUser2 = userService.createUser( second );
        final UserDto secondUser = userService.getUserById( savedUser2.getId() );

        OrderDto orderDto2 = new OrderDto( 2L, items, secondUser.getId(), 12.75, 0.26, 0.9, Status.PLACED, d);
        mvc.perform( post( "/api/orders" ).contentType( MediaType.APPLICATION_JSON )
                .content( TestUtils.asJsonString( orderDto2 ) ).accept( MediaType.APPLICATION_JSON ) )
                .andExpect( status().isCreated() );

        // GET for orders should return all the orders available to the user
        // with that specified id
        // GET for this customer should return the newly created order above
        final String orders = mvc.perform( get( "/api/orders" ) ).andDo( print() ).andExpect( status().isOk() )
                .andReturn().getResponse().getContentAsString();
        assertTrue( orders.contains("" + savedBread.getId()));
        assertTrue(orders.contains("" + orderDto2.getCustomerId()));
        assertFalse(orders.contains("" + userRepository.findByUsername("rthinsha")));
    }
    
    @Test
    @Transactional
    @WithMockUser ( username = "knandak", roles = "BARISTA" )
    public void testOrderBarista() throws Exception {
        userRepository.deleteAll();
        taxService.setTaxRate( 2.0 );
        
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

        final Date date = new Date();
        final SimpleDateFormat formatter = new SimpleDateFormat( "yyyy-MM-dd HH:mm:ss" );
        final String d = formatter.format( date );

        final Map<Long, Integer> itemList = new HashMap<>();
        itemList.put( savedBread.getId(), 2 );
        itemList.put( savedHam.getId(), 3 );

        OrderDto orderDto = new OrderDto(1L, itemList, ryanUser.getId(), 12.75, 0.26, 0.9, Status.PLACED, d );
        orderService.addOrder(orderDto);
        orderDto.setStatus(Status.FULFILLED);
        
        final UserDto baristaDto = new UserDto( 1, "Karthik Nandakumar", "knandak", "knandak@ncsu.edu", "cqhavhhv",
        Role.BARISTA );
        userService.createUser( baristaDto );

        // GET for orders should return all the orders available to the user
        // with that specified id
        // GET for this customer should return the newly created order above
        Long orderId = orderRepository.findAll().get(0).getId() + 1;
        
        final String update = mvc.perform(put("/api/orders/{id}", orderId)
				.contentType(MediaType.APPLICATION_JSON).content(TestUtils.asJsonString(orderDto))
				.accept(MediaType.APPLICATION_JSON)).andExpect(status().isOk())
				.andExpect(jsonPath("$.id").value(orderId)).andReturn().getResponse().getContentAsString();
        assertTrue(update.contains("" + Status.FULFILLED));
    }
    
	@AfterTestClass
	public void cleanUp() throws Exception {
		userRepository.deleteAll();
	}

}
