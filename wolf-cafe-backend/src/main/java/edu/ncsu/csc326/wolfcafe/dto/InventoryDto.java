package edu.ncsu.csc326.wolfcafe.dto;

import java.util.HashMap;
import java.util.Map;

import lombok.Data;

@Data
public class InventoryDto {
    private Map<Long, Integer> itemQuantities = new HashMap<>();
}