package edu.ncsu.csc326.wolfcafe.service.impl;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import edu.ncsu.csc326.wolfcafe.dto.UserDto;
import edu.ncsu.csc326.wolfcafe.entity.User;
import edu.ncsu.csc326.wolfcafe.exception.ResourceNotFoundException;
import edu.ncsu.csc326.wolfcafe.mapper.UserMapper;
import edu.ncsu.csc326.wolfcafe.repository.UserRepository;
import edu.ncsu.csc326.wolfcafe.service.UserService;

/**
 * User service implementation
 *
 * @author Karthik Nandakumar
 */
@Service
public class UserServiceImpl implements UserService {

    /**
     * Connection to the users table in the database
     */
    @Autowired
    private UserRepository  userRepository;

    /**
     * A Spring tool used to hash passwords
     */
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public UserDto createUser ( final UserDto userDto ) {
        if ( isDuplicateUsername( userDto.getId(), userDto.getUsername() ) ) {
            throw new IllegalArgumentException( "Duplicate username" );
        }
        if ( isDuplicateEmail( userDto.getId(), userDto.getEmail() ) ) {
            throw new IllegalArgumentException( "Duplicate email" );
        }

        validateUserDto( userDto );
        userDto.setPassword( passwordEncoder.encode( userDto.getPassword() ) );
        final User user = UserMapper.mapToUser( userDto );
        final User savedUser = userRepository.save( user );
        return UserMapper.mapToUserDto( savedUser );
    }

    /**
     * Validates a user dto
     *
     * @param userDto
     *            the dto to be validated
     */
    private void validateUserDto ( final UserDto userDto ) {
        // Check name validity
        if ( !userDto.getName().matches( "^[a-zA-Z.\\s\\-']+$" ) ) {
            throw new IllegalArgumentException( "Invalid name format" );
        }

        // Check username validity
        if ( !userDto.getUsername().matches( "^[a-zA-Z0-9.]+$" ) ) {
            throw new IllegalArgumentException( "Invalid username format" );
        }

        // Check email validity
        if ( !userDto.getEmail().matches( "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$" ) ) {
            throw new IllegalArgumentException( "Invalid email format" );
        }

        // Check password length
        if ( userDto.getPassword().length() < 8 ) {
            throw new IllegalArgumentException( "Password must be at least 8 characters long" );
        }
    }

    @Override
    public UserDto getUser ( final Long id ) {
        final User user = userRepository.findById( id )
                .orElseThrow( () -> new ResourceNotFoundException( "User does not exist with id " + id ) );
        return UserMapper.mapToUserDto( user );
    }

    @Override
    public List<UserDto> getUsersList () {
        final List<User> users = userRepository.findAll();
        return users.stream().map( UserMapper::mapToUserDto ).collect( Collectors.toList() );
    }

    @Override
    public UserDto updateUser ( final Long id, final UserDto userDto ) {

        final User user = userRepository.findById( id )
                .orElseThrow( () -> new ResourceNotFoundException( "User does not exist with id " + id ) );
        if ( isDuplicateUsername( id, userDto.getUsername() ) ) {
            throw new IllegalArgumentException( "Duplicate username" );
        }
        if ( isDuplicateEmail( id, userDto.getEmail() ) ) {
            throw new IllegalArgumentException( "Duplicate email" );
        }
        user.setName( userDto.getName() );
        user.setUsername( userDto.getUsername() );
        user.setEmail( userDto.getEmail() );
        user.setRole( userDto.getRole() );

        final User savedUser = userRepository.save( user );
        return UserMapper.mapToUserDto( savedUser );
    }

    @Override
    public void deleteUser ( final Long id ) {
        final User user = userRepository.findById( id )
                .orElseThrow( () -> new ResourceNotFoundException( "User does not exist with id " + id ) );
        userRepository.delete( user );
    }

    @Override
    public UserDto getUserById ( final Long userId ) {
        return getUser( userId ); // Reuse the existing method
    }

    @Override
    public UserDto getUserByUsername ( final String userName ) {
        return userRepository.findByUsername( userName ).map( UserMapper::mapToUserDto )
                .orElseThrow( () -> new ResourceNotFoundException( "User does not exist with username " + userName ) );
    }

    @Override
    public boolean isDuplicateUsername ( final Long userId, final String userName ) {
        final Optional<User> duplicate = userRepository.findByUsername( userName );
        return duplicate.isPresent() && !duplicate.get().getId().equals( userId );
    }

    @Override
    public UserDto getUserByEmail ( final String userEmail ) {
        return userRepository.findByEmail( userEmail ).map( UserMapper::mapToUserDto )
                .orElseThrow( () -> new ResourceNotFoundException( "User does not exist with email " + userEmail ) );
    }

    @Override
    public boolean isDuplicateEmail ( final Long userId, final String userEmail ) {
        final Optional<User> duplicate = userRepository.findByEmail( userEmail );
        return duplicate.isPresent() && !duplicate.get().getId().equals( userId );
    }
}
