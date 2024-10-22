package edu.ncsu.csc326.wolfcafe.service.impl;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import edu.ncsu.csc326.wolfcafe.dto.InventoryDto;
import edu.ncsu.csc326.wolfcafe.entity.InventoryEntry;
import edu.ncsu.csc326.wolfcafe.entity.Item;
import edu.ncsu.csc326.wolfcafe.mapper.InventoryMapper;
import edu.ncsu.csc326.wolfcafe.repository.InventoryRepository;
import edu.ncsu.csc326.wolfcafe.repository.ItemRepository;
import edu.ncsu.csc326.wolfcafe.service.InventoryService;

/**
 * Implementation of the InventoryService interface.
 */
@Service
public class InventoryServiceImpl implements InventoryService {

    /** Repository for inventory operations */
    private final InventoryRepository inventoryRepository;

    /** Repository for item operations */
    private final ItemRepository      itemRepository;

    /**
     * Mapper for wrapping inventory entries up into a dto
     */
    private final InventoryMapper     inventoryMapper;

    /**
     * Constructs a new InventoryServiceImpl with required repositories.
     *
     * @param inventoryRepository
     *            repository for inventory operations
     * @param itemRepository
     *            repository for item operations
     * @param inventoryMapper
     *            mapper to use for converting to InventoryDto
     */
    @Autowired
    public InventoryServiceImpl ( final InventoryRepository inventoryRepository, final ItemRepository itemRepository,
            final InventoryMapper inventoryMapper ) {
        this.inventoryRepository = inventoryRepository;
        this.itemRepository = itemRepository;
        this.inventoryMapper = inventoryMapper;
    }

    @Override
    @Transactional ( readOnly = true )
    public InventoryDto getInventory () {
        final List<InventoryEntry> entries = inventoryRepository.findAll();
        return inventoryMapper.toDto( entries );
    }

    @Override
    @Transactional
    public InventoryDto addInventory ( final InventoryDto inventoryDto ) {
        if ( inventoryDto == null || inventoryDto.getItemQuantities() == null ) {
            throw new IllegalArgumentException( "Inventory DTO cannot be null" );
        }

        for ( final Map.Entry<Long, Integer> entry : inventoryDto.getItemQuantities().entrySet() ) {
            final Long itemId = entry.getKey();
            final Integer quantityToAdd = entry.getValue();

            if ( quantityToAdd < 0 ) {
                throw new IllegalArgumentException( "Cannot add negative quantity for item ID: " + itemId );
            }

            final Item item = itemRepository.findById( itemId )
                    .orElseThrow( () -> new IllegalArgumentException( "Item not found with ID: " + itemId ) );

            inventoryRepository.findByItemId( itemId ).ifPresentOrElse( inventory -> {
                inventory.setQuantity( inventory.getQuantity() + quantityToAdd );
                inventoryRepository.save( inventory );
            }, () -> {
                final InventoryEntry newEntry = new InventoryEntry();
                newEntry.setItem( item );
                newEntry.setQuantity( quantityToAdd );
                inventoryRepository.save( newEntry );
            } );
        }

        return getInventory();
    }
}
