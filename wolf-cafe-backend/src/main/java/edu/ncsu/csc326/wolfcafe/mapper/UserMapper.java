package edu.ncsu.csc326.wolfcafe.mapper;

import org.springframework.stereotype.Component;

import edu.ncsu.csc326.wolfcafe.dto.UserDto;
import edu.ncsu.csc326.wolfcafe.entity.User;

/**
 * Class that maps user entries into a single dto containing a map of items to
 * quantities
 */
@Component
public class UserMapper {

	/**
	 * Converts a User entity to UserDto
	 * 
	 * @param user User to convert
	 * @return UserDto object
	 */
	public static UserDto mapToUserDto(User user) {
		UserDto r = new UserDto(user.getId(), user.getName(), user.getUsername(), user.getEmail(), user.getPassword(),
				user.getRole());
		return r;
	}

	/**
	 * Converts a UserDto object to a User entity.
	 * 
	 * @param userDto UserDto to convert
	 * @return User entity
	 */
	public static User mapToUser(UserDto userDto) {
		User r = new User(userDto.getId(), userDto.getName(), userDto.getUsername(), userDto.getEmail(),
				userDto.getPassword(), userDto.getRole());
		return r;
	}
}
