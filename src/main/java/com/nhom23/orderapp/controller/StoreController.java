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
    @GetMapping("store")
    public ResponseEntity<?> getAllStore(){
        return ResponseEntity.ok().body(service.getAllStore());
    }
    @GetMapping("store/{id}")
    public ResponseEntity<?> getStore(@PathVariable Long id){
        return ResponseEntity.ok().body(service.getStore(id));
    }
}
