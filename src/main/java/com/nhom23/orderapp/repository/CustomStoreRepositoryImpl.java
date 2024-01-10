package com.nhom23.orderapp.repository;

import com.nhom23.orderapp.dto.OrderDetailsDto;
import com.nhom23.orderapp.dto.ShipperDto;
import com.nhom23.orderapp.exception.NotFoundException;
import com.nhom23.orderapp.model.Manager;
import com.nhom23.orderapp.model.OrderDetail;
import com.nhom23.orderapp.model.Store;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public class CustomStoreRepositoryImpl implements CustomStoreRepository{
    @PersistenceContext
    private EntityManager entityManager;
    @Autowired
    private ShipperRepository shipperRepository;
    @Autowired
    private OrderDetailsRepository orderDetailsRepository;
    @Autowired
    private ManagerRepository managerRepository;
    @Override
    @Modifying(clearAutomatically = true,flushAutomatically = true)
    public Store deleteByStoreId(Long id) {
        Store store = entityManager.find(Store.class,id);
        if(store != null){
            Long managerId = managerRepository.findByStoreId(id);
            if(managerId != null){
                managerRepository.deleteManager(managerId);
                List<ShipperDto> shippers = shipperRepository.findAllByStoreId(id);
                shippers.forEach(shipper -> shipperRepository.deleteShipper(shipper.getId()));

                List<Long> orderDetailsId = orderDetailsRepository.findAllOfAStore(id);
                orderDetailsId.forEach(orderId -> orderDetailsRepository.deleteOrder(orderId));
            }
            entityManager.createQuery("""
                        Delete from Store s where s.id = :id
                    """).setParameter("id",id).executeUpdate();
            return store;
        }
        else throw new NotFoundException("Store not found");
    }
}
