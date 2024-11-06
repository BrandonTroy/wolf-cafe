package edu.ncsu.csc326.wolfcafe.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import edu.ncsu.csc326.wolfcafe.TestUtils;
import edu.ncsu.csc326.wolfcafe.dto.UserDto;
import edu.ncsu.csc326.wolfcafe.entity.Role;
import edu.ncsu.csc326.wolfcafe.entity.User;
import edu.ncsu.csc326.wolfcafe.repository.UserRepository;

/**
 * Tests for user controller
 *
 * @author Karthik Nandakumar
 */
@SpringBootTest
@AutoConfigureMockMvc
public class UserControllerTest {

    /** Mock MVC for testing controller */
    @Autowired
    private MockMvc        mvc;

    /** Reference to user repository */
    @Autowired
    private UserRepository userRepository;

    /**
     * Clears the user repository before each test
     *
     * @throws Exception
     *             if error
     */
    @BeforeEach
    public void setUp () throws Exception {
        for ( final User u : userRepository.findAll() ) {
            if ( u.getRole() != Role.ADMIN ) {
                userRepository.delete( u );
            }
        }
    }

    /**
     * Test to create a user
     *
     * @throws Exception
     *             if error
     */
    @Test
    @WithMockUser ( username = "admin", roles = "ADMIN" )
    void testCreateUser () throws Exception {
        final UserDto userDto1 = new UserDto( 0L, "Karthik Nandakumar", "knandak", "knandak@ncsu.edu", "cqhavhhv",
                Role.MANAGER );
        final UserDto userDto2 = new UserDto( 1L, "Ryan Hinshaw", "rthinsha", "rthinsha@ncsu.edu", "ndsofbsjnd",
                Role.BARISTA );

        // Valid user creation
        mvc.perform( post( "/api/users" ).contentType( MediaType.APPLICATION_JSON )
                .content( TestUtils.asJsonString( userDto1 ) ).accept( MediaType.APPLICATION_JSON ) )
                .andExpect( status().isCreated() ).andExpect( jsonPath( "$.username" ).value( "knandak" ) );

        // Test duplicate username
        mvc.perform( post( "/api/users" ).contentType( MediaType.APPLICATION_JSON )
                .content( TestUtils.asJsonString( userDto1 ) ).accept( MediaType.APPLICATION_JSON ) )
                .andExpect( status().isConflict() );

        // Test invalid email
        userDto2.setEmail( "invalidEmail" );
        mvc.perform( post( "/api/users" ).contentType( MediaType.APPLICATION_JSON )
                .content( TestUtils.asJsonString( userDto2 ) ).accept( MediaType.APPLICATION_JSON ) )
                .andExpect( status().isBadRequest() );
    }

    /**
     * Tests to get a user
     *
     * @throws Exception
     *             if error
     */
    @Test
    @WithMockUser ( username = "admin", roles = "ADMIN" )
    void testGetUser () throws Exception {
        final UserDto userDto = new UserDto( 0L, "Karthik Nandakumar", "knandak", "knandak@ncsu.edu", "cqhavhhv",
                Role.MANAGER );
        final UserDto userDto2 = new UserDto( 1L, "Ryan Hinshaw", "rthinsha", "rthinsha@ncsu.edu", "ndsofbsjnd",
                Role.BARISTA );
        mvc.perform( post( "/api/users" ).contentType( MediaType.APPLICATION_JSON )
                .content( TestUtils.asJsonString( userDto ) ).accept( MediaType.APPLICATION_JSON ) )
                .andExpect( status().isCreated() );
        mvc.perform( post( "/api/users" ).contentType( MediaType.APPLICATION_JSON )
                .content( TestUtils.asJsonString( userDto2 ) ).accept( MediaType.APPLICATION_JSON ) )
                .andExpect( status().isCreated() );
        mvc.perform( get( "/api/users/{id}", userRepository.findByUsername( "knandak" ).get().getId() ) )
                .andExpect( status().isOk() ).andExpect( jsonPath( "$.username" ).value( "knandak" ) );
    }

    /**
     * Tests to update a user
     *
     * @throws Exception
     *             if error
     */
    @Test
    @WithMockUser ( username = "admin", roles = "ADMIN" )
    void testUpdateUser () throws Exception {
        final UserDto userDto = new UserDto( 1L, "Karthik Nandakumar", "knandak", "knandak@ncsu.edu", "cqhavhhv",
                Role.MANAGER );

        mvc.perform( post( "/api/users" ).contentType( MediaType.APPLICATION_JSON )
                .content( TestUtils.asJsonString( userDto ) ).accept( MediaType.APPLICATION_JSON ) )
                .andExpect( status().isCreated() );

        userDto.setUsername( "karthik" );
        userDto.setEmail( "newemail@ncsu.edu" );

        mvc.perform( put( "/api/users/{id}", userRepository.findByUsername( "knandak" ).get().getId() )
                .contentType( MediaType.APPLICATION_JSON ).content( TestUtils.asJsonString( userDto ) )
                .accept( MediaType.APPLICATION_JSON ) ).andExpect( status().isOk() )
                .andExpect( jsonPath( "$.email" ).value( "newemail@ncsu.edu" ) );
    }

    /**
     * Tests to delete a user
     *
     * @throws Exception
     *             if error
     */
    @Test
    @WithMockUser ( username = "admin", roles = "ADMIN" )
    void testDeleteUser () throws Exception {
        final UserDto userDto = new UserDto( 1L, "Karthik Nandakumar", "knandak", "knandak@ncsu.edu", "cqhavhhv",
                Role.MANAGER );

        mvc.perform( post( "/api/users" ).contentType( MediaType.APPLICATION_JSON )
                .content( TestUtils.asJsonString( userDto ) ).accept( MediaType.APPLICATION_JSON ) )
                .andExpect( status().isCreated() );
        final Long id = userRepository.findByUsername( "knandak" ).get().getId();
        mvc.perform( delete( "/api/users/{id}", id ) ).andExpect( status().isOk() );

        mvc.perform( get( "/api/users/{id}", id ) ).andExpect( status().isNotFound() );
    }

    /**
     * Tests to get a list of users
     *
     * @throws Exception
     *             if error
     */
    @Test
    @WithMockUser ( username = "admin", roles = "ADMIN" )
    void testGetUsersList () throws Exception {
        mvc.perform( get( "/api/users" ) ).andExpect( status().isOk() );
    }
}
