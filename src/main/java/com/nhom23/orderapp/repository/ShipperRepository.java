package com.nhom23.orderapp.repository;

import com.nhom23.orderapp.dto.ShipperDto;
import com.nhom23.orderapp.model.Shipper;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import com.nhom23.orderapp.model.Address;
import java.util.List;

@Repository
public interface ShipperRepository extends JpaRepository<Shipper,Long>,CustomShipperRepository{
    @Query("""
            SELECT new com.nhom23.orderapp.dto.ShipperDto(
            s.id,s.name,a.email,s.phone,s.dateOfBirth,s.salary,s.gender,
            store.address)
            from Shipper s join Account a
            on a.id = s.account.id
            join Store store on store.id = s.store.id
            where store.id = :storeId
            """)
    List<ShipperDto> findAllByStoreId(Long storeId);
    @Query("""
            SELECT new com.nhom23.orderapp.dto.ShipperDto(
            s.id,s.name,a.email,s.phone,s.dateOfBirth,s.salary,s.gender,
            store.address)
            from Shipper s join Account a
            on a.id = s.account.id
            join Store store on store.id = s.store.id
            """)
    List<ShipperDto> findAllShipper();
}
