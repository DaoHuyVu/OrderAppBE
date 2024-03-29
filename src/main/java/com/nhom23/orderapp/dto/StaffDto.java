package com.nhom23.orderapp.dto;

import com.nhom23.orderapp.model.Address;
import com.nhom23.orderapp.model.Gender;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@NoArgsConstructor
@Data
public class StaffDto{
    private Long id;
    private String name;
    private String email;
    private String phone;
    private String dateOfBirth;
    private String salary;
    private Gender gender;
    private Address address;

    public StaffDto(Long id, String name, String email, String phone, LocalDate dateOfBirth, Double salary, Gender gender, Address address) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.phone = phone;
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        this.dateOfBirth = formatter.format(dateOfBirth);
        this.salary = salary.toString();
        this.gender = gender;
        this.address = address;
    }
}
