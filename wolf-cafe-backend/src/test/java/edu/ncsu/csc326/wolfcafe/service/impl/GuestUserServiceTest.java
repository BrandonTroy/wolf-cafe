package edu.ncsu.csc326.wolfcafe.service.impl;

import java.security.NoSuchAlgorithmException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

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
public class GuestUserServiceTest {

	@Test
	@WithMockUser(username = "guest", roles = "GUEST")
	void testCreateGuestUser() {
		fail("Not yet implemented");
	}

	@Test
	@WithMockUser(username = "guest", roles = "GUEST")
	void testCreateOrderForGuest() {
		fail("Not yet implemented");
	}

	@Test
	@WithMockUser(username = "guest", roles = "GUEST")
	void testGetOrderHistoryForGuest() {
		fail("Not yet implemented");
	}

	@Test
	@WithMockUser(username = "guest", roles = "GUEST")
	void testGetUser() {
		fail("Not yet implemented");
	}

	@Test
	@WithMockUser(username = "guest", roles = "GUEST")
	void testGetUserById() {
		fail("Not yet implemented");
	}

}
