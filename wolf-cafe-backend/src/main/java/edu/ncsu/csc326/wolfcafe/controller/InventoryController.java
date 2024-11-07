package edu.ncsu.csc326.wolfcafe.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import edu.ncsu.csc326.wolfcafe.dto.InventoryDto;
import edu.ncsu.csc326.wolfcafe.service.InventoryService;
import lombok.AllArgsConstructor;

/**
 * Controller for WolfCafe's inventory. The inventory is a table mapping items
 * to the quantity available.
 */
@RestController
@RequestMapping ( "/api/inventory" )
@AllArgsConstructor
@CrossOrigin ( "*" )
public class InventoryController {

    /**
     * Connection to inventory service for manipulating the Inventory model.
     */
    @Autowired
    private final InventoryService inventoryService;

    /**
     * REST API endpoint to provide GET access to the WolfCafe's inventory.
     *
     * @return response to the request
     */
    @GetMapping
    public ResponseEntity<InventoryDto> getInventory () {
        final InventoryDto inventoryDto = inventoryService.getInventory();
        return ResponseEntity.ok( inventoryDto );
    }

    /**
     * REST API endpoint to provide update access to the WolfCafe's inventory.
     *
     * @param inventoryDto
     *            amounts to add to inventory
     * @return response to the request
     */
    @PreAuthorize ( "hasAnyRole('MANAGER', 'BARISTA', 'ADMIN')" )
    @PutMapping
    public ResponseEntity<InventoryDto> updateInventory ( @RequestBody final InventoryDto inventoryDto ) {
        final InventoryDto savedInventoryDto;
        try {
            savedInventoryDto = inventoryService.addInventory( inventoryDto );
        }
        catch ( final IllegalArgumentException e ) {
            return new ResponseEntity<InventoryDto>( inventoryDto, HttpStatus.BAD_REQUEST );
        }
        return ResponseEntity.ok( savedInventoryDto );
    }

}
