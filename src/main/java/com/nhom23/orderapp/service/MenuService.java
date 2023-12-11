package com.nhom23.orderapp.service;

import com.nhom23.orderapp.exception.NotFoundException;
import com.nhom23.orderapp.model.MenuItem;
import com.nhom23.orderapp.repository.MenuRepository;
import com.nhom23.orderapp.response.MenuResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MenuService {
    @Autowired
    private MenuRepository repository;
    public List<MenuItem> getMenu(String category){
        if(category == null)
            return repository.findAll();
        else
            return repository.findByCategory(category);
    }
    public MenuItem getMenuItem(Long id){
        return repository.findById(id)
                .orElseThrow(() -> new NotFoundException("Item not found"));
    }
}
