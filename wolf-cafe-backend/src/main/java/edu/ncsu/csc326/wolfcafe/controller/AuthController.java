package edu.ncsu.csc326.wolfcafe.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import edu.ncsu.csc326.wolfcafe.dto.JwtAuthResponse;
import edu.ncsu.csc326.wolfcafe.dto.LoginDto;
import edu.ncsu.csc326.wolfcafe.dto.RegisterDto;
import edu.ncsu.csc326.wolfcafe.service.AuthService;
import edu.ncsu.csc326.wolfcafe.service.UserService;
import lombok.AllArgsConstructor;

/**
 * Controller for authentication functionality.
 */
@CrossOrigin ( "*" )
@RestController
@RequestMapping ( "/api/auth" )
@AllArgsConstructor
public class AuthController {

    /** Link to AuthService */
    private final AuthService authService;

    /** Link to UserService */
    private final UserService userService;

    /**
     * Registers a new customer user with the system.
     *
     * @param registerDto
     *            object with registration info
     * @return response indicating success or failure
     */
    @PostMapping ( "/register" )
    public ResponseEntity<String> register ( @RequestBody final RegisterDto registerDto ) {
        if ( userService.isDuplicateUsername( registerDto.getUsername() ) ) {
            System.err.println( "Duplicate username" );
            return ResponseEntity.status( HttpStatus.CONFLICT ).body( "Duplicate username or email" );
        }

        if ( registerDto.getUsername().trim().isEmpty() || registerDto.getEmail().trim().isEmpty()
                || registerDto.getPassword().length() < 8 ) {
            System.err.println( "Invalid input format" );
            return ResponseEntity.status( HttpStatus.BAD_REQUEST ).body( "Invalid input format" );
        }

        final boolean isValidUsername = registerDto.getUsername().matches( "^[a-zA-Z0-9.]+$" );
        final boolean isValidEmail = registerDto.getEmail()
                .matches( "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$" );

        if ( !isValidUsername || !isValidEmail ) {
            System.err.println( "Invalid format" );
            return ResponseEntity.status( HttpStatus.BAD_REQUEST ).body( "Invalid format" );
        }

        final String response = authService.register( registerDto );
        return new ResponseEntity<>( response, HttpStatus.CREATED );
    }

    /**
     * Logs in the given user
     *
     * @param loginDto
     *            user information for login
     * @return object representing the logged in user
     */
    @PostMapping ( "/login" )
    public ResponseEntity<JwtAuthResponse> login ( @RequestBody final LoginDto loginDto ) {
        final JwtAuthResponse jwtAuthResponse = authService.login( loginDto );
        return new ResponseEntity<>( jwtAuthResponse, HttpStatus.OK );
    }

    /**
     * Deletes the given user. Requires the ADMIN role.
     *
     * @param id
     *            id of user to delete
     * @return response indicating success or failure
     */
    @PreAuthorize ( "hasRole('ADMIN')" )
    @DeleteMapping ( "/user/{id}" )
    public ResponseEntity<String> deleteUser ( @PathVariable ( "id" ) final Long id ) {
        authService.deleteUserById( id );
        return ResponseEntity.ok( "User deleted successfully." );
    }
}
