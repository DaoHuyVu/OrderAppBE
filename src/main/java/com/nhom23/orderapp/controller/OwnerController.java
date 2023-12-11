package com.nhom23.orderapp.controller;

import com.nhom23.orderapp.request.LoginRequest;
import com.nhom23.orderapp.service.ManagerService;
import com.nhom23.orderapp.service.OwnerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
@RestController
@RequestMapping("/api/owner")
public class OwnerController {
    @Autowired
    private ManagerService managerService;
    @Autowired
    private OwnerService ownerService;
    @PostMapping("manager")
    public ResponseEntity<?> addManager(
            @RequestParam("email") String email,
            @RequestParam("password")String password,
            @RequestParam("name")String name,
            @RequestParam("phone")String phone,
            @RequestParam("salary")String salary,
            @RequestParam("birthday")String dateOfBirth,
            @RequestParam("gender")String gender,
            @RequestParam("storeId") String storeId
    ){
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
        return new ResponseEntity<>(managerService.addManagerAccount(
                email, password, name, phone, Long.valueOf(storeId),Integer.valueOf(salary), LocalDate.parse(dateOfBirth,formatter), gender),
                HttpStatus.CREATED);
    }
    @PostMapping("")
    public ResponseEntity<?> login(@RequestBody LoginRequest loginRequest){
        return ResponseEntity.ok().body(ownerService.login(loginRequest));
    }
}
