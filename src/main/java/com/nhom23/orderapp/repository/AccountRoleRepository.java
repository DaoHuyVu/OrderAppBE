package com.nhom23.orderapp.repository;

import com.nhom23.orderapp.model.AccountRole;
import com.nhom23.orderapp.model.Role;
import jakarta.persistence.Tuple;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import com.nhom23.orderapp.security.service.UserDetailsImp;
import java.util.List;
import java.util.Optional;

@Repository
public interface AccountRoleRepository extends JpaRepository<AccountRole, Long> {
    List<AccountRole> findByAccountId(Long id);
    @Query("""
            SELECT a.id,a.password,a.isEnable,r.role
            from Account a
            join AccountRole ac
            on a.id = ac.account.id
            join Role r
            on r.id = ac.role.id
            where a.email = :email
            """)
    Optional<List<Tuple>> findAccountAndRole(String email);
    @Query("""
            Delete from AccountRole a where a.account.id = :id
            """)
    @Modifying
    void deleteByAccountId(Long id);
}
