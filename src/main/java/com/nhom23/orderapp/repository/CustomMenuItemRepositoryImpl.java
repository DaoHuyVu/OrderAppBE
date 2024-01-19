package com.nhom23.orderapp.repository;

import com.nhom23.orderapp.exception.NotFoundException;
import com.nhom23.orderapp.model.MenuItem;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.transaction.annotation.Transactional;

public class CustomMenuItemRepositoryImpl implements CustomMenuItemRepository{
    @Autowired
    private ItemCategoryRepository itemCategoryRepository;
    @PersistenceContext
    private EntityManager entityManager;
    @Override
    @Modifying
    public MenuItem deleteMenuItem(Long id) {
        MenuItem menuItem = entityManager.find(MenuItem.class,id);
        if(menuItem != null){
            itemCategoryRepository.deleteByItemId(id);
            entityManager.createQuery("""
                        Delete from MenuItem m where m.id = :id
                    """).setParameter("id",id).executeUpdate();
            return menuItem;
        }
        else throw new NotFoundException("Menu item not found");
    }
}
