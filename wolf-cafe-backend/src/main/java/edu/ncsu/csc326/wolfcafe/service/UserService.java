package edu.ncsu.csc326.wolfcafe.service;

import java.util.List;

import edu.ncsu.csc326.wolfcafe.dto.UserDto;
import edu.ncsu.csc326.wolfcafe.exception.ResourceNotFoundException;

/**
 * User service for doing CRUD operations on users
 *
 * @author Karthik Nandakumar
 */
public interface UserService {

    /**
     * Adds given user
     *
     * @param userDto
     *            user to add
     * @return added user
     */
    UserDto createUser ( UserDto userDto );

    /**
     * Gets user by id
     *
     * @param id
     *            id of user to get
     * @return returned user
     */
    UserDto getUser ( Long id );

    /**
     * Returns all users
     *
     * @return all users
     */
    List<UserDto> getUsersList ();

    /**
     * Updates the user with the given id
     *
     * @param id
     *            id of user to update
     * @param userDto
     *            information of user to update
     * @return updated user
     */
    UserDto updateUser ( Long id, UserDto userDto );

    /**
     * Deletes the user with the given id
     *
     * @param id
     *            id of user to delete
     */
    void deleteUser ( Long id );

    /**
     * Returns the user with the given id.
     * 
     * @param userId
     *            user's id
     * @return the user with the given id
     * @throws ResourceNotFoundException
     *             if the user doesn't exist
     */
    UserDto getUserById ( Long userId );

    /**
     * Returns the user with the given name
     * 
     * @param userName
     *            user's name
     * @return the user with the given name.
     * @throws ResourceNotFoundException
     *             if the user doesn't exist
     */
    UserDto getUserByUsername ( String userName );

    /**
     * Returns true if the user already exists in the database.
     * 
     * @param userName
     *            user's name to check
     * @return true if already in the database
     */
    boolean isDuplicateUsername ( String userName );

    /**
     * Returns the user with the given email
     * 
     * @param userEmail
     *            user's email
     * @return the user with the given email.
     * @throws ResourceNotFoundException
     *             if the user doesn't exist
     */
    UserDto getUserByEmail ( String userEmail );

    // /**
    // * Returns true if the user already exists in the database.
    // *
    // * @param userEmail user's email to check
    // * @return true if already in the database
    // */
    // boolean isDuplicateEmail(String userEmail);
}
