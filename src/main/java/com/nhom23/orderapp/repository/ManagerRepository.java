package com.nhom23.orderapp.repository;

import com.nhom23.orderapp.dto.ManagerDto;
import com.nhom23.orderapp.dto.ShipperDto;
import com.nhom23.orderapp.model.Manager;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import javax.print.attribute.standard.MediaName;
import java.util.List;
import java.util.Optional;

public interface ManagerRepository extends JpaRepository<Manager,Long> {
    @Query("""
            SELECT m.store.id from Manager m where m.id = :managerId
            """)
    Optional<Long> findStoreIdByManagerId(Long managerId);
    @Query("""
            SELECT new com.nhom23.orderapp.dto.ManagerDto(
            s.id,s.name,a.email,s.phoneNumber,s.dateOfBirth,s.salary,s.gender,
            store.address)
            from Manager s join Account a
            on a.id = s.account.id
            join Store store on store.id = s.store.id
            """)
    List<ManagerDto> findAllManager();
}
