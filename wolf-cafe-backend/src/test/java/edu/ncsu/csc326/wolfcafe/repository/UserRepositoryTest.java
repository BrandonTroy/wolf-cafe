package edu.ncsu.csc326.wolfcafe.repository;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.security.test.context.support.WithMockUser;

import edu.ncsu.csc326.wolfcafe.dto.UserDto;
import edu.ncsu.csc326.wolfcafe.entity.Role;
import edu.ncsu.csc326.wolfcafe.entity.User;
import edu.ncsu.csc326.wolfcafe.mapper.UserMapper;

/**
 * Tests for user repository
 * 
 * @author Karthik Nandakumar
 */
@DataJpaTest
@AutoConfigureTestDatabase(replace = Replace.NONE)
public class UserRepositoryTest {

	/** Reference to user repository */
	@Autowired
	private UserRepository userRepository;

	// user dto
	private UserDto userDto1;

	// user dto
	private UserDto userDto2;

	/** Sets up the test case */
	@BeforeEach
	@WithMockUser(username = "admin", roles = "ADMIN")
	public void setUp() {
		userRepository.deleteAll(); // Clear the repository before each test

		// Create and save test users
		User user1 = new User(1, "Karthik Nandakumar", "knandak", "knandak@ncsu.edu", "cqhavhhv",
				new Role(1L, "MANAGER"));
		User user2 = new User(2, "Ryan Hinshaw", "rthinsha", "rthinsha@ncsu.edu", "ndsofbsjnd", new Role(2L, "STAFF"));

		User savedUser1 = userRepository.save(user1);
		User savedUser2 = userRepository.save(user2);
		userDto1 = UserMapper.mapToUserDto(savedUser1);
		userDto2 = UserMapper.mapToUserDto(savedUser2);
	}

	/**
	 * Tests find by username
	 */
	@Test
	void testFindByUsername() {
		Optional<User> user = userRepository.findByUsername("knandak");
		User actualUser = user.get();
		assertAll("User contents", () -> assertEquals("Karthik Nandakumar", actualUser.getName()));

		user = userRepository.findByUsername("rthinsha");
		User actualUser2 = user.get();
		assertAll("User contents", () -> assertEquals("Ryan Hinshaw", actualUser2.getName()));
	}

	/**
	 * Tests find by id
	 */
	@Test
	void testFindById() {
		Optional<User> user = userRepository.findById(userDto1.getId());
		User actualUser = user.get();
		assertAll("User contents", () -> assertEquals("Karthik Nandakumar", actualUser.getName()));

		user = userRepository.findById(userDto2.getId());
		User actualUser2 = user.get();
		assertAll("User contents", () -> assertEquals("Ryan Hinshaw", actualUser2.getName()));
	}

	/**
	 * Tests find by email
	 */
	@Test
	void testFindByEmail() {
		Optional<User> user = userRepository.findByEmail("knandak@ncsu.edu");
		User actualUser = user.get();
		assertAll("User contents", () -> assertEquals("Karthik Nandakumar", actualUser.getName()));

		user = userRepository.findByEmail("rthinsha@ncsu.edu");
		User actualUser2 = user.get();
		assertAll("User contents", () -> assertEquals("Ryan Hinshaw", actualUser2.getName()));
	}
}
