package com.nhom23.orderapp.service;

import com.nhom23.orderapp.exception.AlreadyExistException;
import com.nhom23.orderapp.exception.NotFoundException;
import com.nhom23.orderapp.model.Category;
import com.nhom23.orderapp.repository.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.util.ReflectionUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.List;
import java.util.Map;

@Service
@Transactional(readOnly = true)
public class CategoryService {
    @Autowired
    private CategoryRepository categoryRepository;
    @Value("${imageDirectoryPath}")
    private String imageDir;
    public List<Category> getCategories(){
        return categoryRepository.findAll();
    }
    public Category getCategory(Long id){
        return categoryRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Category not found"));
    }
    @Transactional
    public Category deleteCategory(Long id) throws IOException {
        Category category = categoryRepository.findById(id)
                .orElseThrow(()->new NotFoundException("Category not found"));
        File file = new File(imageDir+category.getImageUrl());
        if(file.delete()){
            return categoryRepository.deleteCategory(id);
        }
        throw new IOException("Can not delete file");
    }
    @Transactional
    public Category updateCategory(Long id, String categoryName,MultipartFile file){
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Category not found"));
        category.setName(categoryName);
        if(file != null){
            try(FileOutputStream fos = new FileOutputStream(imageDir+category.getImageUrl())){
                File oldImageFile = new File(imageDir+category.getImageUrl());
                oldImageFile.delete();
                BufferedOutputStream bos = new BufferedOutputStream(fos);
                bos.write(file.getBytes());
                category.setImageUrl(file.getOriginalFilename());
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        return category;
    }
    @Transactional
    public Category addCategory(String name, MultipartFile image) throws IOException {
        if(categoryRepository.findByName(name) != null){
            throw new AlreadyExistException("Category already exist");
        }
        try(FileOutputStream fos = new FileOutputStream(imageDir + image.getOriginalFilename())){
            BufferedOutputStream bos = new BufferedOutputStream(fos);
            bos.write(image.getBytes());
        }
        return categoryRepository.save(new Category(name,image.getOriginalFilename()));
    }
}
