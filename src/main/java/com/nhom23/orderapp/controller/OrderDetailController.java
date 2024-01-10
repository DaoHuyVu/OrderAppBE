package com.nhom23.orderapp.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.nhom23.orderapp.dto.OrderDetailsDto;
import com.nhom23.orderapp.dto.OrderItemDto;
import com.nhom23.orderapp.dto.StoreDto;
import com.nhom23.orderapp.model.OrderItem;
import com.nhom23.orderapp.service.OrderDetailsService;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.google.gson.Gson;

import java.util.List;

@RestController
@RequestMapping("/api/order")
public class OrderDetailController {
    @Autowired
    private OrderDetailsService orderDetailsService;
    @PostMapping("")
    public ResponseEntity<?> createOrder(
            @RequestParam("phone") String phone,
            @RequestParam("address") String address,
            @RequestParam("orderItemList") String orderItemList,
            @RequestParam("price") String price,
            @RequestParam("userName") String userName,
            @RequestParam("storeDto") String storeDto
    ) throws JsonProcessingException {
        Gson gson = new Gson();
        ObjectMapper mapper = new ObjectMapper();
        return new ResponseEntity<>(orderDetailsService.createOrderDetails(
                phone,
                address,
                mapper.readValue(orderItemList, new TypeReference<>() {}),
                Double.valueOf(price),
                userName,
                gson.fromJson(storeDto,StoreDto.class)
        ), HttpStatus.CREATED);
    }
}
