package edu.ncsu.csc326.wolfcafe.service.impl;

import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import edu.ncsu.csc326.wolfcafe.dto.UserDto;
import edu.ncsu.csc326.wolfcafe.entity.Role;
import edu.ncsu.csc326.wolfcafe.entity.User;
import edu.ncsu.csc326.wolfcafe.repository.UserRepository;
import edu.ncsu.csc326.wolfcafe.service.UserService;

import org.junit.jupiter.api.Test;

@SpringBootTest
public class UserServiceImplTest {

	@Autowired
	private UserService userService;

	@Autowired
	private UserRepository userRepository;

	@BeforeEach
	public void setUp() throws Exception {
		userRepository.deleteAll();
	}

	@Test
	void testCreateUser() {
		UserDto userDto = new UserDto(1, "Karthik Nandakumar", "knandak", "knandak@ncsu.edu", "cqhavhhv",
				new Role(1L, "MANAGER"));
		UserDto savedUser = userService.createUser(userDto);
		assertAll("User contents", () -> assertEquals("Karthik Nandakumar", savedUser.getName()),
				() -> assertEquals("knandak", savedUser.getUsername()),
				() -> assertEquals("knandak@ncsu.edu", savedUser.getEmail()),
				() -> assertEquals("cqhavhhv", savedUser.getPassword()),
				() -> assertEquals(new Role(1L, "MANAGER"), savedUser.getRole()));

		UserDto retrievedUser = userService.getUserById(savedUser.getId());
		assertAll("User contents", () -> assertEquals("Karthik Nandakumar", retrievedUser.getName()),
				() -> assertEquals("knandak", retrievedUser.getUsername()),
				() -> assertEquals("knandak@ncsu.edu", retrievedUser.getEmail()),
				() -> assertEquals("cqhavhhv", retrievedUser.getPassword()),
				() -> assertEquals(new Role(1L, "MANAGER"), retrievedUser.getRole()));
	}

	@Test
	void testGetUsersList() {
		UserDto userDto = new UserDto(1, "Karthik Nandakumar", "knandak", "knandak@ncsu.edu", "cqhavhhv",
				new Role(1L, "MANAGER"));

		assertEquals(0, userService.getUsersList().size());
		UserDto savedUser = userService.createUser(userDto);
		List<UserDto> userList = userService.getUsersList();
		assertEquals(userList.size(), 1);
		assertAll("User contents", () -> assertEquals("Karthik Nandakumar", savedUser.getName()),
				() -> assertEquals("knandak", savedUser.getUsername()),
				() -> assertEquals("knandak@ncsu.edu", savedUser.getEmail()),
				() -> assertEquals("cqhavhhv", savedUser.getPassword()),
				() -> assertEquals(new Role(1L, "MANAGER"), savedUser.getRole()));
	}

	@Test
	void testUpdateUser() {
		UserDto userDto = new UserDto(1, "Karthik Nandakumar", "knandak", "knandak@ncsu.edu", "cqhavhhv",
				new Role(1L, "MANAGER"));

		assertEquals(0, userService.getUsersList().size());
		userDto.setRole(new Role(3L, "CUSTOMER"));

		UserDto savedUser = userService.createUser(userDto);
		List<UserDto> userList = userService.getUsersList();
		assertEquals(userList.size(), 1);
		assertAll("User contents", () -> assertEquals("Karthik Nandakumar", savedUser.getName()),
				() -> assertEquals("knandak", savedUser.getUsername()),
				() -> assertEquals("knandak@ncsu.edu", savedUser.getEmail()),
				() -> assertEquals("cqhavhhv", savedUser.getPassword()),
				() -> assertEquals(new Role(3L, "CUSTOMER"), savedUser.getRole()));
	}

	@Test
	void testDeleteUser() {
		UserDto userDto = new UserDto(1, "Karthik Nandakumar", "knandak", "knandak@ncsu.edu", "cqhavhhv",
				new Role(1L, "MANAGER"));

		assertEquals(0, userService.getUsersList().size());
		userService.createUser(userDto);
		List<UserDto> userList = userService.getUsersList();
		assertEquals(userList.size(), 1);
		userService.deleteUser(1L);
		List<UserDto> updatedList = userService.getUsersList();
		assertEquals(updatedList.size(), 0);
	}

	@Test
	void testGetUserById() {
		UserDto userDto = new UserDto(1, "Karthik Nandakumar", "knandak", "knandak@ncsu.edu", "cqhavhhv",
				new Role(1L, "MANAGER"));
		UserDto savedUser = userService.createUser(userDto);
		assertAll("User contents", () -> assertEquals("Karthik Nandakumar", savedUser.getName()),
				() -> assertEquals("knandak", savedUser.getUsername()),
				() -> assertEquals("knandak@ncsu.edu", savedUser.getEmail()),
				() -> assertEquals("cqhavhhv", savedUser.getPassword()),
				() -> assertEquals(new Role(1L, "MANAGER"), savedUser.getRole()));

		UserDto retrievedUser = userService.getUserById(savedUser.getId());
		assertAll("User contents", () -> assertEquals("Karthik Nandakumar", retrievedUser.getName()),
				() -> assertEquals("knandak", retrievedUser.getUsername()),
				() -> assertEquals("knandak@ncsu.edu", retrievedUser.getEmail()),
				() -> assertEquals("cqhavhhv", retrievedUser.getPassword()),
				() -> assertEquals(new Role(1L, "MANAGER"), retrievedUser.getRole()));
	}

	@Test
	void testGetUserByUsername() {
		UserDto userDto = new UserDto(1, "Karthik Nandakumar", "knandak", "knandak@ncsu.edu", "cqhavhhv",
				new Role(1L, "MANAGER"));
		UserDto savedUser = userService.createUser(userDto);
		assertAll("User contents", () -> assertEquals("Karthik Nandakumar", savedUser.getName()),
				() -> assertEquals("knandak", savedUser.getUsername()),
				() -> assertEquals("knandak@ncsu.edu", savedUser.getEmail()),
				() -> assertEquals("cqhavhhv", savedUser.getPassword()),
				() -> assertEquals(new Role(1L, "MANAGER"), savedUser.getRole()));

		UserDto retrievedUser = userService.getUserByUsername(savedUser.getUsername());
		assertAll("User contents", () -> assertEquals("Karthik Nandakumar", retrievedUser.getName()),
				() -> assertEquals("knandak", retrievedUser.getUsername()),
				() -> assertEquals("knandak@ncsu.edu", retrievedUser.getEmail()),
				() -> assertEquals("cqhavhhv", retrievedUser.getPassword()),
				() -> assertEquals(new Role(1L, "MANAGER"), retrievedUser.getRole()));
	}

	@Test
	void testGetUserByEmail() {
		UserDto userDto = new UserDto(1, "Karthik Nandakumar", "knandak", "knandak@ncsu.edu", "cqhavhhv",
				new Role(1L, "MANAGER"));
		UserDto savedUser = userService.createUser(userDto);
		assertAll("User contents", () -> assertEquals("Karthik Nandakumar", savedUser.getName()),
				() -> assertEquals("knandak", savedUser.getUsername()),
				() -> assertEquals("knandak@ncsu.edu", savedUser.getEmail()),
				() -> assertEquals("cqhavhhv", savedUser.getPassword()),
				() -> assertEquals(new Role(1L, "MANAGER"), savedUser.getRole()));

		UserDto retrievedUser = userService.getUserByEmail(savedUser.getEmail());
		assertAll("User contents", () -> assertEquals("Karthik Nandakumar", retrievedUser.getName()),
				() -> assertEquals("knandak", retrievedUser.getUsername()),
				() -> assertEquals("knandak@ncsu.edu", retrievedUser.getEmail()),
				() -> assertEquals("cqhavhhv", retrievedUser.getPassword()),
				() -> assertEquals(new Role(1L, "MANAGER"), retrievedUser.getRole()));
	}

}
