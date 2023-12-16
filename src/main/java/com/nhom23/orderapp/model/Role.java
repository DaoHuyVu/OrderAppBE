package com.nhom23.orderapp.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


@Entity
@NoArgsConstructor
@Data
@Table(name = "role")
public class Role {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @Column(name = "role")
    @Enumerated(value = EnumType.STRING)
    private ERole role;
    @OneToMany(mappedBy = "role",cascade = CascadeType.ALL,orphanRemoval = true)
    private List<AccountRole> accounts = new ArrayList<>();
    public void addRole(AccountRole role){
        accounts.add(role);
        role.setRole(this);
    }
    public void removeRole(AccountRole role){
        accounts.remove(role);
        role.setRole(null);
    }
}
