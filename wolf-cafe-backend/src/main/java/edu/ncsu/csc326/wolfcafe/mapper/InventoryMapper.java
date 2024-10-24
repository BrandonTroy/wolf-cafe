package edu.ncsu.csc326.wolfcafe.mapper;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import edu.ncsu.csc326.wolfcafe.dto.InventoryDto;
import edu.ncsu.csc326.wolfcafe.entity.InventoryEntry;

/**
 * Class that maps inventory entries into a single dto containing a map of items
 * to quantities
 */
@Component
public class InventoryMapper {

    /**
     * Converts a list of inventory entries to an InventoryDto
     *
     * @param entries
     *            list of inventory entries
     * @return InventoryDto containing the mapped quantities
     */
    public InventoryDto toDto ( final List<InventoryEntry> entries ) {
        final InventoryDto dto = new InventoryDto();
        final Map<Long, Integer> quantities = entries.stream()
                .collect( Collectors.toMap( entry -> entry.getItem().getId(), InventoryEntry::getQuantity ) );
        dto.setItemQuantities( quantities );
        return dto;
    }

    /*
     * Converts a single inventory entry to a map entry
     * @param entry inventory entry
     * @return Map.Entry containing the item ID and quantity
     */
    // Commenting this out until we need to use it
    // public Map.Entry<Long, Integer> toMapEntry(InventoryEntry entry) {
    // return Map.entry(entry.getItem().getId(), entry.getQuantity());
    // }
}
