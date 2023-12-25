package com.nhom23.orderapp.repository;

import com.nhom23.orderapp.dto.ShipperDto;
import com.nhom23.orderapp.exception.NotFoundException;
import com.nhom23.orderapp.model.Shipper;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.Modifying;

public class CustomShipperRepositoryImpl implements CustomShipperRepository{
    @PersistenceContext
    private EntityManager entityManager;
    @Autowired
    private AccountRepository accountRepository;

    @Override
    @Modifying
    public ShipperDto deleteShipper(Long id) {
        Shipper shipper = entityManager.find(Shipper.class,id);
        if(shipper != null){
            ShipperDto returnedShipper = shipper.toDto();
            entityManager.createQuery("""
                        Delete from Shipper s where s.id = :id
                    """).setParameter("id",id).executeUpdate();
            accountRepository.deleteAccount(id);
            return returnedShipper;
        }
        else throw new NotFoundException("Shipper not found");
    }
}
