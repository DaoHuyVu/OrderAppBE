package com.nhom23.orderapp.controller;

import com.nhom23.orderapp.model.Address;
import com.nhom23.orderapp.service.StoreService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.time.LocalTime;

@RestController
@RequestMapping("/api")
public class StoreController {
    @Autowired
    private StoreService service;
    @PostMapping("store")
    public ResponseEntity<?> addStore(
            @RequestParam("city") String city,
            @RequestParam("district") String district,
            @RequestParam("street") String street,
            @RequestParam("opening_time") String openingTime,
            @RequestParam("closing_time") String closingTime
    ){
        Address address = new Address(city,district,street);
        return new ResponseEntity<>(
                service.addStore(
                        address,
                        LocalTime.parse(openingTime),
                        LocalTime.parse(closingTime)),
                HttpStatus.CREATED);
    }
    @GetMapping("store")
    public ResponseEntity<?> getAllStore(){
        return ResponseEntity.ok().body(service.getAllStore());
    }
    @GetMapping("store/{id}")
    public ResponseEntity<?> getStore(@PathVariable Long id){
        return ResponseEntity.ok().body(service.getStore(id));
    }
}
