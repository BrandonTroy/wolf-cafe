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
	/** The unique id of the User */
	private Long id;
	/** The name of the User */
	private String name;
	/** The unique username of the User */
	private String username;
	/** The email of the User */
	private String email;
	/** The password of the User */
	private String password;
	/** The Role of the User */
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
