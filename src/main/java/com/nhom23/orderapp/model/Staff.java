package com.nhom23.orderapp.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
@MappedSuperclass
public abstract class Staff {
    protected String name;
    @Column(name = "phone_number")
    protected String phoneNumber;
    @JsonFormat(shape = JsonFormat.Shape.STRING,pattern = "DD-MM-YYYY")
    protected LocalDate dateOfBirth;
    protected Integer salary;
    @Enumerated(value = EnumType.STRING)
    protected Gender gender;
}

