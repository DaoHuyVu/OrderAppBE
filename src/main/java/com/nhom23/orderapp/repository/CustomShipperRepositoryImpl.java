package com.nhom23.orderapp.repository;

import com.nhom23.orderapp.dto.OrderDetailsDto;
import com.nhom23.orderapp.dto.StaffDto;
import com.nhom23.orderapp.exception.NotFoundException;
import com.nhom23.orderapp.model.OrderDetail;
import com.nhom23.orderapp.model.Staff;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.Modifying;

import java.util.List;

public class CustomShipperRepositoryImpl implements CustomShipperRepository{
    @PersistenceContext
    private EntityManager entityManager;
    @Autowired
    private AccountRepository accountRepository;
    @Autowired
    private OrderDetailsRepository orderDetailsRepository;

    @Override
    @Modifying
    public StaffDto deleteShipper(Long id) {
        Staff shipper = entityManager.find(Staff.class,id);
        if(shipper != null){
            StaffDto returnedShipper = shipper.toDto();
            orderDetailsRepository.updateItemOnShipperDeletion(id);
            entityManager.createQuery("""
                        Delete from Shipper s where s.id = :id
                    """).setParameter("id",id).executeUpdate();
            accountRepository.deleteAccount(id);
            return returnedShipper;
        }
        else throw new NotFoundException("Shipper not found");
    }
}
