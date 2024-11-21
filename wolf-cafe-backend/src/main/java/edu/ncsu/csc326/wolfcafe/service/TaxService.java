package edu.ncsu.csc326.wolfcafe.service;

/**
 * Interface for implementing a tax service that includes the functionality to
 * set and get a tax rate
 *
 * @author Narim Lee
 */
public interface TaxService {
    /**
     *
     * @param taxRate
     * @return set the tax rate to the given value
     */
    void setTaxRate ( double taxRate );

    /**
     * Return the tax rate used by the system
     *
     * @return tax rate
     */
    double getTaxRate ();
}
