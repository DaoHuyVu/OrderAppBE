package com.nhom23.orderapp.model;

import com.nhom23.orderapp.dto.StaffDto;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Staff {
    @Id
    private Long id;
    @OneToOne(fetch = FetchType.LAZY)
    @MapsId
    @JoinColumn(name = "id")
    private Account account;
    @ManyToOne(fetch = FetchType.LAZY)
    private Store store;
    private  String name;
    private  String phone;
    private  LocalDate dateOfBirth;
    private  Double salary;
    @Enumerated(value = EnumType.STRING)
    private Gender gender;

    public Staff(String name, String phone, LocalDate dateOfBirth, Double salary, Gender gender) {
        this.name = name;
        this.phone = phone;
        this.dateOfBirth = dateOfBirth;
        this.salary = salary;
        this.gender = gender;
    }
    public StaffDto toDto(){
        return new StaffDto(id,name,account.getEmail(),phone,dateOfBirth,salary,gender,store.getAddress());
    }
}

