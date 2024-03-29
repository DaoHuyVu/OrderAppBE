package com.nhom23.orderapp.repository;

import com.nhom23.orderapp.dto.OrderDetailsDto;
import com.nhom23.orderapp.model.OrderDetail;
import org.hibernate.query.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import com.nhom23.orderapp.model.Address;
import java.util.List;
import java.util.Optional;

public interface OrderDetailsRepository extends JpaRepository<OrderDetail,Long>,CustomOrderDetailsRepository {
    //Find all orders of a store that hasn't been delivered (shipperId is null)
    @Query("""
            SELECT new com.nhom23.orderapp.dto.OrderDetailsDto
            (od.id,od.phone,od.address,od.price,c.userName,od.createdAt,od.status,new com.nhom23.orderapp.model.Address(s.address.city,s.address.district,s.address.street))
            from OrderDetail od
            join Store s on od.store.id = s.id
            join Staff m on s.id = m.store.id
            join Customer c on c.id = od.customer.id
            where m.id = :id and od.shipper.id is null
            """)
    // Find all newly-created orders
    List<OrderDetailsDto> findAllByManagerId(Long id);
    @Query("""
            SELECT new com.nhom23.orderapp.dto.OrderDetailsDto
            (od.id,od.phone,od.address,od.price,c.userName,od.createdAt,od.status,new com.nhom23.orderapp.model.Address(s.address.city,s.address.district,s.address.street))
            from OrderDetail od
            join Customer c on c.id = od.customer.id
            join Store s on od.store.id = s.id
            where od.shipper.id = :id and od.status = DELIVERING
            """)
    // Find all delegated orders of a shipper
    List<OrderDetailsDto> findAllByShipperId(Long id);
    @Query("""
            SELECT new com.nhom23.orderapp.dto.OrderDetailsDto
            (od.id,od.phone,od.address,od.price,c.userName,od.createdAt,od.status,new com.nhom23.orderapp.model.Address(s.address.city,s.address.district,s.address.street))
            from OrderDetail od
            join Store s on od.store.id = s.id
            join Customer c on c.id = od.customer.id
            where od.id = :id
            """)

    Optional<OrderDetailsDto> findDtoById(Long id);
    @Query("""
            SELECT  new com.nhom23.orderapp.dto.OrderDetailsDto
            (od.id,od.phone,od.address,od.price,c.userName,od.createdAt,od.status,new com.nhom23.orderapp.model.Address(s.address.city,s.address.district,s.address.street))
            from OrderDetail od
            join Store s on od.store.id = s.id
            join Customer c on c.id = od.customer.id
            where c.id = :id
            """)
    // Find all orders of a customer
    Optional<List<OrderDetailsDto>> findAllByCustomerId(Long id);

    //Find all orders of a store
    @Query("""
            Select o.id from OrderDetail o where o.store.id = :id
            """)
    List<Long> findAllOfAStore(Long id);
    @Query("""
            Update OrderDetail od set od.shipper = null where od.shipper.id = :shipperId
            """)
    void updateItemOnShipperDeletion(Long shipperId);
}
