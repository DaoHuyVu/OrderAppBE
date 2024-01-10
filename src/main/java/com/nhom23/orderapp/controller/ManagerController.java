package com.nhom23.orderapp.controller;

import com.nhom23.orderapp.request.LoginRequest;
import com.nhom23.orderapp.response.AuthResponse;
import com.nhom23.orderapp.service.ManagerService;
import com.nhom23.orderapp.service.OrderDetailsService;
import com.nhom23.orderapp.service.ShipperService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/manager")
public class ManagerController {
    @Autowired
    private ManagerService managerService;
    @Autowired
    private ShipperService shipperService;
    @Autowired
    private OrderDetailsService orderDetailsService;
    @GetMapping("shipper")
    public ResponseEntity<?> getAllShipper(){
        return ResponseEntity.ok().body(shipperService.getAllShipperOfStore());
    }
    @PostMapping("login")
    public ResponseEntity<?> login(@RequestBody LoginRequest loginRequest, HttpServletRequest request){
        AuthResponse authResponse = managerService.login(loginRequest,request.getServerName());
        return new ResponseEntity<>(authResponse,HttpStatus.OK);
    }
    // Get all newly-created orders of a store
    @GetMapping("order")
    public ResponseEntity<?> getAllNewlyCreatedOrders(){
        return ResponseEntity.ok().body(orderDetailsService.getAllOrder());
    }
    @PostMapping("order/{id}")
    public ResponseEntity<?> delegateJob(@PathVariable Long id,@RequestParam("id") Long shipperId){
        return ResponseEntity.ok().body(orderDetailsService.delegateJob(id,shipperId));
    }
    @GetMapping("order/{id}")
    public ResponseEntity<?> getOrder(@PathVariable Long id){
        return ResponseEntity.ok().body(orderDetailsService.getOrderById(id));
    }
}
