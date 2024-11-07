package edu.ncsu.csc326.wolfcafe.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
@RequestMapping ( "api/users" )
@AllArgsConstructor
@CrossOrigin ( "*" )
public class UserController {

    /** Link to UserService */
    private final UserService userService;

    /**
     * Creates a user
     *
     * @param userDto
     *            a user dto
     * @return a UserDto object
     */
    @PreAuthorize ( "hasRole('ADMIN')" )
    @PostMapping
    public ResponseEntity<UserDto> createUser ( @RequestBody final UserDto userDto ) {
        if ( userService.isDuplicateUsername( userDto.getUsername() ) ) {
            System.err.println( "Duplicate username" );
            return ResponseEntity.status( HttpStatus.CONFLICT ).body( userDto );
        }

        if ( userDto.getUsername().trim().isEmpty() || userDto.getEmail().trim().isEmpty()
                || userDto.getPassword().length() < 8 ) {
            System.err.println( "Invalid input format" );
            return ResponseEntity.status( HttpStatus.BAD_REQUEST ).body( userDto );
        }

        final boolean isValidUsername = userDto.getUsername().matches( "^[a-zA-Z0-9.]+$" );
        final boolean isValidEmail = userDto.getEmail().matches( "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$" );

        if ( !isValidUsername || !isValidEmail ) {
            System.err.println( "Invalid format" );
            return ResponseEntity.status( HttpStatus.BAD_REQUEST ).body( userDto );
        }

        final UserDto savedUserDto = userService.createUser( userDto );
        return ResponseEntity.status( HttpStatus.CREATED ).body( savedUserDto );
    }

    /**
     * Updates a user
     *
     * @param id
     *            id of the user dto
     * @param userDto
     *            the user dto
     * @return an updated user dto
     */
    @PreAuthorize ( "hasRole('ADMIN')" )
    @PutMapping ( "/{id}" )
    public ResponseEntity<UserDto> updateUser ( @PathVariable ( "id" ) final long id,
            @RequestBody final UserDto userDto ) {
        if ( userService.isDuplicateUsername( userDto.getUsername() ) ) {
            System.err.println( "Duplicate username" );
            return ResponseEntity.status( HttpStatus.CONFLICT ).body( userDto );
        }

        if ( userDto.getUsername().trim().isEmpty() || userDto.getEmail().trim().isEmpty()
                || userDto.getPassword().length() < 8 ) {
            System.err.println( "Invalid input format" );
            return ResponseEntity.status( HttpStatus.BAD_REQUEST ).body( userDto );
        }

        final boolean isValidUsername = userDto.getUsername().matches( "^[a-zA-Z0-9.]+$" );
        final boolean isValidEmail = userDto.getEmail().matches( "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$" );

        if ( !isValidUsername || !isValidEmail ) {
            System.err.println( "Invalid format" );
            return ResponseEntity.status( HttpStatus.BAD_REQUEST ).body( userDto );
        }

        final UserDto editedUserDto = userService.updateUser( id, userDto );
        return ResponseEntity.ok( editedUserDto );
    }

    /**
     * Gets a user dto with the given id
     *
     * @param id
     *            the id of the user dto to find
     * @return a user dto
     */
    @PreAuthorize ( "hasRole('ADMIN')" )
    @GetMapping ( "/{id}" )
    public ResponseEntity<UserDto> getUser ( @PathVariable final long id ) {
        final UserDto userDto = userService.getUserById( id );
        if ( userDto == null ) {
            return ResponseEntity.status( HttpStatus.NOT_FOUND ).build();
        }
        return ResponseEntity.ok( userDto );
    }

    /**
     * Deletes a user dto with the given id
     *
     * @param id
     *            the id of the user dto to be deleted
     * @return status
     */
    @PreAuthorize ( "hasRole('ADMIN')" )
    @DeleteMapping ( "/{id}" )
    public ResponseEntity<String> deleteUser ( @PathVariable final long id ) {
        try {
            userService.deleteUser( id );
            return ResponseEntity.ok( "User deleted successfully." );
        }
        catch ( final ResourceNotFoundException e ) {
            return ResponseEntity.status( HttpStatus.NOT_FOUND ).body( "User not found." );
        }
    }

    /**
     * Returns a list of user dtos
     *
     * @return a list of user dtos
     */
    @PreAuthorize ( "hasRole('ADMIN')" )
    @GetMapping
    public ResponseEntity<List<UserDto>> getUsersList () {
        final List<UserDto> users = userService.getUsersList();
        return ResponseEntity.ok( users );
    }
}