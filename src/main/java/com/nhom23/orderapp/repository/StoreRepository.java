package com.nhom23.orderapp.repository;

import com.nhom23.orderapp.model.Address;
import com.nhom23.orderapp.model.Store;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface StoreRepository extends JpaRepository<Store,Long> {
    @Query("""
            SELECT s
            FROM Store s
            JOIN Manager m ON m.store.id = s.id
            WHERE m.id = :id
            """)
    Optional<Store> findByManagerId(Long id);
    @Query("""
            SELECT s
            FROM Store s
            JOIN Shipper shipper ON shipper.store.id = s.id
            WHERE shipper.id = :id
            """)
    Optional<Store> findByShipperId(Long id);
    @Query("""
            SELECT new com.nhom23.orderapp.model.Address(s.address.city,s.address.district,s.address.street)
            from Store s join Manager m on m.store.id = s.id
            where m.id = :id
            """)
    Address findAddressByManagerId(Long id);
}
