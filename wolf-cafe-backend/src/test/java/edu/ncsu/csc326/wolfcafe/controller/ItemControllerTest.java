package edu.ncsu.csc326.wolfcafe.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jayway.jsonpath.JsonPath;

import edu.ncsu.csc326.wolfcafe.TestUtils;
import edu.ncsu.csc326.wolfcafe.dto.ItemDto;
import edu.ncsu.csc326.wolfcafe.repository.ItemRepository;

/**
 * Tests for ItemController, including creating and getting items
 */
@SpringBootTest
@AutoConfigureMockMvc
public class ItemControllerTest {

    /**
     * Mock mvc for making test api calls
     */
    @Autowired
    private MockMvc                   mvc;

    /**
     * Connection to the database to delete items
     */
    @Autowired
    private ItemRepository            itemRepository;

    /**
     * Used to convert objects to Json
     */
    private static final ObjectMapper MAPPER           = new ObjectMapper();

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

    /**
     * Sets up testing environment by deleting all existing items to avoid
     * duplicate names
     */
    @BeforeEach
    public void setUp () {
        itemRepository.deleteAll();
    }

    /**
     * Test for creating a new item with a POST request
     *
     * @throws Exception
     *             in case of an unexpected error
     */
    @Test
    @WithMockUser ( username = "staff", roles = "STAFF" )
    public void testCreateItem () throws Exception {
        // Create ItemDto with all contents but the id
        final ItemDto itemDto = new ItemDto();
        itemDto.setName( ITEM_NAME );
        itemDto.setDescription( ITEM_DESCRIPTION );
        itemDto.setPrice( ITEM_PRICE );

        final String json = MAPPER.writeValueAsString( itemDto );

        mvc.perform( post( API_PATH ).contentType( MediaType.APPLICATION_JSON ).characterEncoding( ENCODING )
                .content( json ).accept( MediaType.APPLICATION_JSON ) ).andExpect( status().isCreated() )
                .andExpect( jsonPath( "$.name", Matchers.equalTo( ITEM_NAME ) ) )
                .andExpect( jsonPath( "$.description", Matchers.equalTo( ITEM_DESCRIPTION ) ) )
                .andExpect( jsonPath( "$.price", Matchers.equalTo( ITEM_PRICE ) ) );
    }

    /**
     * Testing that non-staff users cannot create items
     *
     * @throws Exception
     *             in case of unexpected error
     */
    @Test
    public void testCreateItemNotStaff () throws Exception {
        // Create ItemDto with all contents but the id
        final ItemDto itemDto = new ItemDto();
        itemDto.setName( ITEM_NAME );
        itemDto.setDescription( ITEM_DESCRIPTION );
        itemDto.setPrice( ITEM_PRICE );

        final String json = MAPPER.writeValueAsString( itemDto );

        mvc.perform( post( API_PATH ).contentType( MediaType.APPLICATION_JSON ).characterEncoding( ENCODING )
                .content( json ).accept( MediaType.APPLICATION_JSON ) ).andExpect( status().isUnauthorized() );
    }

    /**
     * Testing retrieving an item with a GET api call
     *
     * @throws Exception
     *             in case of unexpected error
     */
    @Test
    @WithMockUser ( username = "staff", roles = "STAFF" )
    public void testGetItemById () throws Exception {
        final ItemDto itemDto = new ItemDto();
        itemDto.setName( ITEM_NAME );
        itemDto.setDescription( ITEM_DESCRIPTION );
        itemDto.setPrice( ITEM_PRICE );

        final String json = "";

        final Long itemId = JsonPath.parse( mvc
                .perform( post( API_PATH ).contentType( MediaType.APPLICATION_JSON ).characterEncoding( ENCODING )
                        .content( TestUtils.asJsonString( itemDto ) ).accept( MediaType.APPLICATION_JSON ) )
                .andExpect( status().isCreated() ).andReturn().getResponse().getContentAsString() )
                .read( "$.id", Long.class );

        mvc.perform( get( API_PATH + "/" + itemId ).contentType( MediaType.APPLICATION_JSON )
                .characterEncoding( ENCODING ).content( json ).accept( MediaType.APPLICATION_JSON ) )
                .andExpect( status().isOk() ).andExpect( jsonPath( "$.name", Matchers.equalTo( ITEM_NAME ) ) )
                .andExpect( jsonPath( "$.description", Matchers.equalTo( ITEM_DESCRIPTION ) ) )
                .andExpect( jsonPath( "$.price", Matchers.equalTo( ITEM_PRICE ) ) );
    }

    /**
     * Tests get on the items endpoint with no items
     *
     * @throws Exception
     *             in case of unexpected error
     */
    @Test
    @WithMockUser ( username = "staff", roles = "STAFF" )
    public void testGetItems () throws Exception {
        final String items = mvc.perform( get( API_PATH ) ).andDo( print() ).andExpect( status().isOk() ).andReturn()
                .getResponse().getContentAsString();
        assertEquals( "[]", items );
    }

    /**
     * Tests delete on the items endpoint, by creating an item and then calling
     * the endpoint to delete the item
     *
     * @throws Exception
     *             in case of unexpected error
     */
    @Test
    @WithMockUser ( username = "staff", roles = "STAFF" )
    public void testDeleteItem () throws Exception {
        final ItemDto itemDto = new ItemDto( 0L, ITEM_NAME, ITEM_DESCRIPTION, ITEM_PRICE );

        final Long itemId = JsonPath.parse( mvc
                .perform( post( API_PATH ).contentType( MediaType.APPLICATION_JSON ).characterEncoding( ENCODING )
                        .content( TestUtils.asJsonString( itemDto ) ).accept( MediaType.APPLICATION_JSON ) )
                .andExpect( status().isCreated() ).andReturn().getResponse().getContentAsString() )
                .read( "$.id", Long.class );

        mvc.perform( get( API_PATH + "/" + itemId ).contentType( MediaType.APPLICATION_JSON )
                .characterEncoding( ENCODING ).content( "" ).accept( MediaType.APPLICATION_JSON ) )
                .andExpect( status().isOk() ).andExpect( jsonPath( "$.id" ).value( itemId ) )
                .andExpect( jsonPath( "$.name", Matchers.equalTo( ITEM_NAME ) ) )
                .andExpect( jsonPath( "$.description", Matchers.equalTo( ITEM_DESCRIPTION ) ) )
                .andExpect( jsonPath( "$.price", Matchers.equalTo( ITEM_PRICE ) ) );

        mvc.perform( delete( API_PATH + "/" + itemId ).accept( MediaType.APPLICATION_JSON ) )
                .andExpect( status().isOk() );

        // Verify the old item id no longer exists
        mvc.perform( get( API_PATH + "/" + itemId ).accept( MediaType.APPLICATION_JSON ) )
                .andExpect( status().isNotFound() );
    }

    /**
     * Tests updating an item through a put call, by creating an item, updating
     * it, and then calling the endpoint to get the updated item
     *
     * @throws Exception
     *             in case of unexpected error
     */
    @Test
    @WithMockUser ( username = "staff", roles = "STAFF" )
    public void testUpdateItem () throws Exception {
        // Create initial item
        final ItemDto itemDto = new ItemDto( 0L, ITEM_NAME, ITEM_DESCRIPTION, ITEM_PRICE );

        final Long itemId = JsonPath.parse( mvc
                .perform( post( API_PATH ).contentType( MediaType.APPLICATION_JSON ).characterEncoding( ENCODING )
                        .content( TestUtils.asJsonString( itemDto ) ).accept( MediaType.APPLICATION_JSON ) )
                .andExpect( status().isCreated() ).andReturn().getResponse().getContentAsString() )
                .read( "$.id", Long.class );

        // Update item
        final ItemDto updatedItemDto = new ItemDto( itemId, "Mocha", "idk what a Mocha is I don't drink coffee",
                19.99 );

        mvc.perform( put( API_PATH + "/" + itemId ).contentType( MediaType.APPLICATION_JSON )
                .content( TestUtils.asJsonString( updatedItemDto ) ).accept( MediaType.APPLICATION_JSON ) )
                .andExpect( status().isOk() );

        // Verify updated item
        mvc.perform( get( API_PATH + "/" + itemId ).accept( MediaType.APPLICATION_JSON ) ).andExpect( status().isOk() )
                .andExpect( jsonPath( "$.id" ).value( itemId ) ).andExpect( jsonPath( "$.name" ).value( "Mocha" ) )
                .andExpect( jsonPath( "$.description" ).value( "idk what a Mocha is I don't drink coffee" ) )
                .andExpect( jsonPath( "$.price" ).value( 19.99 ) );
    }
}
