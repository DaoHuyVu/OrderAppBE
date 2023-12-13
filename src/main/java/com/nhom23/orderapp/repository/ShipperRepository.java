package com.nhom23.orderapp.repository;

import com.nhom23.orderapp.dto.ShipperDto;
import com.nhom23.orderapp.model.Shipper;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ShipperRepository extends JpaRepository<Shipper,Long> {
    @Query("""
            SELECT new com.nhom23.orderapp.dto.ShipperDto(s.id,s.name,a.email,s.phoneNumber,s.dateOfBirth,s.salary,s.gender)
            from Shipper s join Account a
            on a.id = s.account.id
            where s.store.id = :storeId
            """)
    List<ShipperDto> findAllByStoreId(Long storeId);
}
