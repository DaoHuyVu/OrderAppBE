package com.nhom23.orderapp.repository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.Modifying;

public class CustomAccountRepositoryImpl implements CustomAccountRepository{
    @PersistenceContext
    private EntityManager entityManager;
    @Autowired
    private AccountRoleRepository accountRoleRepository;
    @Override
    @Modifying
    public void deleteAccount(Long id) {
        accountRoleRepository.deleteByAccountId(id);
        entityManager.createQuery("""
                    Delete from Account a where a.id = :id
                """).setParameter("id",id).executeUpdate();
    }
}
