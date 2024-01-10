package com.nhom23.orderapp.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.nhom23.orderapp.model.Address;
import com.nhom23.orderapp.model.Category;
import com.nhom23.orderapp.repository.ItemCategoryRepository;
import com.nhom23.orderapp.service.*;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.ParseException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/admin")
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
    private ManagerService managerService;
    @Autowired
    private AdminService adminService;
    @Autowired
    private ShipperService shipperService;
    @Autowired
    private RevenueService revenueService;
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
        return new ResponseEntity<>(managerService.addManagerAccount(
                email, password, name, phone,
                Long.valueOf(storeId),
                Double.valueOf(salary),
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
        numberFormat.setMaximumFractionDigits(0);
        return new ResponseEntity<>(shipperService.addShipper(
                email, password, name, phone,
                Double.valueOf(salary),
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
            @RequestParam("imageUrl") String imageUrl
    ){
        return new ResponseEntity<>(menuService.addCategory(name,imageUrl),HttpStatus.CREATED);
    }
    @PostMapping("menu")
    public ResponseEntity<?> addMenuItem(
            @RequestParam("name") String name,
            @RequestParam("price") String price,
            @RequestParam("description") String description,
            @RequestParam("imageUrl") String imageUrl,
            @RequestParam("categories") String categoriesStr
    ) throws JsonProcessingException {
        List<Category> categories = objectMapper.readValue(categoriesStr, new TypeReference<>() {});
        return new ResponseEntity<>(
                menuService.addMenuItem(name,price,description,imageUrl,categories),HttpStatus.CREATED
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
        return ResponseEntity.ok().body(managerService.getAllManager());
    }
    @GetMapping("shipper")
    public ResponseEntity<?> getAllShipper(){
        return ResponseEntity.ok().body(shipperService.getAllShipper());
    }
    @GetMapping("store")
    public ResponseEntity<?> getAllStore(@RequestParam(required = false) Boolean isManaged ){
        return ResponseEntity.ok().body(storeService.getAllStore(isManaged));
    }
    @DeleteMapping("category/{id}")
    public ResponseEntity<?> deleteCategory(@PathVariable("id") Long id){
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
        return ResponseEntity.ok().body(shipperService.deleteShipper(id));
    }
    @DeleteMapping("manager/{id}")
    public ResponseEntity<?> deleteManager(@PathVariable("id") Long id){
        return ResponseEntity.ok().body(managerService.deleteManager(id));
    }
    @PutMapping("manager/{id}")
    public ResponseEntity<?> updateManager(
            @PathVariable("id") Long id,
            @RequestParam("email") String email,
            @RequestParam("name")String name,
            @RequestParam("phone")String phone,
            @RequestParam("salary")String salary,
            @RequestParam("dateOfBirth")String dateOfBirth,
            @RequestParam("gender")String gender
    ){
        return ResponseEntity.ok().body(managerService.updateManager(id,email,name,phone,salary,dateOfBirth,gender));
    }
    @PutMapping("shipper/{id}")
    public ResponseEntity<?> updateShipper(
            @PathVariable("id") Long id,
            @RequestParam("email") String email,
            @RequestParam("name")String name,
            @RequestParam("phone")String phone,
            @RequestParam("salary")String salary,
            @RequestParam("dateOfBirth")String dateOfBirth,
            @RequestParam("gender")String gender
    ){
        return ResponseEntity.ok().body(shipperService.updateShipper(id,email,name,phone,salary,dateOfBirth,gender));
    }
    @PutMapping("store/{id}")
    public ResponseEntity<?> updateStore(
            @PathVariable("id") Long id,
            @RequestParam("city") String city,
            @RequestParam("district") String district,
            @RequestParam("street") String street,
            @RequestParam("openingTime") String openingTime,
            @RequestParam("closingTime") String closingTime
    ){
        Address address = new Address(city,district,street);
        return ResponseEntity.ok().body(
                storeService.updateStore(
                        id,
                        address,
                        LocalTime.parse(openingTime),
                        LocalTime.parse(closingTime))
        );
    }
    @PutMapping("category/{id}")
    public ResponseEntity<?> updateCategory(
            @PathVariable("id") Long id,
            @RequestParam("name") String name,
            @RequestParam("imageUrl") String imageUrl
    ){
        return ResponseEntity.ok().body(categoryService.updateCategory(id,name,imageUrl));
    }
    @PatchMapping("manager/{id}")
    public ResponseEntity<?> updateManager(
            @PathVariable("id") Long id,
            @RequestParam("fields") String f
    ) throws JsonProcessingException {
        Map<String,String> fields = objectMapper.readValue(f, new TypeReference<>() {});
        return ResponseEntity.ok().body(managerService.updateManager(id,fields));
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
        return ResponseEntity.ok().body(shipperService.updateShipper(id,fields));
    }
    @PatchMapping("menu/{id}")
    public ResponseEntity<?> updateMenuItem(
            @PathVariable("id") Long id,
            @RequestParam("fields") String f
    ) throws JsonProcessingException {
        Map<String,String> fields = objectMapper.readValue(f, new TypeReference<>() {});
        return ResponseEntity.ok().body(menuService.updateMenuItem(id,fields));
    }
    @GetMapping("revenue")
    public ResponseEntity<?> getRevenue(
            @RequestParam(required = false) Integer day,
            @RequestParam(required = false) Integer month,
            @RequestParam Integer year,
            @RequestParam(required = false) String category
    ){
        if(category != null)
            return ResponseEntity.ok().body(revenueService.getRevenueOfCategory(day,month,year,category));
        return ResponseEntity.ok().body(revenueService.getRevenue(day,month,year));
    }
    @PostMapping("/test")
    public String getImage(
            @RequestParam MultipartFile image,
            @RequestParam String fileName
    ) {
        return menuService.test(image,fileName);
    }
}