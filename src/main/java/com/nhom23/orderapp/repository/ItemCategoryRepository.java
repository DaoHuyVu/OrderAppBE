package com.nhom23.orderapp.repository;

import com.nhom23.orderapp.dto.MenuItemDto;
import com.nhom23.orderapp.model.Category;
import com.nhom23.orderapp.model.ItemCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ItemCategoryRepository extends JpaRepository<ItemCategory,Long> {
    @Query("""
            Delete from ItemCategory i where i.category.id = :id
            """)
    @Modifying
    void deleteByCategoryId(Long id);
    @Modifying
    @Query("""
            Delete from ItemCategory i where i.item.id = :id
            """)
    void deleteByItemId(Long id);
    @Query("""
            Select c from ItemCategory ic
            join Category c
            on ic.category.id = c.id
            where ic.item.id = :id
            """)
    List<Category> findCategoriesByItemId(Long id);
}
