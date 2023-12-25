package com.nhom23.orderapp.controller;

import com.nhom23.orderapp.request.LoginRequest;
import com.nhom23.orderapp.service.ShipperService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/shipper")
public class ShipperController {
    @Autowired
    private ShipperService shipperService;
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest loginRequest){
        return ResponseEntity.ok().body(shipperService.login(loginRequest));
    }
    @PostMapping("/order/{id}")
    public ResponseEntity<?> informOrder(@PathVariable Long id,@RequestParam("isSucceed") Boolean isSucceed){
        return ResponseEntity.ok().body(shipperService.informOrder(id,isSucceed));
    }
    @GetMapping("order")
    public ResponseEntity<?> getAllOrder(){
        return ResponseEntity.ok().body(shipperService.getAllOrder());
    }
    @PostMapping("")
    public ResponseEntity<?> getAllShipper(){
        return ResponseEntity.ok().body(shipperService.getAllShipperOfStore());
    }
}
