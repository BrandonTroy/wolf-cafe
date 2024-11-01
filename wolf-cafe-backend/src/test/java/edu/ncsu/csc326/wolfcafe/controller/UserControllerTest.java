package edu.ncsu.csc326.wolfcafe.controller;

import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import com.jayway.jsonpath.JsonPath;

import edu.ncsu.csc326.wolfcafe.TestUtils;
import edu.ncsu.csc326.wolfcafe.dto.UserDto;
import edu.ncsu.csc326.wolfcafe.entity.Role;
import edu.ncsu.csc326.wolfcafe.repository.UserRepository;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

@SpringBootTest
@AutoConfigureMockMvc
public class UserControllerTest {

	/** Mock MVC for testing controller */
	@Autowired
	private MockMvc mvc;

	/** Reference to user repository */
	@Autowired
	private UserRepository userRepository;

	@BeforeEach
	public void setUp() throws Exception {
		userRepository.deleteAll();
	}

	@Test
	void testCreateUser() throws Exception {
		UserDto userDto1 = new UserDto(1, "Karthik Nandakumar", "knandak", "knandak@ncsu.edu", "cqhavhhv",
				new Role(1L, "MANAGER"));
		UserDto userDto2 = new UserDto(2, "Ryan Hinshaw", "rthinsha", "rthinsha@ncsu.edu", "ndsofbsjnd",
				new Role(2L, "STAFF"));

		// Valid user creation
		mvc.perform(post("/api/users").contentType(MediaType.APPLICATION_JSON).content(TestUtils.asJsonString(userDto1))
				.accept(MediaType.APPLICATION_JSON)).andExpect(status().isCreated())
				.andExpect(jsonPath("$.username").value("knandak"));

		// Test duplicate username
		mvc.perform(post("/api/users").contentType(MediaType.APPLICATION_JSON).content(TestUtils.asJsonString(userDto1))
				.accept(MediaType.APPLICATION_JSON)).andExpect(status().isConflict());

		// Test invalid email
		userDto2.setEmail("invalidEmail");
		mvc.perform(post("/api/users").contentType(MediaType.APPLICATION_JSON).content(TestUtils.asJsonString(userDto2))
				.accept(MediaType.APPLICATION_JSON)).andExpect(status().isBadRequest());
	}

	@Test
	void testGetUser() throws Exception {
		UserDto userDto = new UserDto(1, "Karthik Nandakumar", "knandak", "knandak@ncsu.edu", "cqhavhhv",
				new Role(1L, "MANAGER"));

		mvc.perform(post("/api/users").contentType(MediaType.APPLICATION_JSON).content(TestUtils.asJsonString(userDto))
				.accept(MediaType.APPLICATION_JSON)).andExpect(status().isCreated());

		mvc.perform(get("/api/users/1")).andExpect(status().isOk()).andExpect(jsonPath("$.username").value("knandak"));
	}

	@Test
	void testUpdateUser() throws Exception {
		UserDto userDto = new UserDto(1, "Karthik Nandakumar", "knandak", "knandak@ncsu.edu", "cqhavhhv",
				new Role(1L, "MANAGER"));

		mvc.perform(post("/api/users").contentType(MediaType.APPLICATION_JSON).content(TestUtils.asJsonString(userDto))
				.accept(MediaType.APPLICATION_JSON)).andExpect(status().isCreated());

		userDto.setEmail("newemail@ncsu.edu");

		mvc.perform(put("/api/users/1").contentType(MediaType.APPLICATION_JSON).content(TestUtils.asJsonString(userDto))
				.accept(MediaType.APPLICATION_JSON)).andExpect(status().isOk())
				.andExpect(jsonPath("$.email").value("newemail@ncsu.edu"));
	}

	@Test
	void testDeleteUser() throws Exception {
		UserDto userDto = new UserDto(1, "Karthik Nandakumar", "knandak", "knandak@ncsu.edu", "cqhavhhv",
				new Role(1L, "MANAGER"));

		mvc.perform(post("/api/users").contentType(MediaType.APPLICATION_JSON).content(TestUtils.asJsonString(userDto))
				.accept(MediaType.APPLICATION_JSON)).andExpect(status().isCreated());

		mvc.perform(delete("/api/users/1")).andExpect(status().isOk());

		mvc.perform(get("/api/users/1")).andExpect(status().isNotFound());
	}

	@Test
	void testGetUsersList() throws Exception {
		mvc.perform(get("/api/users")).andExpect(status().isOk());
	}
}
