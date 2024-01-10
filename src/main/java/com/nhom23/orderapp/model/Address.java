package com.nhom23.orderapp.model;

import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Embeddable
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Address {
    private String city;
    private String district;
    private String street;
    @Override
    public String toString() {
        return street + " - " + district + " - " + city;
    }
}
