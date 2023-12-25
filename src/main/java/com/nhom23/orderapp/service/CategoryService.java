package com.nhom23.orderapp.service;

import com.nhom23.orderapp.exception.NotFoundException;
import com.nhom23.orderapp.model.Category;
import com.nhom23.orderapp.repository.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
public class CategoryService {
    @Autowired
    private CategoryRepository categoryRepository;

    public List<Category> getCategories(){
        return categoryRepository.findAll();
    }
    public Category getCategory(Long id){
        return categoryRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Category not found"));
    }
    @Transactional
    public Category deleteCategory(Long id){
        return categoryRepository.deleteCategory(id);
    }
    @Transactional
    public Category updateCategory(Long id,String name,String imageUrl){
        Category category = categoryRepository.findById(id).orElseThrow(() -> new NotFoundException("Category not found"));
        category.setName(name);
        category.setImageUrl(imageUrl);
        return category;
    }
}
