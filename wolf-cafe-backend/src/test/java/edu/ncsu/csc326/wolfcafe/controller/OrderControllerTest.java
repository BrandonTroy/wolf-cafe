package edu.ncsu.csc326.wolfcafe.controller;

import static org.junit.jupiter.api.Assertions.*;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.junit.jupiter.api.Test;
import org.hamcrest.Matchers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import edu.ncsu.csc326.wolfcafe.TestUtils;
import edu.ncsu.csc326.wolfcafe.dto.ItemDto;
import edu.ncsu.csc326.wolfcafe.dto.OrderDto;
import edu.ncsu.csc326.wolfcafe.dto.UserDto;
import edu.ncsu.csc326.wolfcafe.entity.Item;
import edu.ncsu.csc326.wolfcafe.entity.Order;
import edu.ncsu.csc326.wolfcafe.entity.Role;
import edu.ncsu.csc326.wolfcafe.entity.Status;
import edu.ncsu.csc326.wolfcafe.mapper.OrderMapper;
import edu.ncsu.csc326.wolfcafe.repository.ItemRepository;
import edu.ncsu.csc326.wolfcafe.repository.OrderRepository;
import edu.ncsu.csc326.wolfcafe.service.OrderService;
import edu.ncsu.csc326.wolfcafe.service.UserService;

@SpringBootTest
@AutoConfigureMockMvc
class OrderControllerTest {
	
    /** Mock MVC for testing controller */
    @Autowired
    private MockMvc mvc;
    
	/** user service */
	@Autowired
	private UserService userService;
	
	@Autowired
	private ItemRepository itemRepository;
	
//    /**
//     * Api endpoint under test
//     */
//    private static final String       API_PATH         = "/api/orders";
//    /**
//     * Encoding for performing api calls
//     */
//    private static final String       ENCODING         = "utf-8";

	
    @Test
    @Transactional
    @WithMockUser ( username = "customer", roles = "CUSTOMER" )
    public void testAddAndPickupOrder () throws Exception {
		Item bread = new Item(1L, "bread", "bread item", 1.50);
		Item ham = new Item(2L, "ham", "ham item", 3.25);
	    Item cheese = new Item(3L, "cheese", "cheese item", 1.25);
		Item tomato = new Item(4L, "tomato", "tomato item", 2.40);
	    Item savedBread = itemRepository.save(bread);
	    Item savedHam = itemRepository.save(ham);
	    Item savedCheese = itemRepository.save(cheese);
	    Item savedTomato = itemRepository.save(tomato);
		
	  	UserDto ryan = new UserDto(4L, "Ryan", "rthinsha", "rthinsha@ncsu.edu", "password", Role.CUSTOMER);
	  	UserDto savedUser = userService.createUser(ryan);
	 	UserDto ryanUser = userService.getUserById(savedUser.getId());
	 	
        UserDto baristaDto = new UserDto(1, "Karthik Nandakumar", "knandak", "knandak@ncsu.edu", "cqhavhhv",
				Role.BARISTA);
        userService.createUser(baristaDto);
	 	
        Date date = new Date();
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"); 
        String d = formatter.format(date);
		
		Map<Long, Integer> itemList = new HashMap<>();
		itemList.put(savedBread.getId(), 2);
		itemList.put(savedHam.getId(), 3);
		
	  	OrderDto orderDto = new OrderDto(1L, itemList, ryanUser.getId(), 0.9, 12.75, Status.PLACED, d);

	  	mvc.perform( post( "/api/order" ).contentType( MediaType.APPLICATION_JSON )
                .content( TestUtils.asJsonString( orderDto ) ).accept( MediaType.APPLICATION_JSON ) )
                .andExpect( status().isCreated() );

	  	//GET for orders should return all the orders available to the user with that specified id
	  	//GET for this customer should return the newly created order above
//        String orders = mvc.perform( get( "/api/order" ) ).andDo( print() ).andExpect( status().isOk() ).andReturn()
//                .getResponse().getContentAsString();
//        assertTrue( orders.contains( "bread" ) );
    }
    
    

}
