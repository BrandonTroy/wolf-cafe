package edu.ncsu.csc326.wolfcafe.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import edu.ncsu.csc326.wolfcafe.entity.InventoryEntry;

/**
 * Repository for storing inventory entries mapping an item id to a quantity
 */
public interface InventoryRepository extends JpaRepository<InventoryEntry, Long> {
    /**
     * Finds an entry given the id of the item the entry is for
     *
     * @param itemId
     *            id of item to get entry for
     * @return InventoryEntry containing item id and quantity of that item in
     *         the inventory
     */
    Optional<InventoryEntry> findByItemId ( Long itemId );
}
