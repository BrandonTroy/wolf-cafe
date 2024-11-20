package edu.ncsu.csc326.wolfcafe.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import edu.ncsu.csc326.wolfcafe.exception.ResourceNotFoundException;
import edu.ncsu.csc326.wolfcafe.service.TaxService;
import lombok.AllArgsConstructor;

/**
 * TaxController provides the endpoint for ordering a item.
 *
 * @author Narim Lee
 */
@RestController
@AllArgsConstructor
@RequestMapping ( "/api/tax" )
@CrossOrigin ( "*" )
public class TaxController {

    /** Link to TaxService */
    private final TaxService taxService;

    /**
     * Set the tax rate of the system to the given value. Requires the ADMIN
     * role
     *
     * @param taxRate
     *            tax rate to set to
     * @return tax rate
     */
    @PreAuthorize ( "hasAnyRole('ADMIN')" )
    @PutMapping
    public ResponseEntity<Double> setTaxRate ( @RequestBody final double taxRate ) {
        try {
            taxService.setTaxRate( taxRate );
            return ResponseEntity.ok( taxRate );
        }
        catch ( IllegalArgumentException e ) {
            return ResponseEntity.status( HttpStatus.BAD_REQUEST ).body( taxRate );
        }

    }

    /**
     * Return the tax rate of the system. Requires ADMIN, CUSTOMER, or GUEST
     * role
     *
     * @return tax rate of the system
     */
    @PreAuthorize ( "hasAnyRole('ADMIN', 'CUSTOMER', 'GUEST')" )
    @GetMapping
    public ResponseEntity<Double> getTaxRate () {
        try {
            Double taxRate = taxService.getTaxRate();
            return ResponseEntity.ok( taxRate );
        }
        catch ( ResourceNotFoundException e ) {
            return ResponseEntity.status( HttpStatus.NOT_FOUND ).body( null );
        }
    }
}
