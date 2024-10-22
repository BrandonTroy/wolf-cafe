package edu.ncsu.csc326.wolfcafe.service.impl;

import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import edu.ncsu.csc326.wolfcafe.dto.ItemDto;
import edu.ncsu.csc326.wolfcafe.entity.InventoryEntry;
import edu.ncsu.csc326.wolfcafe.entity.Item;
import edu.ncsu.csc326.wolfcafe.exception.ResourceNotFoundException;
import edu.ncsu.csc326.wolfcafe.repository.InventoryRepository;
import edu.ncsu.csc326.wolfcafe.repository.ItemRepository;
import edu.ncsu.csc326.wolfcafe.service.ItemService;
import lombok.AllArgsConstructor;

/**
 * Implemented item service
 */
@Service
@AllArgsConstructor
public class ItemServiceImpl implements ItemService {

    private final ItemRepository itemRepository;
    private final InventoryRepository inventoryRepository;

    private final ModelMapper modelMapper;

    /**
	 * Adds given item
	 * @param itemDto item to add
	 * @return added item
	 */
    @Override
    public ItemDto addItem(ItemDto itemDto) {
        Item item = modelMapper.map(itemDto, Item.class);
        Item savedItem = itemRepository.save(item);

        // Add an inventory entry with 0 quantity by default
        InventoryEntry entry = new InventoryEntry(savedItem, 0);
        inventoryRepository.save(entry);

        return modelMapper.map(savedItem, ItemDto.class);
    }

    /**
     * Gets item by id
     * @param id id of item to get
     * @return returned item
     */
    @Override
    public ItemDto getItem(Long id) {
        Item item = itemRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Item not found with id " + id));
        return modelMapper.map(item, ItemDto.class);
    }

    /**
     * Returns all items
     * @return all items
     */
    @Override
    public List<ItemDto> getAllItems() {
        List<Item> items = itemRepository.findAll();
        return items.stream().map((item) -> modelMapper.map(item, ItemDto.class)).collect(Collectors.toList());
    }

    /**
     * Updates the item with the given id
     * @param id id of item to update
     * @param itemDto information of item to update
     * @return updated item
     */
    @Override
    public ItemDto updateItem(Long id, ItemDto itemDto) {
        Item item = itemRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Item not found with id " + id));
        item.setName(itemDto.getName());
        item.setDescription(itemDto.getDescription());
        item.setPrice(itemDto.getPrice());
        Item updatedItem = itemRepository.save((item));
        return modelMapper.map(updatedItem, ItemDto.class);
    }

    /**
     * Deletes the item with the given id
     * @param id id of item to delete
     */
    @Override
    public void deleteItem(Long id) {
        itemRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Item not found with id " + id));
        itemRepository.deleteById(id);
    }
}
