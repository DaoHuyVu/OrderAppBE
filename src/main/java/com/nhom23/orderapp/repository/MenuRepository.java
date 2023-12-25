package com.nhom23.orderapp.repository;

import com.nhom23.orderapp.model.MenuItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.lang.NonNullApi;
import org.springframework.stereotype.Repository;

import java.awt.*;
import java.util.List;
import java.util.Optional;

@Repository
public interface MenuRepository extends JpaRepository<MenuItem,Long>,CustomMenuItemRepository {
    @Query("""
            SELECT new MenuItem(m.id,m.name,m.price,m.description,m.imageUrl)
            from MenuItem m
            join ItemCategory ic
            on m.id = ic.item.id
            join Category c
            on c.id = ic.category.id
            where c.name = :category
            """)
    List<MenuItem> findByCategory(String category);
    Optional<MenuItem> findByName(String name) ;
}
