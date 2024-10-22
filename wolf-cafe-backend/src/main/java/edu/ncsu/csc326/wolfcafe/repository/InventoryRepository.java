package edu.ncsu.csc326.wolfcafe.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import edu.ncsu.csc326.wolfcafe.entity.InventoryEntry;

public interface InventoryRepository extends JpaRepository<InventoryEntry, Long> {
    Optional<InventoryEntry> findByItemId(Long itemId);
}