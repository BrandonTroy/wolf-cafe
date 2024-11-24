package edu.ncsu.csc326.wolfcafe.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import edu.ncsu.csc326.wolfcafe.entity.Order;

/**
 * Stores all of the Orders in the system in a repository
 * 
 * @author Ryan Hinshaw (rthinsha)
 */
public interface OrderRepository extends JpaRepository<Order, Long> {
}

