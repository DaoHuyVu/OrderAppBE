package com.nhom23.orderapp.controller;

import com.nhom23.orderapp.request.LoginRequest;
import com.nhom23.orderapp.service.ShipperService;
import org.hibernate.StatelessSessionBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.swing.text.DateFormatter;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;

@RestController
@RequestMapping("/api/shipper")
public class ShipperController {
    @Autowired
    private ShipperService shipperService;
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest loginRequest){
        return ResponseEntity.ok().body(shipperService.login(loginRequest));
    }
    @PostMapping("")
    public ResponseEntity<?> getAllShipper(){
        return ResponseEntity.ok().body(shipperService.getAllShipper());
    }
}
