package edu.ncsu.csc326.wolfcafe.controller;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import edu.ncsu.csc326.wolfcafe.TestUtils;
import edu.ncsu.csc326.wolfcafe.dto.LoginDto;
import edu.ncsu.csc326.wolfcafe.dto.RegisterDto;
import edu.ncsu.csc326.wolfcafe.entity.Role;
import edu.ncsu.csc326.wolfcafe.entity.User;
import edu.ncsu.csc326.wolfcafe.repository.UserRepository;

/**
 * Tests for AuthController, which handles logging in users.
 */
@SpringBootTest
@AutoConfigureMockMvc
public class AuthControllerTest {

    /**
     * Password for the admin retrieved from application.properties
     */
    @Value ( "${app.admin-user-password}" )
    private String         adminUserPassword;

    /**
     * Connection to database to get user id and delete user before start
     */
    @Autowired
    private UserRepository userRepository;

    /**
     * Mock mvc to use for testing api calls
     */
    @Autowired
    private MockMvc        mvc;

    /**
     * Creates a user that will be used by other tests
     *
     * @throws Exception
     *             in case of unexpected error
     */
    @BeforeEach
    @Test
    @Transactional
    public void setUpAndTestCreateUser () throws Exception {
        userRepository.deleteByUsername( "jestes" );
        final RegisterDto registerDto = new RegisterDto( "Jordan Estes", "jestes", "vitae.erat@yahoo.edu",
                "JXB16TBD4LC" );

        mvc.perform( post( "/api/auth/register" ).contentType( MediaType.APPLICATION_JSON )
                .content( TestUtils.asJsonString( registerDto ) ).accept( MediaType.APPLICATION_JSON ) )
                .andExpect( status().isCreated() ).andExpect( content().string( "User registered successfully." ) );
    }

    /**
     * Tests logging in the admin user
     *
     * @throws Exception
     *             in case of unexpected error
     */
    @Test
    @Transactional
    public void testLoginAdmin () throws Exception {
        final LoginDto loginDto = new LoginDto( "admin", adminUserPassword );

        if ( userRepository.findByUsername( "admin" ).isEmpty() ) {
            User admin = new User( 0L, "Admin User", "admin", "admin@admin.edu", adminUserPassword,
                    new Role( 0L, "ADMIN" ) );
            userRepository.save( admin );
        }

        mvc.perform( post( "/api/auth/login" ).contentType( MediaType.APPLICATION_JSON )
                .content( TestUtils.asJsonString( loginDto ) ).accept( MediaType.APPLICATION_JSON ) )
                .andExpect( status().isOk() ).andExpect( jsonPath( "$.tokenType" ).value( "Bearer" ) )
                .andExpect( jsonPath( "$.role" ).value( "ROLE_ADMIN" ) );
    }

    /**
     * Tests creating and then logging in a customer
     *
     * @throws Exception
     *             in case of unexpected error
     */
    @Test
    @Transactional
    public void testCreateCustomerAndLogin () throws Exception {

        final LoginDto loginDto = new LoginDto( "jestes", "JXB16TBD4LC" );

        mvc.perform( post( "/api/auth/login" ).contentType( MediaType.APPLICATION_JSON )
                .content( TestUtils.asJsonString( loginDto ) ).accept( MediaType.APPLICATION_JSON ) )
                .andExpect( status().isOk() ).andExpect( jsonPath( "$.tokenType" ).value( "Bearer" ) )
                .andExpect( jsonPath( "$.role" ).value( "ROLE_CUSTOMER" ) );
    }

    /**
     * Test deleting a user
     *
     * @throws Exception
     *             in case of unexpected error
     */
    @Test
    @Transactional
    @WithMockUser ( username = "admin", roles = "ADMIN" )
    public void testDeleteCustomer () throws Exception {
        mvc.perform( delete( "/api/auth/user/" + userRepository.findByUsername( "jestes" ).get().getId() )
                .accept( MediaType.APPLICATION_JSON ) ).andExpect( status().isOk() )
                .andExpect( content().string( "User deleted successfully." ) );

        assertTrue( userRepository.findByUsername( "jestes" ).isEmpty() );
    }

}
