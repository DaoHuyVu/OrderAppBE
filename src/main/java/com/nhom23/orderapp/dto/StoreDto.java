package com.nhom23.orderapp.dto;

import com.nhom23.orderapp.model.Address;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class StoreDto {
    private Long id;
    private Address address;

}
