package edu.ncsu.csc326.wolfcafe.entity;

import static org.junit.jupiter.api.Assertions.*;

import java.util.HashMap;
import java.util.Map;
import java.util.Date;
import java.text.DateFormat;
import java.text.SimpleDateFormat;

import org.junit.jupiter.api.Test;

import edu.ncsu.csc326.wolfcafe.repository.InventoryRepository;

class OrderTest {
	
	private InventoryRepository inventoryRepository;

	@Test
	public void testOrder() {
		Map<Long, Integer> itemList = new HashMap<>();
		Item bread = new Item(1L, "bread", "bread item", 1.50);
		Item ham = new Item(2L, "ham", "ham item", 3.25);
		
		InventoryEntry breadEntry = new InventoryEntry(bread, 10);
		InventoryEntry hamEntry = new InventoryEntry(ham, 20);
		inventoryRepository.save(breadEntry);
		inventoryRepository.save(hamEntry);
		
		itemList.put(bread.getId(), 2);
		itemList.put(ham.getId(), 3);
		
	    DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
	    Date date = new Date();
	    dateFormat.format(date);
	    //In the constructor I have it set to default to Status.PLACED so I'm just checking that part of it
	    Status status = Status.FULFILLED;

		User ryan = new User(4L, "Ryan", "rthinsha", "rthinsha@ncsu.edu", "password", Role.CUSTOMER);
		Order order = new Order(1L, itemList, ryan.getId(), 0.9, date, 0.0, status);
		
		assertEquals(1L, order.getId());
		assertEquals(2, order.getItemList().size());
		assertEquals(4L, order.getCustomerId());
		assertEquals(0.9, order.getTip());
		assertEquals(Status.FULFILLED, order.getStatus());
	}

}
