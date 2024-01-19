package com.nhom23.orderapp.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.nhom23.orderapp.dto.OrderDetailsDto;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
public class OrderDetail {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne(fetch = FetchType.LAZY)
    private Customer customer;
    @ManyToOne(fetch = FetchType.LAZY)
    private Staff shipper;
    @ManyToOne(fetch = FetchType.LAZY)
    private Store store;
    private LocalDateTime createdAt;
    private String phone;
    private String address;
    private Double price;
    @Enumerated(value = EnumType.STRING)
    private OrderStatus status;
    public OrderDetailsDto toDto(){
        return new OrderDetailsDto(id,phone,address,price,customer.getUserName(),createdAt,status,store.getAddress());
    }
}
