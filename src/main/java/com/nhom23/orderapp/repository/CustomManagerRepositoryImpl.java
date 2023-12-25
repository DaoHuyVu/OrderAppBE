package com.nhom23.orderapp.repository;

import com.nhom23.orderapp.dto.ManagerDto;
import com.nhom23.orderapp.exception.NotFoundException;
import com.nhom23.orderapp.model.Manager;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.Modifying;

public class CustomManagerRepositoryImpl implements CustomManagerRepository{
    @PersistenceContext
    private EntityManager entityManager;
    @Autowired
    private AccountRepository accountRepository;
    @Override
    @Modifying(clearAutomatically = true,flushAutomatically = true)
    public ManagerDto deleteManager(Long id) {
        Manager manager = entityManager.find(Manager.class,id);
        if(manager != null){
            ManagerDto returnedManager = manager.toDto();
            entityManager.createQuery("""
                        Delete from Manager m where m.id = :id
                    """).setParameter("id",id).executeUpdate();
            accountRepository.deleteAccount(manager.getId());
            return returnedManager;
        }
        else throw new NotFoundException("Manager not found");
    }
}
