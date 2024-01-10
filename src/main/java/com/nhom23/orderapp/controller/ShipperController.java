package com.nhom23.orderapp.controller;

import com.nhom23.orderapp.request.LoginRequest;
import com.nhom23.orderapp.service.OrderDetailsService;
import com.nhom23.orderapp.service.ShipperService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/shipper")
public class ShipperController {
    @Autowired
    private ShipperService shipperService;
    @Autowired
    private OrderDetailsService orderDetailsService;
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest loginRequest, HttpServletRequest request){
        return ResponseEntity.ok().body(shipperService.login(loginRequest,request.getServerName()));
    }
    @PostMapping("/order/{id}")
    public ResponseEntity<?> informOrder(@PathVariable Long id,@RequestParam("isSucceed") Boolean isSucceed){
        return ResponseEntity.ok().body(shipperService.informOrder(id,isSucceed));
    }
    @GetMapping("order/{id}")
    public ResponseEntity<?> getOrder(@PathVariable Long id){
        return ResponseEntity.ok().body(orderDetailsService.getOrderById(id));
    }
    @GetMapping("order")
    public ResponseEntity<?> getAllOrder(){
        return ResponseEntity.ok().body(shipperService.getAllOrder());
    }
}
