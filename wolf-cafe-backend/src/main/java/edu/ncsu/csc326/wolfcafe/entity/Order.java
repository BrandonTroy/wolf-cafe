package edu.ncsu.csc326.wolfcafe.entity;

import java.util.Map;

import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.MapKeyColumn;
import jakarta.persistence.Table;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Getter;


@AllArgsConstructor
@NoArgsConstructor
@Entity
@Getter
@Table(name = "orders")
public class Order {
	
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
    
    /** a map of order items and amounts */
    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "order_items", 
            joinColumns = @JoinColumn(name = "order_id"))
    @MapKeyColumn(name = "item_id")
    @Column(name = "item_amount")
	private Map<Long, Integer> itemList;
    
	@Column ( nullable = false)
	private Long customerId;
	@Column
	private double tip;
	@Column ( nullable = false)
	private double totalPrice;
	
	@Column ( nullable = false )
	private Status status;
	
	@Column ( nullable = false )
	private String date;

//	private InventoryRepository inventoryRepository;
	
//	private double calculateTotalPrice() {
//		double total = 0.0;
//		for (Entry<Long, Integer> itemEntry : itemList.entrySet()) {
//			Long id = itemEntry.getKey();
//			Optional<InventoryEntry> item = inventoryRepository.findByItemId(id);
//			double itemPrice = item.get().getItem().getPrice();
//			total += itemPrice;
//		}
//		total = total + tip;
//		return total;
//	}
	
	public void setStatus(Status status) {
		this.status = status;
	}
}
