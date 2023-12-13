package com.nhom23.orderapp.controller;

import com.nhom23.orderapp.dto.OrderDetailsDto;
import com.nhom23.orderapp.service.OrderDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/order")
public class OrderDetailController {
    @Autowired
    private OrderDetailsService orderDetailsService;
    @PostMapping("")
    public ResponseEntity<?> createOrder(@RequestBody OrderDetailsDto orderDetailsDto){
        return new ResponseEntity<>(orderDetailsService.createOrderDetails(orderDetailsDto), HttpStatus.CREATED);
    }
}
