package com.nhom23.orderapp.repository;

import com.nhom23.orderapp.model.Manager;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import javax.print.attribute.standard.MediaName;
import java.util.Optional;

public interface ManagerRepository extends JpaRepository<Manager,Long> {
    @Query("""
            SELECT m.store.id from Manager m where m.id = :managerId
            """)
    Optional<Long> findStoreIdByManagerId(Long managerId);

}
