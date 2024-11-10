package edu.ncsu.csc326.wolfcafe.service.impl;

import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
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
 * Referenced https://www.javatpoint.com/how-to-encrypt-password-in-java for
 * SHA-256 encryption for passwords
 *
 * @author Karthik Nandakumar
 */
@Service
public class UserServiceImpl implements UserService {

	/**
	 * Connection to the users table in the database
	 */
	@Autowired
	private UserRepository userRepository;

	@Override
	public UserDto createUser(final UserDto userDto) throws NoSuchAlgorithmException {
		if (isDuplicateUsername(userDto.getUsername())) {
			throw new IllegalArgumentException("Duplicate username");
		}
		if (isDuplicateEmail(userDto.getEmail())) {
			throw new IllegalArgumentException("Duplicate email");
		}

		validateUserDto(userDto);
		userDto.setPassword(toHexString(getSHA(userDto.getPassword())));
		final User user = UserMapper.mapToUser(userDto);
		final User savedUser = userRepository.save(user);
		return UserMapper.mapToUserDto(savedUser);
	}

	/**
	 * Validates a user dto
	 *
	 * @param userDto the dto to be validated
	 */
	private void validateUserDto(final UserDto userDto) {
		// Check username validity
		if (!userDto.getUsername().matches("^[a-zA-Z0-9.]+$")) {
			throw new IllegalArgumentException("Invalid username format");
		}

		// Check email validity
		if (!userDto.getEmail().matches("^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}$")) {
			throw new IllegalArgumentException("Invalid email format");
		}

		// Check password length
		if (userDto.getPassword().length() < 8) {
			throw new IllegalArgumentException("Password must be at least 8 characters long");
		}
	}

	/**
	 * Hashes a password into SHA-256 format
	 * 
	 * @param password the password to be hashed
	 * @return the hashed password
	 * @throws NoSuchAlgorithmException
	 */
	private static byte[] getSHA(String password) throws NoSuchAlgorithmException {
		// MessageDigest instance for hashing using SHA256
		MessageDigest messageDigest = MessageDigest.getInstance("SHA-256");

		// digest() method called to calculate message digest of an input and return
		// array of byte
		return messageDigest.digest(password.getBytes(StandardCharsets.UTF_8));
	}

	/**
	 * Converts the hashed password to a hex string
	 * 
	 * @param hash the hashed password to be converted
	 * @return the hex password string
	 */
	private static String toHexString(byte[] hash) {
		// Convert byte array of hash into digest
		BigInteger bigInteger = new BigInteger(1, hash);

		// Convert the digest into hex value
		StringBuilder password = new StringBuilder(bigInteger.toString(16));

		// Pad with leading zeros
		while (password.length() < 32) {
			password.insert(0, '0');
		}

		return password.toString();
	}

	@Override
	public UserDto getUser(final Long id) {
		final User user = userRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("User does not exist with id " + id));
		return UserMapper.mapToUserDto(user);
	}

	@Override
	public List<UserDto> getUsersList() {
		final List<User> users = userRepository.findAll();
		return users.stream().map(UserMapper::mapToUserDto).collect(Collectors.toList());
	}

	@Override
	public UserDto updateUser(final Long id, final UserDto userDto) {

		final User user = userRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("User does not exist with id " + id));
		if (isDuplicateUsername(userDto.getUsername())) {
			throw new IllegalArgumentException("Duplicate username");
		}
		if (isDuplicateEmail(userDto.getEmail())) {
			throw new IllegalArgumentException("Duplicate email");
		}
		user.setUsername(userDto.getUsername());
		user.setEmail(userDto.getEmail());
		user.setRole(userDto.getRole());

		final User savedUser = userRepository.save(user);
		return UserMapper.mapToUserDto(savedUser);
	}

	@Override
	public void deleteUser(final Long id) {
		final User user = userRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("User does not exist with id " + id));
		userRepository.delete(user);
	}

	@Override
	public UserDto getUserById(final Long userId) {
		return getUser(userId); // Reuse the existing method
	}

	@Override
	public UserDto getUserByUsername(final String userName) {
		return userRepository.findByUsername(userName).map(UserMapper::mapToUserDto)
				.orElseThrow(() -> new ResourceNotFoundException("User does not exist with username " + userName));
	}

	@Override
	public boolean isDuplicateUsername(final String userName) {
		return userRepository.existsByUsername(userName);
	}

	@Override
	public UserDto getUserByEmail(final String userEmail) {
		return userRepository.findByEmail(userEmail).map(UserMapper::mapToUserDto)
				.orElseThrow(() -> new ResourceNotFoundException("User does not exist with email " + userEmail));
	}

	@Override
	public boolean isDuplicateEmail(final String userEmail) {
		return userRepository.existsByEmail(userEmail);
	}
}
