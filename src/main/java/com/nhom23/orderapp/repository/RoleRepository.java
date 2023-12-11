package com.nhom23.orderapp.repository;

import com.nhom23.orderapp.model.ERole;
import com.nhom23.orderapp.model.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RoleRepository extends JpaRepository<Role,Long> {
    Role findByRole(ERole role);
}
