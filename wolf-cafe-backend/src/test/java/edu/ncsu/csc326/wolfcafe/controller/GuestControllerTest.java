package edu.ncsu.csc326.wolfcafe.controller;

import static org.junit.jupiter.api.Assertions.fail;
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
public class GuestControllerTest {

	@Test
	@WithMockUser(username = "guest", roles = "GUEST")
	void testContinueAsGuest() {
		fail("Not yet implemented");
	}

	@Test
	@WithMockUser(username = "guest", roles = "GUEST")
	void testPlaceOrder() {
		fail("Not yet implemented");
	}

	@Test
	@WithMockUser(username = "guest", roles = "GUEST")
	void testGetOrderHistory() {
		fail("Not yet implemented");
	}

}
