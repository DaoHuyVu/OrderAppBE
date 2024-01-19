package com.nhom23.orderapp.repository;

import com.nhom23.orderapp.dto.OrderItemDto;
import com.nhom23.orderapp.model.OrderItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;
@Repository
public interface OrderItemRepository extends JpaRepository<OrderItem,Long> {
    @Query("""
            SELECT new com.nhom23.orderapp.dto.OrderItemDto(ot.id,i.name,ot.quantity,i.price,i.imageUrl)
            from Account a
            join OrderItem ot
            on a.id = ot.customer.id
            join MenuItem i
            on i.id = ot.item.id
            where a.email = :email and ot.orderDetail.id is null
            """)
    Optional<List<OrderItemDto>> findByEmail(String email);
    @Query("""
            UPDATE OrderItem ot SET ot.quantity = :quantity
            where ot.id = :id and ot.customer.id = :customerId
            """)
    @Modifying
    void updateQuantity(Long id,Long customerId,Integer quantity);
    @Query("""
            SELECT new com.nhom23.orderapp.dto.OrderItemDto(ot.id,i.name,ot.quantity,i.price,i.imageUrl)
            from OrderItem ot
            join MenuItem i
            on i.id = ot.item.id
            where ot.id = :id and ot.customer.id = :customerId
            """)
    Optional<OrderItemDto> getItemDto(Long id,Long customerId);
    @Query("""
            SELECT new com.nhom23.orderapp.dto.OrderItemDto(ot.id,i.name,ot.quantity,i.price,i.imageUrl)
            from OrderItem ot
            join MenuItem i
            on i.id = ot.item.id
            where i.id = :itemId and ot.customer.id = :customerId and ot.orderDetail.id is null
            """)
    Optional<OrderItemDto> findByItemIdAndCustomerIdAndNotYetBeIncludedInOrderDetail(Long itemId, Long customerId);

    @Query("""
            SELECT new com.nhom23.orderapp.dto.OrderItemDto(ot.id,i.name,ot.quantity,i.price,i.imageUrl)
            from OrderItem ot
            join MenuItem i
            on i.id = ot.item.id
            where ot.id = :id and ot.customer.id = :customerId
            """)
    Optional<OrderItemDto> findByIdAndCustomerId(Long id,Long customerId);
    @Query("""
             SELECT new com.nhom23.orderapp.dto.OrderItemDto(ot.id,i.name,ot.quantity,i.price,i.imageUrl)
            from OrderItem ot
            join MenuItem i
            on i.id = ot.item.id
            where ot.orderDetail.id = :id
            """)
    List<OrderItemDto> findByOrderDetailsId(Long id);
    @Modifying
    @Query("""
            Delete from OrderItem o where o.orderDetail.id = :id
            """)
    void deleteByOrderDetailId(Long id);
}
