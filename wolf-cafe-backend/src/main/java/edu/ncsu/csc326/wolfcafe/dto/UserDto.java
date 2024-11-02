package edu.ncsu.csc326.wolfcafe.dto;

import edu.ncsu.csc326.wolfcafe.entity.Role;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Item for data transfer.
 * 
 * @author Karthik Nandakumar
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserDto {
	// User id
	private Long id;
	// User name
	private String name;
	// Username
	private String username;
	// User email
	private String email;
	// User password
	private String password;
	// User role
	private Role role;

	/**
	 * Constructs a user dto object
	 * 
	 * @param role user role
	 */
	public UserDto(Role role) {
		this.role = role;
	}

	/**
	 * Constructs a user dto object
	 * 
	 * @param id       the user id
	 * @param name     the user's name
	 * @param username the username
	 * @param email    the user email
	 * @param password the user password
	 * @param role     the user role
	 */
	public UserDto(long id, String name, String username, String email, String password, Role role) {
		this.id = id;
		this.name = name;
		this.username = username;
		this.email = email;
		this.password = password;
		this.role = role;
	}
}
