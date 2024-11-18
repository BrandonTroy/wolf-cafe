package edu.ncsu.csc326.wolfcafe.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import edu.ncsu.csc326.wolfcafe.entity.Tax;

/**
 * Repository for storing tax rate information
 *
 * @author Narim Lee
 */
public interface TaxRepository extends JpaRepository<Tax, Long> {

}
