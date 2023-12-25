package com.nhom23.orderapp.controller;

import com.nhom23.orderapp.model.Category;
import com.nhom23.orderapp.service.CategoryService;
import com.nhom23.orderapp.service.MenuService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class MenuController {
    private static final Logger logger = LoggerFactory.getLogger(MenuController.class);
    @Autowired
    private MenuService menuService;
    @Autowired
    private CategoryService categoryService;
    @GetMapping("/menu")
    public ResponseEntity<?> getMenu(@RequestParam(value = "category",required = false) String category){
        return ResponseEntity.ok().body(menuService.getMenu(category));
    }
    @GetMapping("/menu/{id}")
    public ResponseEntity<?> getMenuItem(@PathVariable Long id){
        return ResponseEntity.ok().body(menuService.getMenuItem(id));
    }
    @GetMapping("/category")
    public ResponseEntity<?> getCategories(){
        return ResponseEntity.ok().body(categoryService.getCategories());
    }

}
