package edu.ncsu.csc326.wolfcafe.repository;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import edu.ncsu.csc326.wolfcafe.entity.Role;
import edu.ncsu.csc326.wolfcafe.entity.User;

@DataJpaTest
@AutoConfigureTestDatabase(replace = Replace.NONE)
public class UserRepositoryTest {

	/** Reference to user repository */
	@Autowired
	private UserRepository userRepository;

	/**
	 * Sets up the test case.
	 * 
	 * @throws java.lang.Exception if error
	 */
	@BeforeEach
	public void setUp() throws Exception {
		userRepository.deleteAll();

		User userDto1 = new User(1, "Karthik Nandakumar", "knandak", "knandak@ncsu.edu", "cqhavhhv",
				new Role(1L, "MANAGER"));
		User userDto2 = new User(2, "Ryan Hinshaw", "rthinsha", "rthinsha@ncsu.edu", "ndsofbsjnd",
				new Role(2L, "STAFF"));

		userRepository.save(userDto1);
		userRepository.save(userDto2);
	}

	@Test
	void testFindByUsername() {
		Optional<User> user = userRepository.findByUsername("knandak");
		User actualUser = user.get();
		assertAll("User contents", () -> assertEquals("Karthik Nandakumar", actualUser.getName()),
				() -> assertEquals("knandak", actualUser.getUsername()),
				() -> assertEquals("knandak@ncsu.edu", actualUser.getEmail()),
				() -> assertEquals("cqhavhhv", actualUser.getPassword()),
				() -> assertEquals(new Role(1L, "MANAGER"), actualUser.getRole()));

		user = userRepository.findByUsername("rthinsha");
		User actualUser2 = user.get();
		assertAll("User contents", () -> assertEquals("Ryan Hinshaw", actualUser2.getName()),
				() -> assertEquals("rthinsha", actualUser2.getUsername()),
				() -> assertEquals("rthinsha@ncsu.edu", actualUser2.getEmail()),
				() -> assertEquals("ndsofbsjnd", actualUser2.getPassword()),
				() -> assertEquals(new Role(2L, "STAFF"), actualUser2.getRole()));
	}

	@Test
	void testFindByIdLong() {
		Optional<User> user = userRepository.findById(1L);
		User actualUser = user.get();
		assertAll("User contents", () -> assertEquals("Karthik Nandakumar", actualUser.getName()),
				() -> assertEquals("knandak", actualUser.getUsername()),
				() -> assertEquals("knandak@ncsu.edu", actualUser.getEmail()),
				() -> assertEquals("cqhavhhv", actualUser.getPassword()),
				() -> assertEquals(new Role(1L, "MANAGER"), actualUser.getRole()));

		user = userRepository.findById(2L);
		User actualUser2 = user.get();
		assertAll("User contents", () -> assertEquals("Ryan Hinshaw", actualUser2.getName()),
				() -> assertEquals("rthinsha", actualUser2.getUsername()),
				() -> assertEquals("rthinsha@ncsu.edu", actualUser2.getEmail()),
				() -> assertEquals("ndsofbsjnd", actualUser2.getPassword()),
				() -> assertEquals(new Role(2L, "STAFF"), actualUser2.getRole()));
	}

	@Test
	void testFindByEmail() {
		Optional<User> user = userRepository.findByEmail("knandak@ncsu.edu");
		User actualUser = user.get();
		assertAll("User contents", () -> assertEquals("Karthik Nandakumar", actualUser.getName()),
				() -> assertEquals("knandak", actualUser.getUsername()),
				() -> assertEquals("knandak@ncsu.edu", actualUser.getEmail()),
				() -> assertEquals("cqhavhhv", actualUser.getPassword()),
				() -> assertEquals(new Role(1L, "MANAGER"), actualUser.getRole()));

		user = userRepository.findByEmail("rthinsha@ncsu.edu");
		User actualUser2 = user.get();
		assertAll("User contents", () -> assertEquals("Ryan Hinshaw", actualUser2.getName()),
				() -> assertEquals("rthinsha", actualUser2.getUsername()),
				() -> assertEquals("rthinsha@ncsu.edu", actualUser2.getEmail()),
				() -> assertEquals("ndsofbsjnd", actualUser2.getPassword()),
				() -> assertEquals(new Role(2L, "STAFF"), actualUser2.getRole()));
	}

}
