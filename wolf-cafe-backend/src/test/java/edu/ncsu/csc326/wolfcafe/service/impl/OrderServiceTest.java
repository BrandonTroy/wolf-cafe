package edu.ncsu.csc326.wolfcafe.service.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import edu.ncsu.csc326.wolfcafe.dto.InventoryDto;
import edu.ncsu.csc326.wolfcafe.dto.ItemDto;
import edu.ncsu.csc326.wolfcafe.dto.OrderDto;
import edu.ncsu.csc326.wolfcafe.dto.UserDto;
import edu.ncsu.csc326.wolfcafe.entity.Role;
import edu.ncsu.csc326.wolfcafe.entity.Status;
import edu.ncsu.csc326.wolfcafe.repository.ItemRepository;
import edu.ncsu.csc326.wolfcafe.repository.OrderRepository;
import edu.ncsu.csc326.wolfcafe.repository.UserRepository;
import edu.ncsu.csc326.wolfcafe.service.InventoryService;
import edu.ncsu.csc326.wolfcafe.service.ItemService;
import edu.ncsu.csc326.wolfcafe.service.OrderService;
import edu.ncsu.csc326.wolfcafe.service.TaxService;
import edu.ncsu.csc326.wolfcafe.service.UserService;

@SpringBootTest
@AutoConfigureMockMvc
class OrderServiceTest {

    /** order repository */
    @Autowired
    private OrderService     orderService;

    /** user service */
    @Autowired
    private UserService      userService;

    /** user repository */
    @Autowired
    private UserRepository   userRepository;

    /** item repository */
    @Autowired
    private ItemRepository   itemRepository;

    /** order repository */
    @Autowired
    private OrderRepository  orderRepository;

    /** item service */
    @Autowired
    private ItemService      itemService;

    /** inventory service */
    @Autowired
    private InventoryService inventoryService;

    /** tax service */
    @Autowired
    private TaxService       taxService;

    /** Deletes repositories before each test */
    @BeforeEach
    public void setUp () throws Exception {
        userRepository.deleteAll();
        itemRepository.deleteAll();
        orderRepository.deleteAll();
        taxService.setTaxRate( 2.0 );
    }

    @Test
    @Transactional
    public void testOrderHistory () {
        // Necessary arguments for making an order
        final Map<Long, Integer> itemList = new HashMap<>();
        final ItemDto bread = new ItemDto( 1L, "bread", "bread item", 1.50 );
        final ItemDto ham = new ItemDto( 2L, "ham", "ham item", 3.25 );
        final ItemDto savedBread = itemService.addItem( bread );
        final ItemDto savedHam = itemService.addItem( ham );
        itemList.put( savedBread.getId(), 2 );
        itemList.put( savedHam.getId(), 3 );

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
        final Status status = Status.PLACED;

        // Make a customer user
        final UserDto ryan = new UserDto( 4L, "Ryan", "rthinsha", "rthinsha@ncsu.edu", "password", Role.CUSTOMER );
        final UserDto savedUser = userService.createUser( ryan );
        final UserDto ryanUser = userService.getUserById( savedUser.getId() );
        final OrderDto orderDto = new OrderDto( 1L, itemList, ryanUser.getId(), 12.75, 0.26, 0.9, status, d );

        // Make a second customer user
        final UserDto second = new UserDto( 5L, "Second", "suser2", "second@ncsu.edu", "password2", Role.CUSTOMER );
        final UserDto savedUser2 = userService.createUser( second );
        final UserDto secondUser = userService.getUserById( savedUser2.getId() );
        final OrderDto orderDto2 = new OrderDto( 2L, itemList, secondUser.getId(), 12.75, 0.26, 0.9, status, d );

        // Make a manager
        final UserDto managerDto = new UserDto( 1, "Karthik Nandakumar", "knandak", "knandak@ncsu.edu", "cqhavhhv",
                Role.MANAGER );
        final UserDto savedManager = userService.createUser( managerDto );
        final UserDto retrievedManager = userService.getUserById( savedManager.getId() );

        // Add orders to order service
        orderService.addOrder( orderDto );
        orderService.addOrder( orderDto2 );

        // Getting order history for the customers should return 1, because they
        // only have one order
        // But order history for the manager returns two because they can see
        // all orders
        final Map<Long, OrderDto> orders = orderService.getOrderHistory( ryanUser.getUsername() );
        final Map<Long, OrderDto> orders2 = orderService.getOrderHistory( secondUser.getUsername() );
        final Map<Long, OrderDto> orders3 = orderService.getOrderHistory( retrievedManager.getUsername() );
        assertEquals( 1, orders.size() );
        assertEquals( 1, orders2.size() );
        assertEquals( 2, orders3.size() );

        final OrderDto orderError = new OrderDto( 4L, itemList, ryanUser.getId(), 12.75, 0.26, 0.9, Status.FULFILLED,
                d );
        assertThrows( IllegalArgumentException.class, () -> orderService.addOrder( orderError ) );
    }

    @Test
    @Transactional
    public void testEditOrder () throws NoSuchAlgorithmException {

        // Necessary arguments for making an order
        final Map<Long, Integer> itemList = new HashMap<>();
        final Date date = new Date();
        final SimpleDateFormat formatter = new SimpleDateFormat( "yyyy-MM-dd HH:mm:ss" );
        final String d = formatter.format( date );
        final Status status = Status.PLACED;

        assertTrue( itemRepository.findAll().isEmpty() );

        final ItemDto bread = new ItemDto( 0L, "bread", "bread item", 1.50 );
        final ItemDto savedBread = itemService.addItem( bread );
        final ItemDto ham = new ItemDto( 1L, "ham", "ham item", 3.25 );
        final ItemDto savedHam = itemService.addItem( ham );
        final ItemDto cheese = new ItemDto( 2L, "cheese", "cheese item", 1.25 );
        final ItemDto savedCheese = itemService.addItem( cheese );

        itemList.put( savedBread.getId(), 2 );
        itemList.put( savedHam.getId(), 3 );

        final InventoryDto inventoryDto = new InventoryDto();
        final Map<Long, Integer> items = new HashMap<>();
        items.put( savedBread.getId(), 10 );
        items.put( savedHam.getId(), 10 );
        items.put( savedCheese.getId(), 10 );
        inventoryDto.setItemQuantities( items );
        inventoryService.addInventory( inventoryDto );

        assertEquals( 10, inventoryService.getInventory().getItemQuantities().get( savedBread.getId() ) );
        assertEquals( 10, inventoryService.getInventory().getItemQuantities().get( savedHam.getId() ) );
        assertEquals( 10, inventoryService.getInventory().getItemQuantities().get( savedCheese.getId() ) );

        // Make a customer user
        final UserDto ryan = new UserDto( 4L, "Ryan", "rthinsha", "rthinsha@ncsu.edu", "password", Role.CUSTOMER );
        final UserDto savedUser = userService.createUser( ryan );
        final UserDto ryanUser = userService.getUserById( savedUser.getId() );
        final OrderDto orderDto = new OrderDto( 1L, itemList, ryanUser.getId(), 12.75, 0.26, 0.9, status, d );

        // Make a barista
        final UserDto baristaDto = new UserDto( 1, "Karthik Nandakumar", "knandak", "knandak@ncsu.edu", "cqhavhhv",
                Role.BARISTA );
        userService.createUser( baristaDto );
        // UserDto savedBarista = userService.createUser(baristaDto);
        // UserDto retrievedBarista =
        // userService.getUserById(savedBarista.getId());

        orderService.addOrder( orderDto );
        assertEquals( 8, inventoryService.getInventory().getItemQuantities().get( savedBread.getId() ) );
        assertEquals( 7, inventoryService.getInventory().getItemQuantities().get( savedHam.getId() ) );
        assertEquals( 10, inventoryService.getInventory().getItemQuantities().get( savedCheese.getId() ) );

        final OrderDto updatedDto = orderService.editOrder( orderDto, Status.FULFILLED );
        assertEquals( Status.FULFILLED, updatedDto.getStatus() );
        assertEquals( 8, inventoryService.getInventory().getItemQuantities().get( savedBread.getId() ) );
        assertEquals( 7, inventoryService.getInventory().getItemQuantities().get( savedHam.getId() ) );
        assertEquals( 10, inventoryService.getInventory().getItemQuantities().get( savedCheese.getId() ) );

        final OrderDto pickedUp = orderService.editOrder( orderDto, Status.PICKEDUP );
        assertEquals( Status.PICKEDUP, pickedUp.getStatus() );
        assertEquals( 8, inventoryService.getInventory().getItemQuantities().get( savedBread.getId() ) );
        assertEquals( 7, inventoryService.getInventory().getItemQuantities().get( savedHam.getId() ) );
        assertEquals( 10, inventoryService.getInventory().getItemQuantities().get( savedCheese.getId() ) );

        final OrderDto orderDto2 = new OrderDto( 12L, itemList, ryanUser.getId(), 12.75, 0.26, 0.9, status, d );

        orderService.addOrder( orderDto2 );
        assertEquals( 6, inventoryService.getInventory().getItemQuantities().get( savedBread.getId() ) );
        assertEquals( 4, inventoryService.getInventory().getItemQuantities().get( savedHam.getId() ) );
        assertEquals( 10, inventoryService.getInventory().getItemQuantities().get( savedCheese.getId() ) );

        orderService.addOrder( orderDto2 );
        assertEquals( 4, inventoryService.getInventory().getItemQuantities().get( savedBread.getId() ) );
        assertEquals( 1, inventoryService.getInventory().getItemQuantities().get( savedHam.getId() ) );
        assertEquals( 10, inventoryService.getInventory().getItemQuantities().get( savedCheese.getId() ) );

        assertThrows( IllegalArgumentException.class, () -> orderService.addOrder( orderDto ) );
    }

}
