package com.nhom23.orderapp.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
@Table(name = "account")
public class Account {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private String password;
    private String email;
    private Boolean isEnable;
    @OneToMany(mappedBy = "account",orphanRemoval = true,cascade = CascadeType.ALL)
    private List<AccountRole> roles = new ArrayList<>();

    public Account(String email, String password) {
        this.email = email;
        this.password = password;
        isEnable = false;
    }
    public void addRole(AccountRole role){
        roles.add(role);
        role.setAccount(this);
    }
    public void removeRole(AccountRole role){
        roles.remove(role);
        role.setRole(null);
    }
}
