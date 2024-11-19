package edu.ncsu.csc326.wolfcafe.controller;

import static org.junit.jupiter.api.Assertions.*;

import org.hibernate.mapping.Map;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import edu.ncsu.csc326.wolfcafe.dto.ItemDto;
import edu.ncsu.csc326.wolfcafe.dto.OrderDto;
import edu.ncsu.csc326.wolfcafe.entity.Order;

class OrderControllerTest {
	
    /** Mock MVC for testing controller */
    @Autowired
    private MockMvc        mvc;
	
    /**
     * Api endpoint under test
     */
    private static final String       API_PATH         = "/api/items";
    /**
     * Encoding for performing api calls
     */
    private static final String       ENCODING         = "utf-8";
    /**
     * Name of test item
     */
    private static final String       ITEM_NAME        = "Coffee";
    /**
     * Description of test item
     */
    private static final String       ITEM_DESCRIPTION = "Coffee is life";
    /**
     * Price of test item
     */
    private static final double       ITEM_PRICE       = 3.25;

	@PreAuthorize ( "hasRole('CUSTOMER')" )
	
    @Test
    @Transactional
    @WithMockUser ( username = "customer", roles = "CUSTOMER" )
    public void testAddOrder () throws Exception {
        OrderDto orderDto = new OrderDto();
        
        final ItemDto itemDto = new ItemDto();
        itemDto.setName( ITEM_NAME );
        itemDto.setDescription( ITEM_DESCRIPTION );
        itemDto.setPrice( ITEM_PRICE );
        
//        Map<Long, Integer> items = new Map<>();
//
//        Order order = new Order();
//        
//
//        mvc.perform( post( "/api/order" ).contentType( MediaType.APPLICATION_JSON ).characterEncoding( ENCODING )
//                .content( json ).accept( MediaType.APPLICATION_JSON ) ).andExpect( status().isCreated() )
//                .andExpect( jsonPath( "$.name", Matchers.equalTo( ITEM_NAME ) ) )
//                .andExpect( jsonPath( "$.description", Matchers.equalTo( ITEM_DESCRIPTION ) ) )
//                .andExpect( jsonPath( "$.price", Matchers.equalTo( ITEM_PRICE ) ) );
    }

}
