package com.nhom23.orderapp.dto;

import com.nhom23.orderapp.model.Address;
import com.nhom23.orderapp.model.OrderStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Setter
public class OrderDetailsDto {
    private Long id;
    private String phone;
    private String address;
    private List<OrderItemDto> orderItemDtoList;
    private Double price;
    private String name;
    private LocalDateTime createdAt;
    private OrderStatus status;
    private Address location;

    public OrderDetailsDto(Long id, String phone, String address,Double price, String name, LocalDateTime createdAt, OrderStatus status,Address location) {
        this.id = id;
        this.phone = phone;
        this.address = address;
        this.price = price;
        this.name = name;
        this.createdAt = createdAt;
        this.status = status;
        this.location = location;
    }

}
