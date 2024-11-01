package edu.ncsu.csc326.wolfcafe.service.impl;

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

@Service
public class UserServiceImpl implements UserService {

	@Autowired
	private UserRepository userRepository;

	@Override
	public UserDto createUser(UserDto userDto) {
		if (isDuplicateUsername(userDto.getUsername()) || isDuplicateEmail(userDto.getEmail())) {
			throw new IllegalArgumentException("Duplicate username or email");
		}

		validateUserDto(userDto);

		User user = UserMapper.mapToUser(userDto);
		User savedUser = userRepository.save(user);
		return UserMapper.mapToUserDto(savedUser);
	}

	private void validateUserDto(UserDto userDto) {
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

	@Override
	public UserDto getUser(Long id) {
		User user = userRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("User does not exist with id " + id));
		return UserMapper.mapToUserDto(user);
	}

	@Override
	public List<UserDto> getUsersList() {
		List<User> users = userRepository.findAll();
		return users.stream().map(UserMapper::mapToUserDto).collect(Collectors.toList());
	}

	@Override
	public UserDto updateUser(Long id, UserDto userDto) {
		if (isDuplicateUsername(userDto.getUsername()) || isDuplicateEmail(userDto.getEmail())) {
			throw new IllegalArgumentException("Duplicate username or email");
		}

		User user = userRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("User does not exist with id " + id));
		user.setName(userDto.getName());
		user.setUsername(userDto.getUsername());
		user.setEmail(userDto.getEmail());
		user.setPassword(userDto.getPassword());
		user.setRole(userDto.getRole()); // Assuming role is properly mapped in UserDto

		User savedUser = userRepository.save(user);
		return UserMapper.mapToUserDto(savedUser);
	}

	@Override
	public void deleteUser(Long id) {
		User user = userRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("User does not exist with id " + id));
		userRepository.delete(user);
	}

	@Override
	public UserDto getUserById(Long userId) {
		return getUser(userId); // Reuse the existing method
	}

	@Override
	public UserDto getUserByUsername(String userName) {
		return userRepository.findByUsername(userName).map(UserMapper::mapToUserDto)
				.orElseThrow(() -> new ResourceNotFoundException("User does not exist with username " + userName));
	}

	@Override
	public boolean isDuplicateUsername(String userName) {
		return userRepository.existsByUsername(userName);
	}

	@Override
	public UserDto getUserByEmail(String userEmail) {
		return userRepository.findByEmail(userEmail).map(UserMapper::mapToUserDto)
				.orElseThrow(() -> new ResourceNotFoundException("User does not exist with email " + userEmail));
	}

	@Override
	public boolean isDuplicateEmail(String userEmail) {
		return userRepository.existsByEmail(userEmail);
	}
}
