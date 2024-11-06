package edu.ncsu.csc326.wolfcafe.entity;

/**
 * User roles.
 */
public enum Role {
    /** Admin role for managing users and tax */
    ADMIN,
    /** Manager role for managing items and inventory. */
    MANAGER,
    /** Barista role for managing inventory. */
    BARISTA,
    /** Customer role for placing orders. */
    CUSTOMER,
    /** Guest role for placing anonymous orders. */
    GUEST

}
