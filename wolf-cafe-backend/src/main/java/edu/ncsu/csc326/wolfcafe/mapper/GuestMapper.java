package edu.ncsu.csc326.wolfcafe.mapper;

import org.springframework.stereotype.Component;

import edu.ncsu.csc326.wolfcafe.dto.GuestDto;
import edu.ncsu.csc326.wolfcafe.entity.Guest;

/**
 * Class that maps user entries into a single dto containing a map of items to
 * quantities
 * 
 * @author Karthik Nandakumar
 */
@Component
public class GuestMapper {

	/**
	 * Converts a User entity to UserDto
	 * 
	 * @param user User to convert
	 * @return UserDto object
	 */
	public static GuestDto mapToUserDto(Guest user) {
		GuestDto r = new GuestDto(user.getId(), user.getUsername(), user.getRole());
		return r;
	}

	/**
	 * Converts a UserDto object to a User entity.
	 * 
	 * @param userDto UserDto to convert
	 * @return User entity
	 */
	public static Guest mapToUser(GuestDto userDto) {
		Guest r = new Guest(userDto.getId(), userDto.getUsername(), userDto.getRole());
		return r;
	}
}
