package edu.ncsu.csc326.wolfcafe.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import edu.ncsu.csc326.wolfcafe.entity.Guest;

/**
 * Repository interface for users.
 *
 * @author Karthik Nandakumar
 */
public interface GuestRepository extends JpaRepository<Guest, Long> {

	/**
	 * Returns the user object associated with either the id
	 *
	 * @param id user's id
	 * @return User object or exception on error
	 */
	@Override
	Optional<Guest> findById(Long id);

}
