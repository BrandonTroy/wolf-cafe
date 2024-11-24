package edu.ncsu.csc326.wolfcafe.entity;

import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Creates entries for Items that get stored in the Inventory
 */
@Entity
@Table(name = "inventory")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class InventoryEntry {
    /** The unique Inventory entry's id */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /** The Item to be stored in the Inventory as an InventoryEntry */
    @OneToOne
    @JoinColumn(name = "item_id", nullable = false, unique = true)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Item item;

    /** The amount of the item in the Inventory */
    @Column(nullable = false)
    private int quantity;

    /**
     * Constructor for the InventoryEntry
     * @param item The item to be stored in the Inventory
     * @param quantity The amount of Item to be stored in the Inventory
     */
    public InventoryEntry(Item item, int quantity) {
        this.item = item;
        this.quantity = quantity;
    }
}