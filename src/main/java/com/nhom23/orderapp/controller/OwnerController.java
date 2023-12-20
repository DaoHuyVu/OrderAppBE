package com.nhom23.orderapp.controller;

import com.nhom23.orderapp.model.Address;

import com.nhom23.orderapp.service.ManagerService;
import com.nhom23.orderapp.service.OwnerService;
import com.nhom23.orderapp.service.ShipperService;
import com.nhom23.orderapp.service.StoreService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.ParseException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

@Controller
@RequestMapping("/admin")
public class OwnerController {
    @Autowired
    private StoreService storeService;
    @Autowired
    private ManagerService managerService;
    @Autowired
    private OwnerService ownerService;
    @Autowired
    private ShipperService shipperService;
    @PostMapping("manager")
    public ResponseEntity<?> addManager(
            @RequestParam("email") String email,
            @RequestParam("password")String password,
            @RequestParam("name")String name,
            @RequestParam("phone")String phone,
            @RequestParam("salary")String salary,
            @RequestParam("dateOfBirth")String dateOfBirth,
            @RequestParam("gender")String gender,
            @RequestParam("storeId") String storeId
    ){
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        NumberFormat numberFormat = NumberFormat.getCurrencyInstance();
        return new ResponseEntity<>(managerService.addManagerAccount(
                email, password, name, phone,
                Long.valueOf(storeId),
                numberFormat.format(Double.valueOf(salary)),
                LocalDate.parse(dateOfBirth,formatter),
                gender),
                HttpStatus.CREATED);
    }
    @PostMapping("shipper")
    public ResponseEntity<?> addShipper(
            @RequestParam("email") String email,
            @RequestParam("password")String password,
            @RequestParam("name")String name,
            @RequestParam("phone")String phone,
            @RequestParam("salary")String salary,
            @RequestParam("dateOfBirth")String dateOfBirth,
            @RequestParam("gender")String gender,
            @RequestParam("storeId") String storeId
    ) throws ParseException {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        NumberFormat numberFormat = DecimalFormat.getCurrencyInstance();
        return new ResponseEntity<>(shipperService.addShipper(
                email, password, name, phone,
                numberFormat.format(Double.valueOf(salary)),
                LocalDate.parse(dateOfBirth,formatter),
                gender,
                Long.valueOf(storeId)),
                HttpStatus.CREATED);
    }
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
                storeService.addStore(
                        address,
                        LocalTime.parse(openingTime),
                        LocalTime.parse(closingTime)),
                HttpStatus.CREATED);
    }
    @PostMapping("login")
    public ResponseEntity<?> login(
            @RequestParam("userName") String userName,
            @RequestParam("password") String password
    ){
        return ResponseEntity.ok().body(ownerService.login(userName,password));
    }
    @GetMapping("manager")
    public ResponseEntity<?> getAllManager(){
        return ResponseEntity.ok().body(ownerService.getAllManager());
    }
    @GetMapping("shipper")
    public ResponseEntity<?> getAllShipper(){
        return ResponseEntity.ok().body(ownerService.getAllShipper());
    }
    @GetMapping("menu")
    public ResponseEntity<?> getAllMenuItem(){
        return ResponseEntity.ok().body(ownerService.getAllItem());
    }
    @GetMapping("store")
    public ResponseEntity<?> getAllStore(){
        return ResponseEntity.ok().body(ownerService.getAllStore());
    }

}
