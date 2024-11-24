package edu.ncsu.csc326.wolfcafe.entity;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;

import edu.ncsu.csc326.wolfcafe.dto.OrderDto;

@SpringBootTest
@AutoConfigureMockMvc
class OrderTest {

    @Test
    public void testOrder () {
        final Map<Long, Integer> itemList = new HashMap<>();
        final Item bread = new Item( 1L, "bread", "bread item", 1.50 );
        final Item ham = new Item( 2L, "ham", "ham item", 3.25 );
        itemList.put( bread.getId(), 2 );
        itemList.put( ham.getId(), 3 );

        final Date date = new Date();
        final SimpleDateFormat formatter = new SimpleDateFormat( "yyyy-MM-dd HH:mm:ss" );
        final String d = formatter.format( date );
        final Status status = Status.PLACED;

        final User ryan = new User( 4L, "Ryan", "rthinsha", "rthinsha@ncsu.edu", "password", Role.CUSTOMER );
        final Order order = new Order( 1L, itemList, ryan.getId(), 12.75, 0.26, 0.9, status, d );

        assertEquals( 1L, order.getId() );
        assertEquals( 2, order.getItemList().size() );
        assertEquals( 4L, order.getCustomerId() );
        assertEquals( 0.9, order.getTip() );
        assertEquals( 12.75, order.getPrice() );
        assertEquals( 0.26, order.getTax() );
        assertEquals( Status.PLACED, order.getStatus() );

        order.setStatus( Status.FULFILLED );
        assertEquals( Status.FULFILLED, order.getStatus() );
        
        OrderDto orderDto = new OrderDto(itemList, 0.4);
        assertEquals(2, orderDto.getItemList().size());
        assertEquals(0.4, orderDto.getTip());
    }
}
