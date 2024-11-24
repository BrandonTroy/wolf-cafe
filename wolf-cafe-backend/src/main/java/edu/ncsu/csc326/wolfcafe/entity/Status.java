package edu.ncsu.csc326.wolfcafe.entity;

/**
 * Order status
 */
public enum Status {
	/** Status for when a customer has placed an Order and the Inventory has been updated */
	PLACED,
	/** Status for when a staff member has fulfilled the Order */
	FULFILLED,
	/** Status for when a customer has picked up their fulfilled Orders */
	PICKEDUP,
	/** Status for when a customer cancels their Order */
	CANCELED
}
