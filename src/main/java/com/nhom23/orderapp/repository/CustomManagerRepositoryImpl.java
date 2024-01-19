package com.nhom23.orderapp.repository;

import com.nhom23.orderapp.dto.StaffDto;
import com.nhom23.orderapp.exception.NotFoundException;
import com.nhom23.orderapp.model.Staff;
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
    @Modifying
    public StaffDto deleteManager(Long id) {
        Staff manager = entityManager.find(Staff.class,id);
        if(manager != null){
            StaffDto returnedManager = manager.toDto();
            entityManager.createQuery("""
                        Delete from Manager m where m.id = :id
                    """).setParameter("id",id).executeUpdate();
            accountRepository.deleteAccount(manager.getId());
            return returnedManager;
        }
        else throw new NotFoundException("Manager not found");
    }
}
