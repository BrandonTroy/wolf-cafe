package edu.ncsu.csc326.wolfcafe.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import edu.ncsu.csc326.wolfcafe.dto.UserDto;
import edu.ncsu.csc326.wolfcafe.exception.ResourceNotFoundException;
import edu.ncsu.csc326.wolfcafe.service.UserService;
import lombok.AllArgsConstructor;

/**
 * Controller for API endpoints for User management
 */
@RestController
@RequestMapping("api/users")
@AllArgsConstructor
@CrossOrigin("*")
public class UserController {

	/** Link to UserService */
	private final UserService userService;

	@PostMapping
	public ResponseEntity<UserDto> createUser(@RequestBody UserDto userDto) {
		validateUserDto(userDto);

		UserDto savedUserDto = userService.createUser(userDto);
		return ResponseEntity.status(HttpStatus.CREATED).body(savedUserDto);
	}

	@PutMapping("/{id}")
	public ResponseEntity<UserDto> updateUser(@PathVariable("id") long id, @RequestBody UserDto userDto) {
		validateUserDto(userDto);

		UserDto editedUserDto = userService.updateUser(id, userDto);
		return ResponseEntity.ok(editedUserDto);
	}

	private void validateUserDto(UserDto userDto) {
		if (userService.isDuplicateUsername(userDto.getUsername())
				|| userService.isDuplicateEmail(userDto.getEmail())) {
			throw new IllegalArgumentException("Duplicate username or email");
		}

		if (userDto.getUsername().trim().isEmpty() || userDto.getEmail().trim().isEmpty()
				|| userDto.getPassword().length() < 8) {
			throw new IllegalArgumentException("Invalid input format");
		}

		boolean isValidUsername = userDto.getUsername().matches("^[a-zA-Z0-9.]+$");
		boolean isValidEmail = userDto.getEmail().matches("^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$");

		if (!isValidUsername || !isValidEmail) {
			throw new IllegalArgumentException("Invalid format");
		}
	}

	@GetMapping("/{id}")
	public ResponseEntity<UserDto> getUser(@PathVariable long id) {
		UserDto userDto = userService.getUserById(id);
		if (userDto == null) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
		}
		return ResponseEntity.ok(userDto);
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<String> deleteUser(@PathVariable long id) {
		try {
			userService.deleteUser(id);
			return ResponseEntity.ok("User deleted successfully.");
		} catch (ResourceNotFoundException e) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found.");
		}
	}

	@GetMapping
	public ResponseEntity<List<UserDto>> getUsersList() {
		List<UserDto> users = userService.getUsersList();
		return ResponseEntity.ok(users);
	}
}
