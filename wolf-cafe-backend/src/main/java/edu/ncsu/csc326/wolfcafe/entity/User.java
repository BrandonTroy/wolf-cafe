package edu.ncsu.csc326.wolfcafe.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Collection;

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
public class User {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	// user id
	private Long id;
	// user's name
	private String name;
	@Column(nullable = false, unique = true)
	// username
	private String username;
	@Column(nullable = false, unique = true)
	// user email
	private String email;
	@Column(nullable = false)
	// user password
	private String password;
	@ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
	@JoinTable(name = "users_roles", joinColumns = @JoinColumn(name = "user_id", referencedColumnName = "id"), inverseJoinColumns = @JoinColumn(name = "role_id", referencedColumnName = "id"))
	// user roles
	private Collection<Role> roles;

	@ManyToOne(fetch = FetchType.EAGER, cascade = { CascadeType.PERSIST, CascadeType.MERGE })
	@JoinColumn(name = "role_id", referencedColumnName = "id")
	// user role
	private Role role;

	/**
	 * Constructs a user dto object
	 * 
	 * @param role user role
	 */
	public User(Role role) {
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
	public User(long id, String name, String username, String email, String password, Role role) {
		this.id = id;
		this.name = name;
		this.username = username;
		this.email = email;
		this.password = password;
		this.role = role;
	}
}
