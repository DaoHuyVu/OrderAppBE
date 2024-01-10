package com.nhom23.orderapp.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class Revenue {
    private String store;
    private Double total;
}
