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
public class GuestDto {
	// User id
	private Long id;
	// Username
	private String username;
	// User role
	private Role role;

	/**
	 * Constructs a user dto object
	 * 
	 * @param role user role
	 */
	public GuestDto(Role role) {
		this.role = role;
	}

	/**
	 * Constructs a user dto object
	 * 
	 * @param id       the user id
	 * @param username the username
	 * @param role     the user role
	 */
	public GuestDto(long id, String username, Role role) {
		this.id = id;
		this.username = username;
		this.role = role;
	}
}
