package com.nhom23.orderapp.repository;

import com.nhom23.orderapp.model.Address;
import com.nhom23.orderapp.model.Store;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface StoreRepository extends JpaRepository<Store,Long>,CustomStoreRepository{
//    @Query("""
//            SELECT s
//            FROM Store s
//            JOIN Manager m ON m.store.id = s.id
//            WHERE m.id = :id
//            """)
//    Optional<Store> findByManagerId(Long id);
//    @Query("""
//            SELECT s
//            FROM Store s
//            JOIN Shipper shipper ON shipper.store.id = s.id
//            WHERE shipper.id = :id
//            """)
//    Optional<Store> findByShipperId(Long id);
    @Query("""
            SELECT s FROM Store s where not exists (SELECT m FROM Manager m where m.store = s)
            """)
    List<Store> findAllUnmanaged();
}
