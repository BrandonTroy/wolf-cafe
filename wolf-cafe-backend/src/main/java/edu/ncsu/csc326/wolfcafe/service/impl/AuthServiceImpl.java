package edu.ncsu.csc326.wolfcafe.service.impl;

import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import edu.ncsu.csc326.wolfcafe.dto.JwtAuthResponse;
import edu.ncsu.csc326.wolfcafe.dto.LoginDto;
import edu.ncsu.csc326.wolfcafe.dto.RegisterDto;
import edu.ncsu.csc326.wolfcafe.entity.Role;
import edu.ncsu.csc326.wolfcafe.entity.User;
import edu.ncsu.csc326.wolfcafe.exception.ResourceNotFoundException;
import edu.ncsu.csc326.wolfcafe.exception.WolfCafeAPIException;
import edu.ncsu.csc326.wolfcafe.mapper.RoleMapper;
import edu.ncsu.csc326.wolfcafe.repository.UserRepository;
import edu.ncsu.csc326.wolfcafe.security.JwtTokenProvider;
import edu.ncsu.csc326.wolfcafe.service.AuthService;
import lombok.AllArgsConstructor;

/**
 * Implemented AuthService
 */
@Service
@AllArgsConstructor
public class AuthServiceImpl implements AuthService {

    /**
     * Connection to the database table storing users
     */
    private final UserRepository        userRepository;
    /**
     * Tool to encrypt passwords for security
     */
    private final PasswordEncoder       passwordEncoder;
    /**
     * Processes authentication requests
     */
    private final AuthenticationManager authenticationManager;
    /**
     * Generates tokens for JWT authentication
     */
    private final JwtTokenProvider      jwtTokenProvider;

    /**
     * Registers the given user
     *
     * @param registerDto
     *            new user information
     * @return message for success or failure
     */
    @Override
    public String register ( final RegisterDto registerDto ) {
        // Check for duplicates - username
        if ( userRepository.existsByUsername( registerDto.getUsername() ) ) {
            throw new WolfCafeAPIException( HttpStatus.BAD_REQUEST, "Username already exists." );
        }
        // Check for duplicates - email
        if ( userRepository.existsByEmail( registerDto.getEmail() ) ) {
            throw new WolfCafeAPIException( HttpStatus.BAD_REQUEST, "Email already exists." );
        }

        final User user = new User();
        user.setName( registerDto.getName() );
        user.setUsername( registerDto.getUsername() );
        user.setEmail( registerDto.getEmail() );
        user.setPassword( passwordEncoder.encode( registerDto.getPassword() ) );
        user.setRole( Role.CUSTOMER );

        userRepository.save( user );

        return "User registered successfully.";
    }

    /**
     * Logins in the given user
     *
     * @param loginDto
     *            username/email and password
     * @return response with authenticated user
     */
    @Override
    public JwtAuthResponse login ( final LoginDto loginDto ) {
        // Guest Login
        if ( loginDto.getUsernameOrEmail().equals( "guest-user" ) && loginDto.getPassword().equals( "guest" ) ) {
            final User guestUser = createGuestUser();
            final JwtAuthResponse jwtAuthResponse = new JwtAuthResponse();
            jwtAuthResponse.setRole( "ROLE_GUEST" );
            jwtAuthResponse.setAccessToken( jwtTokenProvider.generateTokenGuest( guestUser ) );
            return jwtAuthResponse;
        }

        final Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken( loginDto.getUsernameOrEmail(), loginDto.getPassword() ) );

        SecurityContextHolder.getContext().setAuthentication( authentication );

        final String token = jwtTokenProvider.generateToken( authentication );

        final Optional<User> userOptional = userRepository.findByUsernameOrEmail( loginDto.getUsernameOrEmail(),
                loginDto.getUsernameOrEmail() );

        String role = null;
        if ( userOptional.isPresent() ) {
            final User loggedInUser = userOptional.get();
            role = RoleMapper.toString( loggedInUser.getRole() );
        }

        final JwtAuthResponse jwtAuthResponse = new JwtAuthResponse();
        jwtAuthResponse.setRole( role );
        jwtAuthResponse.setAccessToken( token );

        return jwtAuthResponse;

    }

    /**
     * Helper method to create a new Guest user, saving it to the database
     *
     * @return new Guest user
     */
    private User createGuestUser () {
        // Get the current user count from the repository
        final long userCount = userRepository.count(); // Get the count of users

        // Generate a new guest username based on the count
        final String guestUsername = "guest-" + ( userCount + 1 ); // Create a
                                                                   // unique
                                                                   // username

        // Create a new user entity for the guest
        final User newUser = new User();
        newUser.setName( "---GUEST---" );
        newUser.setUsername( guestUsername );
        newUser.setEmail( guestUsername + "@fake.com" );
        newUser.setPassword( "guest" );
        newUser.setRole( Role.GUEST ); // Set the role as guest

        // Save the new guest user to the database
        return userRepository.save( newUser );
    }

    /**
     * Deletes the given user by id
     *
     * @param id
     *            id of user to delete
     */
    @Override
    public void deleteUserById ( final Long id ) {
        userRepository.findById( id )
                .orElseThrow( () -> new ResourceNotFoundException( "User not found with id " + id ) );
        userRepository.deleteById( id );
    }
}
