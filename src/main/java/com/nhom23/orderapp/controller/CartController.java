package com.nhom23.orderapp.controller;

import com.nhom23.orderapp.dto.OrderItemDto;
import com.nhom23.orderapp.service.CartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
@SuppressWarnings("unused")
public class CartController {
    @Autowired
    private CartService service;

    @PostMapping("cart")
    public ResponseEntity<OrderItemDto> addItem(@Param("id") Long id,@RequestParam("quantity") Integer quantity){
        return new ResponseEntity<>(service.addItem(id,quantity), HttpStatus.CREATED);
    }
    @GetMapping("cart")
    public ResponseEntity<List<OrderItemDto>> getAllCartItem(){
        return ResponseEntity.ok().body(service.getAllCartItem());
    }
    @PatchMapping("cart/{id}")
    public ResponseEntity<OrderItemDto> patchOrderItem(@PathVariable Long id,@RequestParam("quantity") Integer quantity){
        return ResponseEntity.ok().body(service.modifyQuantity(id,quantity));
    }
    @DeleteMapping("cart/{id}")
    public ResponseEntity<OrderItemDto> deleteItem(@PathVariable Long id){
        return ResponseEntity.ok().body(service.deleteItem(id));
    }
}
