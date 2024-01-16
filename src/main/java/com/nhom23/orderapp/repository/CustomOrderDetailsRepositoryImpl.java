package com.nhom23.orderapp.repository;

import com.nhom23.orderapp.dto.OrderDetailsDto;
import com.nhom23.orderapp.exception.NotFoundException;
import com.nhom23.orderapp.model.OrderDetail;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.Modifying;

public class CustomOrderDetailsRepositoryImpl implements CustomOrderDetailsRepository{
    @PersistenceContext
    private EntityManager entityManager;
    @Autowired
    private OrderItemRepository orderItemRepository;
    @Override
    @Modifying(clearAutomatically = true,flushAutomatically = true)
    public void deleteOrder(Long id) {
        OrderDetail orderDetail = entityManager.find(OrderDetail.class,id);
        if(orderDetail != null){
            orderItemRepository.deleteByOrderDetailId(id);
            entityManager.createQuery("""
                    Delete from OrderDetail o where o.id = :id
                """).setParameter("id",id).executeUpdate();
        }
        else throw new NotFoundException("Order not found");
    }
}
