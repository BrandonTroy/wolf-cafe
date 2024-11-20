package edu.ncsu.csc326.wolfcafe.service.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import edu.ncsu.csc326.wolfcafe.entity.Tax;
import edu.ncsu.csc326.wolfcafe.exception.ResourceNotFoundException;
import edu.ncsu.csc326.wolfcafe.repository.TaxRepository;
import edu.ncsu.csc326.wolfcafe.service.TaxService;

/**
 * Test for TaxServiceImpl including setting and getting tax rates
 */
@SpringBootTest
public class TaxServiceImplTest {

    /**
     * Connection to tax repository to delete all before the test
     */
    @Autowired
    private TaxRepository taxRepository;

    /**
     * Connection to tax service for changing tax values
     */
    @Autowired
    private TaxService    taxService;

    /**
     * Tests setTaxRate method
     */
    @Test
    @Transactional
    public void testSetTaxRate () {
        // Check for the default tax value
        Optional<Tax> defaultTax = taxRepository.findById( 1L );
        assertEquals( 4.75, defaultTax.get().getTaxRate() );

        // Check setting a new tax value
        taxService.setTaxRate( 49 );
        assertEquals( 49, taxRepository.findById( 1L ).get().getTaxRate() );

        taxService.setTaxRate( 1.234 );
        assertEquals( 1.234, taxRepository.findById( 1L ).get().getTaxRate() );

        // Test invalid tax rates
        assertThrows( IllegalArgumentException.class, () -> taxService.setTaxRate( -30 ) );
        assertThrows( IllegalArgumentException.class, () -> taxService.setTaxRate( 51 ) );

        taxRepository.deleteAll();
    }

    /**
     * Tests getTaxRate method
     */
    @Test
    @Transactional
    public void testGetTaxRate () {
        // Default tax rate
        assertEquals( 4.75, taxService.getTaxRate() );

        // Test valid tax rate change
        taxService.setTaxRate( 49 );
        assertEquals( 49, taxService.getTaxRate() );

        // Check the tax rate after an invalid tax rate change
        assertThrows( IllegalArgumentException.class, () -> taxService.setTaxRate( -30 ) );
        assertEquals( 49, taxService.getTaxRate() );

        taxRepository.deleteAll();

        assertThrows( ResourceNotFoundException.class, () -> taxService.getTaxRate() );

        taxRepository.deleteAll();
    }
}
