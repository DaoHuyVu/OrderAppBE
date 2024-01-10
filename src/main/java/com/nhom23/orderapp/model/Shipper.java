package com.nhom23.orderapp.model;

import com.nhom23.orderapp.dto.ShipperDto;
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
public class Shipper extends Staff{
    @Id
    private Long id;
    @OneToOne(fetch = FetchType.LAZY)
    @MapsId
    @JoinColumn(name = "id")
    private Account account;
    @ManyToOne(fetch = FetchType.LAZY)
    private Store store;

    public Shipper(String name, String phoneNumber, LocalDate dateOfBirth, Double salary, Gender gender) {
        super(name, phoneNumber, dateOfBirth, salary, gender);
    }
    public ShipperDto toDto(){
        return new ShipperDto(
                id,name,account.getEmail(), phone,dateOfBirth,salary,gender,store.getAddress()
        );
    }
}
