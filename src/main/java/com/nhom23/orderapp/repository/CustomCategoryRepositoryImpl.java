package com.nhom23.orderapp.repository;

import com.nhom23.orderapp.exception.NotFoundException;
import com.nhom23.orderapp.model.Category;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.hibernate.query.Query;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.transaction.annotation.Transactional;

public class CustomCategoryRepositoryImpl implements CustomCategoryRepository{
    @Autowired
    private ItemCategoryRepository itemCategoryRepository;
    @PersistenceContext
    private EntityManager entityManager;
    @Override
    @Modifying(clearAutomatically = true,flushAutomatically = true)
    public Category deleteCategory(Long id) {
        Category category = entityManager.getReference(Category.class,id);
        if(category != null){
            itemCategoryRepository.deleteByCategoryId(id);
            entityManager.createQuery("""
                    DELETE FROM Category c where c.id = :id
                """).setParameter("id",id).executeUpdate();
            return category;
        }
        else throw new NotFoundException("Category not found");
    }
}
