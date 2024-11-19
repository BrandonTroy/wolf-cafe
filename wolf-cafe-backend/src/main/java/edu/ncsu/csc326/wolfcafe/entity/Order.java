//package edu.ncsu.csc326.wolfcafe.entity;
//
//import java.util.Date;
//import java.util.HashMap;
//import java.util.Map;
//import java.util.Map.Entry;
//import java.util.Optional;
//
//import edu.ncsu.csc326.wolfcafe.repository.InventoryRepository;
//import jakarta.persistence.CollectionTable;
//import jakarta.persistence.Column;
//import jakarta.persistence.ElementCollection;
//import jakarta.persistence.Entity;
//import jakarta.persistence.FetchType;
//import jakarta.persistence.GeneratedValue;
//import jakarta.persistence.GenerationType;
//import jakarta.persistence.Id;
//import jakarta.persistence.JoinColumn;
//import jakarta.persistence.MapKeyColumn;
//import jakarta.persistence.Table;
//import lombok.NoArgsConstructor;
//import lombok.Data;
//
//
//@NoArgsConstructor
//@Entity
//@Data
//@Table(name = "orders")
//public class Order {
//	
//    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
//	private Long id;
//    
//    /** a map of order items and amounts */
//    @ElementCollection(fetch = FetchType.EAGER)
//    @CollectionTable(name = "order_items", 
//            joinColumns = @JoinColumn(name = "item_id"))
//    @MapKeyColumn(name = "item_id")
//    @Column(name = "item_amount")
//	private Map<Long, Integer> itemList = new HashMap<>();
//    
//	@Column ( nullable = false)
//	private Long customerId;
//	@Column
//	private double tip;
//	@Column ( nullable = false)
//	private double totalPrice;
//	
//	@Column ( nullable = false )
//	private Status status;
//	
//	@Column ( nullable = false )
//	private Date date;
//
////	private InventoryRepository inventoryRepository;
//	
//	public Order(Long id, Map<Long, Integer> itemList, Long customerId, double tip, Date date, double totalPrice, Status status) {
//		this.id = id;
//		this.itemList = itemList;
//		this.customerId = customerId;
//		this.tip = tip;
//		this.date = date;
//		this.totalPrice = totalPrice;
//		this.status = Status.PLACED;
//	}
//	
////	private double calculateTotalPrice() {
////		double total = 0.0;
////		for (Entry<Long, Integer> itemEntry : itemList.entrySet()) {
////			Long id = itemEntry.getKey();
////			Optional<InventoryEntry> item = inventoryRepository.findByItemId(id);
////			double itemPrice = item.get().getItem().getPrice();
////			total += itemPrice;
////		}
////		total = total + tip;
////		return total;
////	}
//}
