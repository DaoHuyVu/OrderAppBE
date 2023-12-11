package com.nhom23.orderapp.model;

import com.nhom23.orderapp.dto.OrderItemDto;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
public class OrderItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Integer quantity;
    @ManyToOne(fetch = FetchType.LAZY)
    private Customer customer;
    @ManyToOne(fetch = FetchType.LAZY)
    private MenuItem item;
    @ManyToOne(fetch = FetchType.LAZY)
    private OrderDetail orderDetail;

    public OrderItemDto toItemDto(){
        return new OrderItemDto(
                id,
                item.getName(),
                quantity,
                item.getPrice(),
                item.getImageUrl());
    }

}
