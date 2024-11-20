package edu.ncsu.csc326.wolfcafe.entity;

import static org.junit.jupiter.api.Assertions.*;

import java.util.HashMap;
import java.util.Map;
import java.util.Date;
import java.text.DateFormat;
import java.text.SimpleDateFormat;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;


@SpringBootTest
@AutoConfigureMockMvc
class OrderTest {

	@Test
	public void testOrder() {
		Map<Long, Integer> itemList = new HashMap<>();
		Item bread = new Item(1L, "bread", "bread item", 1.50);
		Item ham = new Item(2L, "ham", "ham item", 3.25);
		itemList.put(bread.getId(), 2);
		itemList.put(ham.getId(), 3);
		
	    DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
	    Date date = new Date();
	    dateFormat.format(date);
	    Status status = Status.PLACED;

		User ryan = new User(4L, "Ryan", "rthinsha", "rthinsha@ncsu.edu", "password", Role.CUSTOMER);
		Order order = new Order(1L, itemList, ryan.getId(), 0.9, 12.75, status, date);
		
		assertEquals(1L, order.getId());
		assertEquals(2, order.getItemList().size());
		assertEquals(4L, order.getCustomerId());
		assertEquals(0.9, order.getTip());
		assertEquals(12.75, order.getTotalPrice());
		assertEquals(Status.PLACED, order.getStatus());
		
		order.setStatus(Status.FULFILLED);
		assertEquals(Status.FULFILLED, order.getStatus());
	}
}
