package edu.ncsu.csc326.wolfcafe.dto;

import java.util.Date;
import java.util.Map;

import edu.ncsu.csc326.wolfcafe.entity.Status;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class OrderDto {
	private Long id;
	private Map<Long, Integer> itemList;
	private Long customerId;
	private double tip;
	private double totalPrice;
	private Status status;
	private Date date;
	
	public void setStatus(Status status) {
		this.status = status;
	}
}
