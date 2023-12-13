package com.nhom23.orderapp.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class OrderDetailsDto {
    private String phone;
    private Long storeId;
    private String address;
    private List<OrderItemDto> orderItemDtoList;
    private Double price;
}
