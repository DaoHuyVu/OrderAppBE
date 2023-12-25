package com.nhom23.orderapp.dto;

import com.nhom23.orderapp.model.Category;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;
@Getter
@AllArgsConstructor
public class MenuItemDto {
    private long id;
    private String name;
    private String price;
    private String description;
    private String imageUrl;
    private List<Category> categories;

}
