package com.nhom23.orderapp.service;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.nhom23.orderapp.dto.MenuItemDto;
import com.nhom23.orderapp.exception.AlreadyExistException;
import com.nhom23.orderapp.exception.NotFoundException;
import com.nhom23.orderapp.model.Category;
import com.nhom23.orderapp.model.ItemCategory;
import com.nhom23.orderapp.model.MenuItem;
import com.nhom23.orderapp.repository.ItemCategoryRepository;
import com.nhom23.orderapp.repository.MenuRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ReflectionUtils;
import org.springframework.web.multipart.MultipartFile;

import java.awt.*;
import java.io.*;
import java.lang.reflect.Field;
import java.text.NumberFormat;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
public class MenuService {
    private final ObjectMapper mapper = new ObjectMapper();

    @Value("${imageDirectoryPath}")
    private String imageDir;
    @Autowired
    private MenuRepository menuRepository;
    @Autowired
    private ItemCategoryRepository itemCategoryRepository;
    public List<MenuItem> getMenu(String category){
        if(category != null)
            return menuRepository.findByCategory(category);
        else
            return menuRepository.findAll();

    }
    public MenuItem getMenuItem(Long id){
        return menuRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Item not found"));
    }
    public List<MenuItemDto> getAllMenuItemDto(String category){
        List<MenuItem> menuItems = getMenu(category);
        return menuItems.stream().map(
                item -> new MenuItemDto(
                        item.getId(),
                        item.getName(),
                        item.getPrice().toString(),
                        item.getDescription(),
                        item.getImageUrl(),
                        itemCategoryRepository.findCategoriesByItemId(item.getId())
                )
        ).collect(Collectors.toList());
    }
    @Transactional
    public MenuItemDto addMenuItem(
            String name,
            String price,
            String description,
            MultipartFile file,
            List<Category> categories){
        Optional<MenuItem> menuItem = menuRepository.findByName(name);
        if(menuItem.isPresent()){
            throw new AlreadyExistException("Item already exist");
        }
        MenuItem item = new MenuItem(name,Double.valueOf(price),description,file.getOriginalFilename());
        categories.forEach(c -> {
            ItemCategory itemCategory = new ItemCategory();
            itemCategory.setItem(item);
            itemCategory.setCategory(c);
            itemCategoryRepository.save(itemCategory);
        });
        menuRepository.save(item);
        try(FileOutputStream fos = new FileOutputStream(imageDir+file.getOriginalFilename())){
            BufferedOutputStream bos = new BufferedOutputStream(fos);
            bos.write(file.getBytes());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return new MenuItemDto(
                item.getId(),
                item.getName(),
                item.getPrice().toString(),
                item.getDescription(),
                item.getImageUrl(),
                categories
        );
    }

    @Transactional
    public MenuItem deleteMenuItem(Long id){
        MenuItem menuItem = menuRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Menu item not found"));
        File file = new File(imageDir+menuItem.getImageUrl());
        file.delete();
        return menuRepository.deleteMenuItem(id);
    }
    @Transactional
    public MenuItemDto updateMenuItem(Long id, Map<String,String> fields,MultipartFile image) {
        MenuItem item = menuRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Item not found"));
        fields.forEach((key,value) -> {
            if(key.equals("categories")){
                try {
                    List<Category> categories = mapper.readValue(value, new TypeReference<>() {});
                    itemCategoryRepository.deleteByItemId(id);
                    categories.forEach(category -> {
                        ItemCategory itemCategory = new ItemCategory();
                        itemCategory.setCategory(category);
                        itemCategory.setItem(item);
                        itemCategoryRepository.save(itemCategory);
                    });
                } catch (JsonProcessingException e) {
                    throw new RuntimeException(e);
                }
            }
            else{
                Field field = ReflectionUtils.findField(MenuItem.class,key);
                if(field != null){
                    field.setAccessible(true);
                    if(field.getType().equals(Double.class)){
                        Double newValue = Double.valueOf(value);
                        ReflectionUtils.setField(field,item,newValue);
                    }
                    else ReflectionUtils.setField(field,item,value);
                }
            }
        });
        if(image!=null){
            try(FileOutputStream fos = new FileOutputStream(imageDir+image.getOriginalFilename())){
                File file = new File(imageDir+item.getImageUrl());
                file.delete();
                BufferedOutputStream bos = new BufferedOutputStream(fos);
                bos.write(image.getBytes());
                item.setImageUrl(image.getOriginalFilename());
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        return new MenuItemDto(
                item.getId(),
                item.getName(),
                item.getPrice().toString(),
                item.getDescription(),
                item.getImageUrl(),
                itemCategoryRepository.findCategoriesByItemId(item.getId()));
    }
}
