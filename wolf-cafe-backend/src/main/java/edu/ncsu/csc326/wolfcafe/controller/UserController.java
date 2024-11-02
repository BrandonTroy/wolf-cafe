package edu.ncsu.csc326.wolfcafe.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import edu.ncsu.csc326.wolfcafe.dto.UserDto;
import edu.ncsu.csc326.wolfcafe.exception.ResourceNotFoundException;
import edu.ncsu.csc326.wolfcafe.service.UserService;
import lombok.AllArgsConstructor;

/**
 * Controller for API endpoints for User management
 * 
 * @author Karthik Nandakumar
 */
@RestController
@RequestMapping("api/users")
@AllArgsConstructor
@CrossOrigin("*")
public class UserController {

	/** Link to UserService */
	private final UserService userService;

	/**
	 * Creates a user
	 * 
	 * @param userDto a user dto
	 * @return a UserDto object
	 */
	@PostMapping
	public ResponseEntity<UserDto> createUser(@RequestBody UserDto userDto) {
		if (userService.isDuplicateUsername(userDto.getUsername())
				|| userService.isDuplicateEmail(userDto.getEmail())) {
			System.err.println("Duplicate username or email");
			return ResponseEntity.status(HttpStatus.CONFLICT).body(userDto);
		}

		if (userDto.getUsername().trim().isEmpty() || userDto.getEmail().trim().isEmpty()
				|| userDto.getPassword().length() < 8) {
			System.err.println("Invalid input format");
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(userDto);
		}

		boolean isValidUsername = userDto.getUsername().matches("^[a-zA-Z0-9.]+$");
		boolean isValidEmail = userDto.getEmail().matches("^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$");

		if (!isValidUsername || !isValidEmail) {
			System.err.println("Invalid format");
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(userDto);
		}

		UserDto savedUserDto = userService.createUser(userDto);
		return ResponseEntity.status(HttpStatus.CREATED).body(savedUserDto);
	}

	/**
	 * Updates a user
	 * 
	 * @param id      id of the user dto
	 * @param userDto the user dto
	 * @return an updated user dto
	 */
	@PutMapping("/{id}")
	public ResponseEntity<UserDto> updateUser(@PathVariable("id") long id, @RequestBody UserDto userDto) {
		if (userService.isDuplicateUsername(userDto.getUsername())
				|| userService.isDuplicateEmail(userDto.getEmail())) {
			System.err.println("Duplicate username or email");
			return ResponseEntity.status(HttpStatus.CONFLICT).body(userDto);
		}

		if (userDto.getUsername().trim().isEmpty() || userDto.getEmail().trim().isEmpty()
				|| userDto.getPassword().length() < 8) {
			System.err.println("Invalid input format");
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(userDto);
		}

		boolean isValidUsername = userDto.getUsername().matches("^[a-zA-Z0-9.]+$");
		boolean isValidEmail = userDto.getEmail().matches("^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$");

		if (!isValidUsername || !isValidEmail) {
			System.err.println("Invalid format");
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(userDto);
		}

		UserDto editedUserDto = userService.updateUser(id, userDto);
		return ResponseEntity.ok(editedUserDto);
	}

	/**
	 * Gets a user dto with the given id
	 * 
	 * @param id the id of the user dto to find
	 * @return a user dto
	 */
	@GetMapping("/{id}")
	public ResponseEntity<UserDto> getUser(@PathVariable long id) {
		UserDto userDto = userService.getUserById(id);
		if (userDto == null) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
		}
		return ResponseEntity.ok(userDto);
	}

	/**
	 * Deletes a user dto with the given id
	 * 
	 * @param id the id of the user dto to be deleted
	 * @return status
	 */
	@DeleteMapping("/{id}")
	public ResponseEntity<String> deleteUser(@PathVariable long id) {
		try {
			userService.deleteUser(id);
			return ResponseEntity.ok("User deleted successfully.");
		} catch (ResourceNotFoundException e) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found.");
		}
	}

	/**
	 * Returns a list of user dtos
	 * 
	 * @return a list of user dtos
	 */
	@GetMapping
	public ResponseEntity<List<UserDto>> getUsersList() {
		List<UserDto> users = userService.getUsersList();
		return ResponseEntity.ok(users);
	}
}
