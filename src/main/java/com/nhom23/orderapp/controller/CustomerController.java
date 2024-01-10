package com.nhom23.orderapp.controller;

import com.nhom23.orderapp.service.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/customer")
public class CustomerController {
    @Autowired
    private CustomerService customerService;
    @GetMapping("order")
    public ResponseEntity<?> getAllOrder(){
        return ResponseEntity.ok().body(customerService.getAllOrder());
    }
    @PostMapping("order/{id}")
    public ResponseEntity<?> cancelOrder(@PathVariable Long id){
        return ResponseEntity.ok().body(customerService.cancelOrder(id));
    }
    @PostMapping("password")
    public ResponseEntity<?> changePassword(
            @RequestParam() String oldPassword,
            @RequestParam() String newPassword
    ){
        customerService.changePassword(oldPassword,newPassword);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
