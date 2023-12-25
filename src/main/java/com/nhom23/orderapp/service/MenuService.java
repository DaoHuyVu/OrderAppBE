package com.nhom23.orderapp.service;

import com.nhom23.orderapp.dto.MenuItemDto;
import com.nhom23.orderapp.exception.AlreadyExistException;
import com.nhom23.orderapp.exception.NotFoundException;
import com.nhom23.orderapp.model.Category;
import com.nhom23.orderapp.model.ItemCategory;
import com.nhom23.orderapp.model.MenuItem;
import com.nhom23.orderapp.repository.CategoryRepository;
import com.nhom23.orderapp.repository.ItemCategoryRepository;
import com.nhom23.orderapp.repository.MenuRepository;
import com.nhom23.orderapp.response.MenuResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.awt.*;
import java.text.NumberFormat;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
public class MenuService {
    @Autowired
    private CategoryRepository categoryRepository;
    @Autowired
    private MenuRepository menuRepository;
    @Autowired
    private ItemCategoryRepository itemCategoryRepository;
    public List<MenuItem> getMenu(String category){
        if(category == null)
            return menuRepository.findAll();
        else
            return menuRepository.findByCategory(category);
    }
    public MenuItem getMenuItem(Long id){
        return menuRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Item not found"));
    }
    @Transactional
    public MenuItemDto addMenuItem(
            String name,
            String price,
            String description,
            String imageUrl,
            List<Category> categories){
        Optional<MenuItem> menuItem = menuRepository.findByName(name);
        if(menuItem.isPresent()){
            throw new AlreadyExistException("Item already exist");
        }
        MenuItem item = new MenuItem(name,Double.valueOf(price),description,imageUrl);
        categories.forEach(c -> {
            ItemCategory itemCategory = new ItemCategory();
            itemCategory.setItem(item);
            itemCategory.setCategory(c);
            itemCategoryRepository.save(itemCategory);
        });
        menuRepository.save(item);

        return new MenuItemDto(item.getId(),name,price,description,imageUrl,categories);
    }
    @Transactional
    public Category addCategory(String name,String imageUrl){
        return categoryRepository.save(new Category(name,imageUrl));
    }
    @Transactional
    public MenuItem deleteMenuItem(Long id){
        return menuRepository.deleteMenuItem(id);
    }
    @Transactional
    public MenuItemDto updateMenuItem(
            Long id,
            String name,
            String price,
            String description,
            String imageUrl,
            List<Category> categories
    ){
        MenuItem item = menuRepository.findById(id).orElseThrow(() -> new NotFoundException("Item not found"));
        item.setName(name);
        item.setDescription(description);
        NumberFormat formatter = NumberFormat.getCurrencyInstance();
        formatter.setMaximumFractionDigits(0);
        item.setPrice(Double.valueOf(price));
        item.setImageUrl(imageUrl);
        itemCategoryRepository.deleteByItemId(id);
        ItemCategory itemCategory = new ItemCategory();
        itemCategory.setItem(item);
        categories.forEach(itemCategory::setCategory);
        return new MenuItemDto(id,name,price,description,imageUrl,categories);
    }
}
