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
@Table ( name = "users" )
public class User {
    /** user id */
    @Id
    @GeneratedValue ( strategy = GenerationType.IDENTITY )
    private Long   id;
    
    /** user's name */
    private String name;
    
    /** unique username for login */
    @Column ( nullable = false, unique = true )
    private String username;
    
    /** user's email */
    @Column ( nullable = false, unique = true )
    private String email;
    
    /** user's password for login */
    @Column ( nullable = false )
    private String password;

    /** user's role controlling permissions */
    @Column ( nullable = false )
    private Role   role;

    /**
     * Constructs a user dto object
     *
     * @param role
     *            user role
     */
    public User ( final Role role ) {
        this.role = role;
    }

    /**
     * Constructs a user dto object
     *
     * @param id
     *            the user id
     * @param name
     *            the user's name
     * @param username
     *            the username
     * @param email
     *            the user email
     * @param password
     *            the user password
     * @param role
     *            the user role
     */
    public User ( final long id, final String name, final String username, final String email, final String password,
            final Role role ) {
        this.id = id;
        this.name = name;
        this.username = username;
        this.email = email;
        this.password = password;
        this.role = role;
    }
}
