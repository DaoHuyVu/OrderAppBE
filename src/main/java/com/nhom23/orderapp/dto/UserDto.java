package com.nhom23.orderapp.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class UserDto {
    private Long id;
    private String email;

    @Override
    public String toString() {
        return id + " " + email;
    }
}
