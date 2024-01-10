package com.nhom23.orderapp.model;

import com.nhom23.orderapp.dto.ManagerDto;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@EqualsAndHashCode(callSuper = true)
@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
public class Manager extends Staff{
    @Id
    private Long id;
    @OneToOne(fetch = FetchType.LAZY)
    @MapsId
    @JoinColumn(name = "id")
    private Account account;
    @OneToOne(fetch = FetchType.LAZY)
    private Store store;
    public Manager(String name, String phoneNumber, LocalDate dateOfBirth, Double salary, Gender gender) {
        super(name, phoneNumber, dateOfBirth, salary, gender);
    }
    public ManagerDto toDto(){
        return new ManagerDto(
                id,name,account.getEmail(), phone,dateOfBirth,salary,gender,store.getAddress()
        );
    }
}
