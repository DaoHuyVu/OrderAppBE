package com.nhom23.orderapp.model;

import com.fasterxml.jackson.annotation.JsonFormat;
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
    private Shipper shipper;
    @ManyToOne(fetch = FetchType.LAZY)
    private Store store;
    @JsonFormat(shape = JsonFormat.Shape.STRING,pattern = "DD/MM/YYYY HH:mm:ss",timezone = "UTC")
    private LocalDateTime createdAt;
    private String phone;
    private String address;
    private Double price;
    @Enumerated(value = EnumType.STRING)
    private OrderStatus status;

}
