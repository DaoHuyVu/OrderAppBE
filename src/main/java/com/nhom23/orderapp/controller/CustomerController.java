package com.nhom23.orderapp.controller;

import com.nhom23.orderapp.service.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/customer")
public class CustomerController {
    @Autowired
    private CustomerService customerService;
    @GetMapping("order")
    public ResponseEntity<?> getAllOrder(){
        return ResponseEntity.ok().body(customerService.getAllOrder());
    }
}
