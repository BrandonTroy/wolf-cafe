package edu.ncsu.csc326.wolfcafe.service.impl;

import static org.junit.jupiter.api.Assertions.*;

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
import edu.ncsu.csc326.wolfcafe.dto.OrderDto;
import edu.ncsu.csc326.wolfcafe.dto.UserDto;
import edu.ncsu.csc326.wolfcafe.entity.Item;
import edu.ncsu.csc326.wolfcafe.entity.Role;
import edu.ncsu.csc326.wolfcafe.entity.Status;
import edu.ncsu.csc326.wolfcafe.repository.ItemRepository;
import edu.ncsu.csc326.wolfcafe.repository.UserRepository;
import edu.ncsu.csc326.wolfcafe.service.InventoryService;
import edu.ncsu.csc326.wolfcafe.service.OrderService;
import edu.ncsu.csc326.wolfcafe.service.UserService;

@SpringBootTest
@AutoConfigureMockMvc
class OrderServiceTest {
	
	/** order repository */
	@Autowired
	private OrderService orderService;
	
	/** user service */
	@Autowired
	private UserService userService;
	
	/** user repository */
	@Autowired
	private UserRepository userRepository;
	
	/** item repository */
	@Autowired
	private ItemRepository itemRepository;
	
	/** inventory service */
	@Autowired
	private InventoryService inventoryService;
	
	
	/** Deletes repositories before each test */
	@BeforeEach
	public void setUp() throws Exception {
		userRepository.deleteAll();
	}

	@Test
	@Transactional
	public void testOrderHistory() throws NoSuchAlgorithmException {
		//Necessary arguments for making an order
		Map<Long, Integer> itemList = new HashMap<>();
		Item bread = new Item(1L, "bread", "bread item", 1.50);
		Item ham = new Item(2L, "ham", "ham item", 3.25);
		itemList.put(bread.getId(), 2);
		itemList.put(ham.getId(), 3);
        Date date = new Date();
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"); 
        String d = formatter.format(date);
	    Status status = Status.PLACED;

	    //Make a customer user
		UserDto ryan = new UserDto(4L, "Ryan", "rthinsha", "rthinsha@ncsu.edu", "password", Role.CUSTOMER);
		UserDto savedUser = userService.createUser(ryan);
		UserDto ryanUser = userService.getUserById(savedUser.getId());
		OrderDto orderDto = new OrderDto(1L, itemList, ryanUser.getId(), 0.9, 12.75, status, d);
		
		//Make a second customer user
		UserDto second = new UserDto(5L, "Second", "suser2", "second@ncsu.edu", "password2", Role.CUSTOMER);
		UserDto savedUser2 = userService.createUser(second);
		UserDto secondUser = userService.getUserById(savedUser2.getId());
		OrderDto orderDto2 = new OrderDto(2L, itemList, secondUser.getId(), 0.9, 12.75, status, d);
		
		//Make a manager
        UserDto managerDto = new UserDto(1, "Karthik Nandakumar", "knandak", "knandak@ncsu.edu", "cqhavhhv",
				Role.MANAGER);
		UserDto savedManager = userService.createUser(managerDto);
		UserDto retrievedManager = userService.getUserById(savedManager.getId());
		
		//Add orders to order service
		orderService.addOrder(orderDto);
		orderService.addOrder(orderDto2);
		
		//Getting order history for the customers should return 1, because they only have one order
		//But order history for the manager returns two because they can see all orders
		Map<Long, OrderDto> orders = orderService.getOrderHistory(ryanUser.getId());
		Map<Long, OrderDto> orders2 = orderService.getOrderHistory(secondUser.getId());
		Map<Long, OrderDto> orders3 = orderService.getOrderHistory(retrievedManager.getId());
		assertEquals(1, orders.size());
		assertEquals(1, orders2.size());
		assertEquals(2, orders3.size());
	}
	
	@Test
	@Transactional
	public void testEditOrder() throws NoSuchAlgorithmException {
		//Necessary arguments for making an order
		Map<Long, Integer> itemList = new HashMap<>();
        Date date = new Date();
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"); 
        String d = formatter.format(date);
	    Status status = Status.PLACED;
	    
		Item bread = new Item(1L, "bread", "bread item", 1.50);
		Item ham = new Item(2L, "ham", "ham item", 3.25);
	    Item cheese = new Item(3L, "cheese", "cheese item", 1.25);
		Item tomato = new Item(4L, "tomato", "tomato item", 2.40);
	    Item savedBread = itemRepository.save(bread);
	    Item savedHam = itemRepository.save(ham);
	    Item savedCheese = itemRepository.save(cheese);
	    Item savedTomato = itemRepository.save(tomato);
	    
		itemList.put(savedBread.getId(), 2);
		itemList.put(savedHam.getId(), 3);
	    
	    InventoryDto inventoryDto = new InventoryDto();
	    Map<Long, Integer> items = new HashMap<>();
	    items.put(savedBread.getId(), 20);
	    items.put(savedHam.getId(), 20);
	    items.put(savedCheese.getId(), 20);
	    items.put(savedTomato.getId(), 20);
	    inventoryDto.setItemQuantities(items);
	    assertEquals(4, inventoryDto.getItemQuantities().size());
	    inventoryService.addInventory(inventoryDto);
	    
	    assertEquals(20, inventoryService.getInventory().getItemQuantities().get(savedBread.getId()));
	    assertEquals(20, inventoryService.getInventory().getItemQuantities().get(savedHam.getId()));
	    assertEquals(20, inventoryService.getInventory().getItemQuantities().get(savedCheese.getId()));
	    assertEquals(20, inventoryService.getInventory().getItemQuantities().get(savedTomato.getId()));
	    
	    //Make a customer user
	  	UserDto ryan = new UserDto(4L, "Ryan", "rthinsha", "rthinsha@ncsu.edu", "password", Role.CUSTOMER);
	  	UserDto savedUser = userService.createUser(ryan);
	 	UserDto ryanUser = userService.getUserById(savedUser.getId());
	  	OrderDto orderDto = new OrderDto(1L, itemList, ryanUser.getId(), 0.9, 12.75, status, d);
	  	
	  	//Make a barista
        UserDto baristaDto = new UserDto(1, "Karthik Nandakumar", "knandak", "knandak@ncsu.edu", "cqhavhhv",
				Role.BARISTA);
        userService.createUser(baristaDto);
//		UserDto savedBarista = userService.createUser(baristaDto);
//		UserDto retrievedBarista = userService.getUserById(savedBarista.getId());
		
		OrderDto updatedDto = orderService.editOrder(orderDto, Status.FULFILLED);
		assertEquals(Status.FULFILLED, updatedDto.getStatus());
		assertEquals(18, inventoryService.getInventory().getItemQuantities().get(savedBread.getId()));
		assertEquals(17, inventoryService.getInventory().getItemQuantities().get(savedHam.getId()));
		assertEquals(20, inventoryService.getInventory().getItemQuantities().get(savedCheese.getId()));
		assertEquals(20, inventoryService.getInventory().getItemQuantities().get(savedTomato.getId()));
		
		OrderDto pickedUp = orderService.editOrder(orderDto, Status.PICKEDUP);
		assertEquals(Status.PICKEDUP, pickedUp.getStatus());
		assertEquals(18, inventoryService.getInventory().getItemQuantities().get(savedBread.getId()));
		assertEquals(17, inventoryService.getInventory().getItemQuantities().get(savedHam.getId()));
		assertEquals(20, inventoryService.getInventory().getItemQuantities().get(savedCheese.getId()));
		assertEquals(20, inventoryService.getInventory().getItemQuantities().get(savedTomato.getId()));
	}
	

}
