package edu.ncsu.csc326.wolfcafe.service.impl;

import java.security.NoSuchAlgorithmException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
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
	private UserService userService;

	/** user repository */
	@Autowired
	private UserRepository userRepository;

	/** Deletes user repository before each test */
	@BeforeEach
	public void setUp() throws Exception {
		userRepository.deleteAll();
	}

	/**
	 * Tests for create user
	 * 
	 * @throws NoSuchAlgorithmException
	 */
	@Test
	@WithMockUser(username = "admin", roles = "ADMIN")
	void testCreateUser() throws NoSuchAlgorithmException {
		final UserDto userDto = new UserDto(1, "Karthik Nandakumar", "knandak", "knandak@ncsu.edu", "cqhavhhv",
				Role.MANAGER);
		final UserDto savedUser = userService.createUser(userDto);
		final UserDto retrievedUser = userService.getUserById(savedUser.getId());
		assertAll("User contents", () -> assertEquals("Karthik Nandakumar", retrievedUser.getName()),
				() -> assertEquals("knandak", retrievedUser.getUsername()),
				() -> assertEquals("knandak@ncsu.edu", retrievedUser.getEmail()),
				() -> assertEquals("ROLE_MANAGER", RoleMapper.toString(retrievedUser.getRole())));
	}

	/**
	 * Tests for get users list
	 * 
	 * @throws NoSuchAlgorithmException
	 */
	@Test
	@WithMockUser(username = "admin", roles = "ADMIN")
	void testGetUsersList() throws NoSuchAlgorithmException {
		final UserDto userDto = new UserDto(1, "Karthik Nandakumar", "knandak", "knandak@ncsu.edu", "cqhavhhv",
				Role.MANAGER);

		assertEquals(0, userService.getUsersList().size());
		final UserDto savedUser = userService.createUser(userDto);
		final List<UserDto> userList = userService.getUsersList();
		assertEquals(userList.size(), 1);
		assertAll("User contents", () -> assertEquals("Karthik Nandakumar", savedUser.getName()),
				() -> assertEquals("knandak", savedUser.getUsername()),
				() -> assertEquals("knandak@ncsu.edu", savedUser.getEmail()),
				() -> assertEquals("ROLE_MANAGER", RoleMapper.toString(savedUser.getRole())));
	}

	/**
	 * Tests for update user
	 * 
	 * @throws NoSuchAlgorithmException
	 */
	@Test
	@WithMockUser(username = "admin", roles = "ADMIN")
	void testUpdateUser() throws NoSuchAlgorithmException {
		final UserDto userDto = new UserDto(1, "Karthik Nandakumar", "knandak", "knandak@ncsu.edu", "cqhavhhv",
				Role.MANAGER);
		final UserDto userDto1 = new UserDto(2, "Ryan Hinshaw", "rthinsha", "rthinsha@ncsu.edu", "dsjakkdhfjhf",
				Role.MANAGER);

		assertEquals(0, userService.getUsersList().size());
		final UserDto savedUser = userService.createUser(userDto);
		final UserDto savedUser1 = userService.createUser(userDto1);
		assertAll("User contents", () -> assertEquals("Karthik Nandakumar", savedUser.getName()),
				() -> assertEquals("knandak", savedUser.getUsername()),
				() -> assertEquals("knandak@ncsu.edu", savedUser.getEmail()),
				() -> assertEquals("ROLE_MANAGER", RoleMapper.toString(savedUser.getRole())));
		// Test for updating username, email address, and role
		userDto.setUsername("knandakumar");
		userDto.setEmail("karthik@ncsu.edu");
		userDto.setRole(Role.BARISTA);
		final UserDto updatedUser = userService.updateUser(savedUser.getId(), userDto);
		List<UserDto> userList = userService.getUsersList();
		assertEquals(userList.size(), 2);
		assertAll("User contents", () -> assertEquals("knandakumar", updatedUser.getUsername()),
				() -> assertEquals("karthik@ncsu.edu", updatedUser.getEmail()),
				() -> assertEquals("ROLE_BARISTA", RoleMapper.toString(updatedUser.getRole())));
		// Test for duplicate username
		userDto.setUsername("rthinsha");
		assertTrue(userService.isDuplicateUsername(userDto1.getId(), userDto1.getUsername()));
		Exception e1 = assertThrows(IllegalArgumentException.class,
				() -> userService.updateUser(savedUser.getId(), userDto));
		assertEquals("Duplicate username", e1.getMessage());
		// Test for duplicate email
		userDto.setUsername("knandak");
		userDto.setEmail("rthinsha@ncsu.edu");
		e1 = assertThrows(IllegalArgumentException.class, () -> userService.updateUser(savedUser.getId(), userDto));
		assertEquals("Duplicate email", e1.getMessage());
	}

	/**
	 * Tests for delete user
	 * 
	 * @throws NoSuchAlgorithmException
	 */
	@Test
	@WithMockUser(username = "admin", roles = "ADMIN")
	void testDeleteUser() throws NoSuchAlgorithmException {
		final UserDto userDto = new UserDto(1, "Karthik Nandakumar", "knandak", "knandak@ncsu.edu", "cqhavhhv",
				Role.MANAGER);

		assertEquals(0, userService.getUsersList().size());
		userService.createUser(userDto);
		final List<UserDto> userList = userService.getUsersList();
		assertEquals(userList.size(), 1);
		userService.deleteUser(userList.get(0).getId());
		final List<UserDto> updatedList = userService.getUsersList();
		assertEquals(updatedList.size(), 0);
	}

	/**
	 * Tests for get user by id
	 * 
	 * @throws NoSuchAlgorithmException
	 */
	@Test
	@WithMockUser(username = "admin", roles = "ADMIN")
	void testGetUserById() throws NoSuchAlgorithmException {
		final UserDto userDto = new UserDto(1, "Karthik Nandakumar", "knandak", "knandak@ncsu.edu", "cqhavhhv",
				Role.MANAGER);
		final UserDto savedUser = userService.createUser(userDto);
		assertAll("User contents", () -> assertEquals("Karthik Nandakumar", savedUser.getName()),
				() -> assertEquals("knandak", savedUser.getUsername()),
				() -> assertEquals("knandak@ncsu.edu", savedUser.getEmail()),
				() -> assertEquals("ROLE_MANAGER", RoleMapper.toString(savedUser.getRole())));

		final UserDto retrievedUser = userService.getUserById(savedUser.getId());
		assertAll("User contents", () -> assertEquals("Karthik Nandakumar", retrievedUser.getName()),
				() -> assertEquals("knandak", retrievedUser.getUsername()),
				() -> assertEquals("knandak@ncsu.edu", retrievedUser.getEmail()),
				() -> assertEquals("ROLE_MANAGER", RoleMapper.toString(retrievedUser.getRole())));
	}

	/**
	 * Tests for get user by username
	 * 
	 * @throws NoSuchAlgorithmException
	 */
	@Test
	@WithMockUser(username = "admin", roles = "ADMIN")
	void testGetUserByUsername() throws NoSuchAlgorithmException {
		final UserDto userDto = new UserDto(1, "Karthik Nandakumar", "knandak", "knandak@ncsu.edu", "cqhavhhv",
				Role.MANAGER);
		final UserDto savedUser = userService.createUser(userDto);
		assertAll("User contents", () -> assertEquals("Karthik Nandakumar", savedUser.getName()),
				() -> assertEquals("knandak", savedUser.getUsername()),
				() -> assertEquals("knandak@ncsu.edu", savedUser.getEmail()),
				() -> assertEquals("ROLE_MANAGER", RoleMapper.toString(savedUser.getRole())));

		final UserDto retrievedUser = userService.getUserByUsername(savedUser.getUsername());
		assertAll("User contents", () -> assertEquals("Karthik Nandakumar", retrievedUser.getName()),
				() -> assertEquals("knandak", retrievedUser.getUsername()),
				() -> assertEquals("knandak@ncsu.edu", retrievedUser.getEmail()),
				() -> assertEquals("ROLE_MANAGER", RoleMapper.toString(retrievedUser.getRole())));
	}

	/**
	 * Tests for get user by email
	 * 
	 * @throws NoSuchAlgorithmException
	 */
	@Test
	@WithMockUser(username = "admin", roles = "ADMIN")
	void testGetUserByEmail() throws NoSuchAlgorithmException {
		final UserDto userDto = new UserDto(1, "Karthik Nandakumar", "knandak", "knandak@ncsu.edu", "cqhavhhv",
				Role.MANAGER);
		final UserDto savedUser = userService.createUser(userDto);
		assertAll("User contents", () -> assertEquals("Karthik Nandakumar", savedUser.getName()),
				() -> assertEquals("knandak", savedUser.getUsername()),
				() -> assertEquals("knandak@ncsu.edu", savedUser.getEmail()),
				() -> assertEquals("ROLE_MANAGER", RoleMapper.toString(savedUser.getRole())));

		final UserDto retrievedUser = userService.getUserByEmail(savedUser.getEmail());
		assertAll("User contents", () -> assertEquals("Karthik Nandakumar", retrievedUser.getName()),
				() -> assertEquals("knandak", retrievedUser.getUsername()),
				() -> assertEquals("knandak@ncsu.edu", retrievedUser.getEmail()),
				() -> assertEquals("ROLE_MANAGER", RoleMapper.toString(retrievedUser.getRole())));
	}

}
