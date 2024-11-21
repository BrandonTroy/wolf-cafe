package edu.ncsu.csc326.wolfcafe.controller;

import static org.junit.jupiter.api.Assertions.assertFalse;
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
import edu.ncsu.csc326.wolfcafe.repository.TaxRepository;

/**
 * Test TaxController functionality to set and get a tax rate
 */
@SpringBootTest
@AutoConfigureMockMvc
public class TaxControllerTest {
    /** Mock MVC for testing controller */
    @Autowired
    private MockMvc       mvc;

    /**
     * Connection to the table containing tax to reset after a test
     */
    @Autowired
    private TaxRepository taxRepository;

    /**
     * Test the PUT /api/tax endpoint
     *
     * @throws Exception
     *             if issue while running the test
     */
    @Test
    @Transactional
    @WithMockUser ( username = "admin", roles = "ADMIN" )
    public void testSetTaxRate () throws Exception {
        taxRepository.deleteAll();
        // Test valid tax rates
        mvc.perform( put( "/api/tax" ).contentType( MediaType.APPLICATION_JSON )
                .content( TestUtils.asJsonString( 5.55 ) ).accept( MediaType.APPLICATION_JSON ) )
                .andExpect( status().isOk() ).andExpect( content().string( TestUtils.asJsonString( 5.55 ) ) );

        assertFalse( taxRepository.findAll().isEmpty() );

        mvc.perform( put( "/api/tax" ).contentType( MediaType.APPLICATION_JSON )
                .content( TestUtils.asJsonString( 49.9 ) ).accept( MediaType.APPLICATION_JSON ) )
                .andExpect( status().isOk() ).andExpect( content().string( TestUtils.asJsonString( 49.9 ) ) );

        // Test invalid tax rates
        mvc.perform( put( "/api/tax" ).contentType( MediaType.APPLICATION_JSON ).content( TestUtils.asJsonString( 51 ) )
                .accept( MediaType.APPLICATION_JSON ) ).andExpect( status().isBadRequest() );

        mvc.perform( put( "/api/tax" ).contentType( MediaType.APPLICATION_JSON ).content( TestUtils.asJsonString( -1 ) )
                .accept( MediaType.APPLICATION_JSON ) ).andExpect( status().isBadRequest() );
    }

    /**
     * Test the GET /api/tax endpoint
     *
     * @throws Exception
     *             if issue while running the test
     */
    @Test
    @Transactional
    @WithMockUser ( username = "admin", roles = "ADMIN" )
    public void testGetTaxRate () throws Exception {
        taxRepository.deleteAll();

        // Check if a proper error is thrown when no tax information exists
        mvc.perform( get( "/api/tax" ) ).andExpect( status().isNotFound() );

        // Check valid tax rate
        mvc.perform( put( "/api/tax" ).contentType( MediaType.APPLICATION_JSON )
                .content( TestUtils.asJsonString( 5.55 ) ).accept( MediaType.APPLICATION_JSON ) )
                .andExpect( status().isOk() ).andExpect( content().string( TestUtils.asJsonString( 5.55 ) ) );

        System.out.println( taxRepository.findAll().get( 0 ).getId() );

        mvc.perform( get( "/api/tax" ) ).andExpect( content().string( TestUtils.asJsonString( 5.55 ) ) )
                .andExpect( status().isOk() );

        // Check that invalid request doesn't change the tax rate
        mvc.perform( put( "/api/tax" ).contentType( MediaType.APPLICATION_JSON ).content( TestUtils.asJsonString( 51 ) )
                .accept( MediaType.APPLICATION_JSON ) ).andExpect( status().isBadRequest() );
        mvc.perform( get( "/api/tax" ) ).andExpect( content().string( TestUtils.asJsonString( 5.55 ) ) )
                .andExpect( status().isOk() );

    }

}
