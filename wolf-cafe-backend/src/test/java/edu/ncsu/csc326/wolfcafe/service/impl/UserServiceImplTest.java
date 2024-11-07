package edu.ncsu.csc326.wolfcafe.service.impl;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;

import edu.ncsu.csc326.wolfcafe.dto.UserDto;
import edu.ncsu.csc326.wolfcafe.entity.Role;
import edu.ncsu.csc326.wolfcafe.mapper.RoleMapper;
import edu.ncsu.csc326.wolfcafe.repository.UserRepository;
import edu.ncsu.csc326.wolfcafe.service.UserService;

/**
 * Tests for user service implementation
 *
 * @author Karthik Nandakumar
 */
@SpringBootTest
@AutoConfigureMockMvc
public class UserServiceImplTest {

    /** user service */
    @Autowired
    private UserService    userService;

    /** user repository */
    @Autowired
    private UserRepository userRepository;

    /** Deletes user repository before each test */
    @BeforeEach
    public void setUp () throws Exception {
        userRepository.deleteAll();
    }

    /**
     * Tests for create user
     */
    @Test
    @WithMockUser ( username = "admin", roles = "ADMIN" )
    void testCreateUser () {
        final UserDto userDto = new UserDto( 1, "Karthik Nandakumar", "knandak", "knandak@ncsu.edu", "cqhavhhv",
                Role.MANAGER );
        final UserDto savedUser = userService.createUser( userDto );
        final UserDto retrievedUser = userService.getUserById( savedUser.getId() );
        assertAll( "User contents", () -> assertEquals( "Karthik Nandakumar", retrievedUser.getName() ),
                () -> assertEquals( "knandak", retrievedUser.getUsername() ),
                () -> assertEquals( "knandak@ncsu.edu", retrievedUser.getEmail() ),
                () -> assertEquals( "cqhavhhv", retrievedUser.getPassword() ),
                () -> assertEquals( "ROLE_MANAGER", RoleMapper.toString( retrievedUser.getRole() ) ) );
    }

    /**
     * Tests for get users list
     */
    @Test
    @WithMockUser ( username = "admin", roles = "ADMIN" )
    void testGetUsersList () {
        final UserDto userDto = new UserDto( 1, "Karthik Nandakumar", "knandak", "knandak@ncsu.edu", "cqhavhhv",
                Role.MANAGER );

        assertEquals( 0, userService.getUsersList().size() );
        final UserDto savedUser = userService.createUser( userDto );
        final List<UserDto> userList = userService.getUsersList();
        assertEquals( userList.size(), 1 );
        assertAll( "User contents", () -> assertEquals( "Karthik Nandakumar", savedUser.getName() ),
                () -> assertEquals( "knandak", savedUser.getUsername() ),
                () -> assertEquals( "knandak@ncsu.edu", savedUser.getEmail() ),
                () -> assertEquals( "cqhavhhv", savedUser.getPassword() ),
                () -> assertEquals( "ROLE_MANAGER", RoleMapper.toString( savedUser.getRole() ) ) );
    }

    /**
     * Tests for update user
     */
    @Test
    @WithMockUser ( username = "admin", roles = "ADMIN" )
    void testUpdateUser () {
        final UserDto userDto = new UserDto( 1, "Karthik Nandakumar", "knandak", "knandak@ncsu.edu", "cqhavhhv",
                Role.MANAGER );

        assertEquals( 0, userService.getUsersList().size() );
        final UserDto savedUser = userService.createUser( userDto );
        assertAll( "User contents", () -> assertEquals( "Karthik Nandakumar", savedUser.getName() ),
                () -> assertEquals( "knandak", savedUser.getUsername() ),
                () -> assertEquals( "knandak@ncsu.edu", savedUser.getEmail() ),
                () -> assertEquals( "cqhavhhv", savedUser.getPassword() ),
                () -> assertEquals( "ROLE_MANAGER", RoleMapper.toString( savedUser.getRole() ) ) );
        userDto.setUsername( "knandakumar" );
        final UserDto updatedUser = userService.updateUser( savedUser.getId(), userDto );
        final List<UserDto> userList = userService.getUsersList();
        System.out.println( userList.get( 0 ).getUsername() );
        assertEquals( userList.size(), 1 );
        assertAll( "User contents", () -> assertEquals( "Karthik Nandakumar", updatedUser.getName() ),
                () -> assertEquals( "knandakumar", updatedUser.getUsername() ),
                () -> assertEquals( "knandak@ncsu.edu", updatedUser.getEmail() ),
                () -> assertEquals( "cqhavhhv", updatedUser.getPassword() ),
                () -> assertEquals( "ROLE_MANAGER", RoleMapper.toString( updatedUser.getRole() ) ) );
    }

    /**
     * Tests for delete user
     */
    @Test
    @WithMockUser ( username = "admin", roles = "ADMIN" )
    void testDeleteUser () {
        final UserDto userDto = new UserDto( 1, "Karthik Nandakumar", "knandak", "knandak@ncsu.edu", "cqhavhhv",
                Role.MANAGER );

        assertEquals( 0, userService.getUsersList().size() );
        userService.createUser( userDto );
        final List<UserDto> userList = userService.getUsersList();
        assertEquals( userList.size(), 1 );
        userService.deleteUser( userList.get( 0 ).getId() );
        final List<UserDto> updatedList = userService.getUsersList();
        assertEquals( updatedList.size(), 0 );
    }

    /**
     * Tests for get user by id
     */
    @Test
    @WithMockUser ( username = "admin", roles = "ADMIN" )
    void testGetUserById () {
        final UserDto userDto = new UserDto( 1, "Karthik Nandakumar", "knandak", "knandak@ncsu.edu", "cqhavhhv",
                Role.MANAGER );
        final UserDto savedUser = userService.createUser( userDto );
        assertAll( "User contents", () -> assertEquals( "Karthik Nandakumar", savedUser.getName() ),
                () -> assertEquals( "knandak", savedUser.getUsername() ),
                () -> assertEquals( "knandak@ncsu.edu", savedUser.getEmail() ),
                () -> assertEquals( "cqhavhhv", savedUser.getPassword() ),
                () -> assertEquals( "ROLE_MANAGER", RoleMapper.toString( savedUser.getRole() ) ) );

        final UserDto retrievedUser = userService.getUserById( savedUser.getId() );
        assertAll( "User contents", () -> assertEquals( "Karthik Nandakumar", retrievedUser.getName() ),
                () -> assertEquals( "knandak", retrievedUser.getUsername() ),
                () -> assertEquals( "knandak@ncsu.edu", retrievedUser.getEmail() ),
                () -> assertEquals( "cqhavhhv", retrievedUser.getPassword() ),
                () -> assertEquals( "ROLE_MANAGER", RoleMapper.toString( retrievedUser.getRole() ) ) );
    }

    /**
     * Tests for get user by username
     */
    @Test
    @WithMockUser ( username = "admin", roles = "ADMIN" )
    void testGetUserByUsername () {
        final UserDto userDto = new UserDto( 1, "Karthik Nandakumar", "knandak", "knandak@ncsu.edu", "cqhavhhv",
                Role.MANAGER );
        final UserDto savedUser = userService.createUser( userDto );
        assertAll( "User contents", () -> assertEquals( "Karthik Nandakumar", savedUser.getName() ),
                () -> assertEquals( "knandak", savedUser.getUsername() ),
                () -> assertEquals( "knandak@ncsu.edu", savedUser.getEmail() ),
                () -> assertEquals( "cqhavhhv", savedUser.getPassword() ),
                () -> assertEquals( "ROLE_MANAGER", RoleMapper.toString( savedUser.getRole() ) ) );

        final UserDto retrievedUser = userService.getUserByUsername( savedUser.getUsername() );
        assertAll( "User contents", () -> assertEquals( "Karthik Nandakumar", retrievedUser.getName() ),
                () -> assertEquals( "knandak", retrievedUser.getUsername() ),
                () -> assertEquals( "knandak@ncsu.edu", retrievedUser.getEmail() ),
                () -> assertEquals( "cqhavhhv", retrievedUser.getPassword() ),
                () -> assertEquals( "ROLE_MANAGER", RoleMapper.toString( retrievedUser.getRole() ) ) );
    }

    /**
     * Tests for get user by email
     */
    @Test
    @WithMockUser ( username = "admin", roles = "ADMIN" )
    void testGetUserByEmail () {
        final UserDto userDto = new UserDto( 1, "Karthik Nandakumar", "knandak", "knandak@ncsu.edu", "cqhavhhv",
                Role.MANAGER );
        final UserDto savedUser = userService.createUser( userDto );
        assertAll( "User contents", () -> assertEquals( "Karthik Nandakumar", savedUser.getName() ),
                () -> assertEquals( "knandak", savedUser.getUsername() ),
                () -> assertEquals( "knandak@ncsu.edu", savedUser.getEmail() ),
                () -> assertEquals( "cqhavhhv", savedUser.getPassword() ),
                () -> assertEquals( "ROLE_MANAGER", RoleMapper.toString( savedUser.getRole() ) ) );

        final UserDto retrievedUser = userService.getUserByEmail( savedUser.getEmail() );
        assertAll( "User contents", () -> assertEquals( "Karthik Nandakumar", retrievedUser.getName() ),
                () -> assertEquals( "knandak", retrievedUser.getUsername() ),
                () -> assertEquals( "knandak@ncsu.edu", retrievedUser.getEmail() ),
                () -> assertEquals( "cqhavhhv", retrievedUser.getPassword() ),
                () -> assertEquals( "ROLE_MANAGER", RoleMapper.toString( retrievedUser.getRole() ) ) );
    }

}
