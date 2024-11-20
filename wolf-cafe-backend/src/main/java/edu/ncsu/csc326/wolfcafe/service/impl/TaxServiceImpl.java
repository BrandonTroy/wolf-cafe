package edu.ncsu.csc326.wolfcafe.service.impl;

import org.springframework.stereotype.Service;

import edu.ncsu.csc326.wolfcafe.entity.Tax;
import edu.ncsu.csc326.wolfcafe.exception.ResourceNotFoundException;
import edu.ncsu.csc326.wolfcafe.repository.TaxRepository;
import edu.ncsu.csc326.wolfcafe.service.TaxService;
import jakarta.annotation.PostConstruct;
import lombok.AllArgsConstructor;

/**
 * Implementation of TaxService that includes a functionality to set and return
 * a tax rate
 *
 * @author Narim Lee
 */
@Service
@AllArgsConstructor
public class TaxServiceImpl implements TaxService {
    /** Repository for tax operations */
    private final TaxRepository taxRepository;

    /**
     * If the table is empty after the construction, set the default tax rate to
     * North Carolina's sales tax.
     */
    @PostConstruct
    public void setDefaultTax () {
        if ( taxRepository.findById( 1L ).isEmpty() ) {
            Tax newTax = new Tax( 1L, 4.75 );
            taxRepository.save( newTax );
        }

    }

    @Override
    public void setTaxRate ( double taxRate ) {
        // Throw an error for if invalid tax rate is given
        if ( taxRate <= 0 || taxRate > 50 ) {
            throw new IllegalArgumentException( "Invalid tax rate" );
        }

        // Overwrite the previous tax rate with the new rate
        Tax tax = taxRepository.findById( 1L ).orElse( new Tax( 1L, taxRate ) );
        tax.setTaxRate( taxRate );
        taxRepository.save( tax );
    }

    @Override
    public double getTaxRate () {
        // Throw an error if no tax information exists in the system
        Tax returnTax = taxRepository.findById( 1L )
                .orElseThrow( () -> new ResourceNotFoundException( "No tax information exists in the system." ) );
        return returnTax.getTaxRate();
    }

}
