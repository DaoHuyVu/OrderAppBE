package com.nhom23.orderapp.controller;

import com.nhom23.orderapp.model.Gender;
import com.nhom23.orderapp.request.LoginRequest;
import com.nhom23.orderapp.response.AuthResponse;
import com.nhom23.orderapp.service.ManagerService;
import com.nhom23.orderapp.service.ShipperService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.ParseException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

@RestController
@RequestMapping("/api/manager")
public class ManagerController {
    @Autowired
    private ManagerService managerService;
    @Autowired
    private ShipperService shipperService;
    @GetMapping("shipper")
    public ResponseEntity<?> getAllShipper(){
        return ResponseEntity.ok().body(shipperService.getAllShipper());
    }
    @PostMapping("login")
    public ResponseEntity<?> login(@RequestBody LoginRequest loginRequest){
        AuthResponse authResponse = managerService.login(loginRequest);
        return new ResponseEntity<>(authResponse,HttpStatus.OK);
    }
    @PostMapping("shipper")
    public ResponseEntity<?> addShipper(
            @RequestParam("email") String email,
            @RequestParam("password")String password,
            @RequestParam("name")String name,
            @RequestParam("phone")String phone,
            @RequestParam("salary")String salary,
            @RequestParam("birthday")String dateOfBirth,
            @RequestParam("gender")String gender
    ) throws ParseException {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
        NumberFormat numberFormat = DecimalFormat.getCurrencyInstance();
        return new ResponseEntity<>(shipperService.addShipper(
                email, password, name, phone, numberFormat.format(Double.valueOf(salary)), LocalDate.parse(dateOfBirth,formatter), gender),
                HttpStatus.CREATED);
    }

}
