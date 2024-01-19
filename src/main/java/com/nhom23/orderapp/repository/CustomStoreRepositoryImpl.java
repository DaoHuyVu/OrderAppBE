package com.nhom23.orderapp.repository;

import com.nhom23.orderapp.dto.StaffDto;
import com.nhom23.orderapp.exception.NotFoundException;
import com.nhom23.orderapp.model.Store;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.Modifying;

import java.util.List;

public class CustomStoreRepositoryImpl implements CustomStoreRepository{
    @PersistenceContext
    private EntityManager entityManager;
    @Autowired
    private OrderDetailsRepository orderDetailsRepository;
    @Autowired
    private StaffRepository staffRepository;
    @Override
    @Modifying
    public Store deleteByStoreId(Long id) {
        Store store = entityManager.find(Store.class,id);
        if(store != null){
            Long managerId = staffRepository.findByStoreId(id);
            if(managerId != null){
                staffRepository.deleteManager(managerId);
                List<Long> orderDetailsId = orderDetailsRepository.findAllOfAStore(id);
                orderDetailsId.forEach(orderId -> orderDetailsRepository.deleteOrder(orderId));
                List<StaffDto> shippers = staffRepository.findAllShipperByStoreId(id);
                shippers.forEach(shipper -> staffRepository.deleteShipper(shipper.getId()));
            }
            entityManager.createQuery("""
                        Delete from Store s where s.id = :id
                    """).setParameter("id",id).executeUpdate();
            return store;
        }
        else throw new NotFoundException("Store not found");
    }
}
