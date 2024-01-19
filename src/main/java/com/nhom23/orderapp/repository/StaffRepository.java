package com.nhom23.orderapp.repository;

import com.nhom23.orderapp.dto.StaffDto;
import com.nhom23.orderapp.model.Staff;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import java.util.List;
import java.util.Optional;

public interface StaffRepository extends JpaRepository<Staff,Long>,CustomManagerRepository,CustomShipperRepository{
    @Query("""
            SELECT m.store.id from Staff m where m.id = :managerId
            """)
    Optional<Long> findStoreIdByManagerId(Long managerId);
    @Query("""
            SELECT new com.nhom23.orderapp.dto.StaffDto(
            s.id,s.name,a.email,s.phone,s.dateOfBirth,s.salary,s.gender,
            store.address)
            from Staff s
            join Account a
            on s.account.id = a.id
            join Store store
            on s.store.id = store.id
            where exists
            (
                Select 1 from AccountRole ac
                join Role r
                on ac.role.id = r.id
                and r.role = 'ROLE_MANAGER'
                where ac.account.id = s.id
            )
            """)
    List<StaffDto> findAllManager();

    @Query("""
            Select m.id from Staff m where m.store.id = :id
            """)
    Long findByStoreId(Long id);
    @Query("""
            SELECT new com.nhom23.orderapp.dto.StaffDto(
            s.id,s.name,a.email,s.phone,s.dateOfBirth,s.salary,s.gender,
            store.address)
            from Staff s
            join Account a
            on s.account.id = a.id
            join Store store
            on s.store.id = store.id
            where exists
            (
                Select 1 from AccountRole ac
                join Role r
                on ac.role.id = r.id
                and r.role = 'ROLE_STAFF'
                where ac.account.id = s.id
            )
            """)
    List<StaffDto> findAllShipper();
    @Query("""
            SELECT new com.nhom23.orderapp.dto.StaffDto(
            s.id,s.name,a.email,s.phone,s.dateOfBirth,s.salary,s.gender,
            store.address)
            from Staff s
            join Account a
            on s.account.id = a.id
            join Store store
            on s.store.id = store.id and store.id = :storeId
            where exists
            (
                Select 1 from AccountRole ac
                join Role r
                on ac.role.id = r.id
                and r.role = 'ROLE_STAFF'
                where ac.account.id = s.id
            )
            """)
    List<StaffDto> findAllShipperByStoreId(Long storeId);
}
