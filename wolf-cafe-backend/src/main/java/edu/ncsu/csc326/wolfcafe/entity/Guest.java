package edu.ncsu.csc326.wolfcafe.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * System user.
 *
 * @author Karthik Nandakumar
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "users")
public class Guest {
	/** user id */
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	/** user's name */
	/** unique username for login */

	@Column(nullable = false, unique = true)
	private String username;

	/** user's role controlling permissions */
	@Column(nullable = false)
	private Role role;

	/**
	 * Constructs a user dto object
	 *
	 * @param role user role
	 */
	public Guest(final Role role) {
		this.role = role;
	}

	/**
	 * Constructs a user dto object
	 *
	 * @param id       the user id
	 * @param username the username
	 * @param role     the user role
	 */
	public Guest(final long id, final String username, final Role role) {
		this.id = id;
		this.username = username;
		this.role = role;
	}
}
