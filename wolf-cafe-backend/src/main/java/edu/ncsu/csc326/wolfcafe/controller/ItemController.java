package edu.ncsu.csc326.wolfcafe.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import edu.ncsu.csc326.wolfcafe.dto.ItemDto;
import edu.ncsu.csc326.wolfcafe.exception.ResourceNotFoundException;
import edu.ncsu.csc326.wolfcafe.repository.ItemRepository;
import edu.ncsu.csc326.wolfcafe.service.ItemService;
import lombok.AllArgsConstructor;

/**
 * Controller for API endpoints for an Item
 */
@RestController
@RequestMapping ( "api/items" )
@AllArgsConstructor
@CrossOrigin ( "*" )
public class ItemController {

    /** Link to ItemService */
    private final ItemService    itemService;

    /**
     * Connection to ItemRepository for dealing with the items table in the
     * database
     */
    private final ItemRepository itemRepository;

    /**
     * Adds an item to the list of items. Requires the STAFF role.
     *
     * @param itemDto
     *            item to add
     * @return added item
     */
    @PreAuthorize ( "hasAnyRole('MANAGER')" )
    @PostMapping
    public ResponseEntity<ItemDto> addItem ( @RequestBody ItemDto itemDto ) {
        try {
            ItemDto savedItem = itemService.addItem( itemDto );
            return new ResponseEntity<>( savedItem, HttpStatus.CREATED );
        }
        catch ( IllegalArgumentException e ) {
            return ResponseEntity.status( HttpStatus.CONFLICT ).body( itemDto );
        }
    }

    /**
     * Gets an item by id. Requires the STAFF or CUSTOMER role.
     *
     * @param id
     *            item id
     * @return item with the id
     */
    @PreAuthorize ( "hasAnyRole('MANAGER', 'BARISTA', 'CUSTOMER', 'GUEST')" )
    @GetMapping ( "{id}" )
    public ResponseEntity<ItemDto> getItem ( @PathVariable ( "id" ) Long id ) {
        try {
            ItemDto item = itemService.getItem( id );
            return ResponseEntity.ok( item );
        }
        catch ( ResourceNotFoundException e ) {
            return ResponseEntity.status( HttpStatus.NOT_FOUND ).body( null );
        }
    }

    /**
     * Returns all items. Requires the STAFF or CUSTOMER role.
     *
     * @return a list of all items
     */
    @PreAuthorize ( "hasAnyRole('MANAGER', 'BARISTA', 'CUSTOMER', 'GUEST')" )
    @GetMapping
    public ResponseEntity<List<ItemDto>> getAllItems () {
        final List<ItemDto> items = itemService.getAllItems();
        return ResponseEntity.ok( items );
    }

    /**
     * Updates the item with the given id. Requires STAFF role.
     *
     * @param id
     *            item to update
     * @param itemDto
     *            information about the item to update
     * @return updated item
     */
    @PreAuthorize ( "hasAnyRole('MANAGER')" )
    @PutMapping ( "{id}" )
    public ResponseEntity<ItemDto> updateItem ( @PathVariable ( "id" ) Long id, @RequestBody ItemDto itemDto ) {
        try {
            ItemDto updatedItem = itemService.updateItem( id, itemDto );
            return ResponseEntity.ok( updatedItem );
        }
        catch ( ResourceNotFoundException e ) {
            return ResponseEntity.status( HttpStatus.NOT_FOUND ).body( null );
        }
    }

    /**
     * Deletes the item with the given id. Requires the STAFF role.
     *
     * @param id
     *            item to delete
     * @return response indicating success or failure
     */
    @PreAuthorize ( "hasAnyRole('MANAGER')" )
    @DeleteMapping ( "{id}" )
    public ResponseEntity<String> deleteItem ( @PathVariable ( "id" ) Long id ) {
        try {
            itemService.deleteItem( id );
            return ResponseEntity.ok( "Item deleted successfully" );
        }
        catch ( ResourceNotFoundException e ) {
            return ResponseEntity.status( HttpStatus.NOT_FOUND ).body( null );
        }
    }
}
