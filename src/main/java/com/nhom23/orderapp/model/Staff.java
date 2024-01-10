package com.nhom23.orderapp.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@MappedSuperclass
public abstract class Staff {
    protected String name;
    protected String phone;
    protected LocalDate dateOfBirth;
    protected Double salary;
    @Enumerated(value = EnumType.STRING)
    protected Gender gender;
}

