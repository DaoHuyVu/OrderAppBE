package com.nhom23.orderapp.repository;

import com.nhom23.orderapp.model.Account;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AccountRepository extends JpaRepository<Account,Long>,CustomAccountRepository{
    Optional<Account> findAccountByEmail(String email);
    boolean existsByEmail(String email);
}
