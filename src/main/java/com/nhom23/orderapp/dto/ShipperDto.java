package com.nhom23.orderapp.dto;

import com.nhom23.orderapp.model.Address;
import com.nhom23.orderapp.model.Gender;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.swing.text.DateFormatter;
import java.io.Serializable;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@NoArgsConstructor
@Data

public class ShipperDto implements Serializable {
    private Long id;
    private String name;
    private String email;
    private String phone;
    private String dateOfBirth;
    private String salary;
    private Gender gender;
    private Address address;

    public ShipperDto(Long id, String name, String email, String phone, LocalDate dateOfBirth, String salary, Gender gender, Address address) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.phone = phone;
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        this.dateOfBirth = formatter.format(dateOfBirth);
        this.salary = salary;
        this.gender = gender;
        this.address = address;
    }
}
