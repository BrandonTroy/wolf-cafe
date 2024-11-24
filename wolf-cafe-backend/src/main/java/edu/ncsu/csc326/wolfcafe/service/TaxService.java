package edu.ncsu.csc326.wolfcafe.service;

/**
 * Interface for implementing a tax service that includes the functionality to
 * set and get a tax rate
 *
 * @author Narim Lee
 */
public interface TaxService {
    /**
     * Sets the tax rate to the passed in tax value
     * @param taxRate the tax rate set for the whole system
     */
    void setTaxRate ( double taxRate );

    /**
     * Return the tax rate used by the system
     *
     * @return tax rate
     */
    double getTaxRate ();
}
