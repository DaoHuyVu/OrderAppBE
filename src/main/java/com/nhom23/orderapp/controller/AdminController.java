package com.nhom23.orderapp.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.nhom23.orderapp.model.Address;
import com.nhom23.orderapp.model.Category;
import com.nhom23.orderapp.repository.ItemCategoryRepository;
import com.nhom23.orderapp.service.*;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/admin")
@SuppressWarnings("unused")
public class AdminController {
    @Autowired
    private CategoryService categoryService;
    private final ObjectMapper objectMapper = new ObjectMapper();
    @Autowired
    private ItemCategoryRepository itemCategoryRepository;
    @Autowired
    private MenuService menuService;
    @Autowired
    private StoreService storeService;
    @Autowired
    private StaffService staffService;
    @Autowired
    private AdminService adminService;
    @Autowired
    private RevenueService revenueService;
    @PostMapping("staff")
    public ResponseEntity<?> addStaff(
            @RequestParam("email") String email,
            @RequestParam("password")String password,
            @RequestParam("name")String name,
            @RequestParam("phone")String phone,
            @RequestParam("salary")String salary,
            @RequestParam("dateOfBirth")String dateOfBirth,
            @RequestParam("gender")String gender,
            @RequestParam("storeId") String storeId,
            @RequestParam("role") String role
    ){
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        HttpServletRequest request;

        return new ResponseEntity<>(staffService.addStaff(
                email, password, name, phone,
                Long.valueOf(storeId),
                Double.valueOf(salary),
                LocalDate.parse(dateOfBirth,formatter),
                gender,role),
                HttpStatus.CREATED);
    }
    @PostMapping("store")
    public ResponseEntity<?> addStore(
            @RequestParam("city") String city,
            @RequestParam("district") String district,
            @RequestParam("street") String street,
            @RequestParam("openingTime") String openingTime,
            @RequestParam("closingTime") String closingTime
    ){
        Address address = new Address(city,district,street);
        return new ResponseEntity<>(
                storeService.addStore(
                        address,
                        LocalTime.parse(openingTime),
                        LocalTime.parse(closingTime)),
                HttpStatus.CREATED);
    }

    @PostMapping("category")
    public ResponseEntity<?> addCategory(
            @RequestParam("name") String name,
            @RequestParam("image") MultipartFile image
    ) throws IOException {
        return new ResponseEntity<>(categoryService.addCategory(name,image),HttpStatus.CREATED);
    }
    @PostMapping("menu")
    public ResponseEntity<?> addMenuItem(
            @RequestParam String name,
            @RequestParam String price,
            @RequestParam String description,
            @RequestParam MultipartFile image,
            @RequestParam("categories") String categoriesStr
    ) throws JsonProcessingException {
        List<Category> categories = objectMapper.readValue(categoriesStr, new TypeReference<>() {});
        return new ResponseEntity<>(
                menuService.addMenuItem(name,price,description,image,categories),HttpStatus.CREATED
        );
    }

    @PostMapping("login")
    public ResponseEntity<?> login(
            @RequestParam("userName") String userName,
            @RequestParam("password") String password,
            HttpServletRequest request
    ){
        return ResponseEntity.ok().body(adminService.login(userName,password,request.getServerName()));
    }
    @GetMapping("menu")
    public ResponseEntity<?> getAllMenuItemDto(@RequestParam(required = false) String category) {
        return ResponseEntity.ok().body(menuService.getAllMenuItemDto(category));
    }
    @GetMapping("manager")
    public ResponseEntity<?> getAllManager(){
        return ResponseEntity.ok().body(staffService.getAllManager());
    }
    @GetMapping("shipper")
    public ResponseEntity<?> getAllShipper(){
        return ResponseEntity.ok().body(staffService.getAllShipper());
    }
    @GetMapping("store")
    public ResponseEntity<?> getAllStore(){
        return ResponseEntity.ok().body(storeService.getAllStore());
    }
    @DeleteMapping("category/{id}")
    public ResponseEntity<?> deleteCategory(@PathVariable("id") Long id) throws IOException {
        return ResponseEntity.ok().body(categoryService.deleteCategory(id));
    }
    @DeleteMapping("store/{id}")
    public ResponseEntity<?> deleteStore(@PathVariable("id") Long id){
        return ResponseEntity.ok().body(storeService.deleteStore(id));
    }
    @DeleteMapping("menu/{id}")
    public ResponseEntity<?> deleteMenuItem(@PathVariable("id") Long id){
        return ResponseEntity.ok().body(menuService.deleteMenuItem(id));
    }
    @DeleteMapping("shipper/{id}")
    public ResponseEntity<?> deleteShipper(@PathVariable("id") Long id){
        return ResponseEntity.ok().body(staffService.deleteShipper(id));
    }
    @DeleteMapping("manager/{id}")
    public ResponseEntity<?> deleteManager(@PathVariable("id") Long id){
        return ResponseEntity.ok().body(staffService.deleteManager(id));
    }

    @PatchMapping("category/{id}")
    public ResponseEntity<?> updateCategory(
            @PathVariable("id") Long id,
            @RequestParam(required = false) String categoryName,
            @RequestParam(required = false) MultipartFile image
    ) {
        return ResponseEntity.ok().body(categoryService.updateCategory(id,categoryName,image));
    }
    @PatchMapping("manager/{id}")
    public ResponseEntity<?> updateManager(
            @PathVariable("id") Long id,
            @RequestParam("fields") String f
    ) throws JsonProcessingException {
        Map<String,String> fields = objectMapper.readValue(f, new TypeReference<>() {});
        return ResponseEntity.ok().body(staffService.updateStaff(id,fields));
    }
    @PatchMapping("store/{id}")
    public ResponseEntity<?> updateStore(
            @PathVariable("id") Long id,
            @RequestParam("fields") String f
    ) throws JsonProcessingException {
        Map<String,String> fields = objectMapper.readValue(f, new TypeReference<>() {});
        return ResponseEntity.ok().body(storeService.updateStore(id,fields));
    }
    @PatchMapping("shipper/{id}")
    public ResponseEntity<?> updateShipper(
            @PathVariable("id") Long id,
            @RequestParam("fields") String f
    ) throws JsonProcessingException {
        Map<String,String> fields = objectMapper.readValue(f, new TypeReference<>() {});
        return ResponseEntity.ok().body(staffService.updateStaff(id,fields));
    }
    @PatchMapping("menu/{id}")
    public ResponseEntity<?> updateMenuItem(
            @PathVariable("id") Long id,
            @RequestParam("fields") String f,
            @RequestParam(required = false) MultipartFile image
    ) throws JsonProcessingException {
        Map<String,String> fields = objectMapper.readValue(f, new TypeReference<>() {});
        return ResponseEntity.ok().body(menuService.updateMenuItem(id,fields,image));
    }

    @GetMapping("revenue")
    public ResponseEntity<?> getRevenue(
            @RequestParam String from,
            @RequestParam String to,
            @RequestParam(required = false) String menuItem
    ){
        if(menuItem != null)
            return ResponseEntity.ok().body(revenueService.getRevenueOfMenuItem(from,to,menuItem));
        return ResponseEntity.ok().body(revenueService.getRevenue(from,to));

    }

}