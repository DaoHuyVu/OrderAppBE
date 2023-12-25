package com.nhom23.orderapp.repository;

import com.nhom23.orderapp.model.Category;

public interface CustomCategoryRepository{
    Category deleteCategory(Long id);
}
