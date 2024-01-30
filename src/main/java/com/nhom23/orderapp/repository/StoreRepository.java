package com.nhom23.orderapp.repository;

import com.nhom23.orderapp.dto.Revenue;
import com.nhom23.orderapp.model.Store;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDate;
import java.util.List;

public interface StoreRepository extends JpaRepository<Store,Long>,CustomStoreRepository{
    @Query("""
            SELECT new com.nhom23.orderapp.dto.Revenue(
            CONCAT(s.address.street,' - ',s.address.district,' - ',s.address.city),
            COALESCE(sum(od.price),0))
            from Store s
            left join OrderDetail od
            on s.id = od.store.id
            and od.status = 'DELIVERED'
            and DATE(od.createdAt) between :from and :to
            GROUP by s.id
            """)
    List<Revenue> findRevenue(LocalDate from,LocalDate to);
    @Query("""
            SELECT new com.nhom23.orderapp.dto.Revenue(
            CONCAT(s.address.street,' - ',s.address.district,' - .',s.address.city),
            COALESCE(sum(price*quantity),0) as total )
            from Store s
            left join
            (
                SELECT od.store.id as store_id,mi.price as price,oi.quantity as quantity from MenuItem mi
                left join OrderItem oi
                on mi.id = oi.item.id
                and mi.name = :menuItem
                join OrderDetail od
                on od.id = oi.orderDetail.id
                where od.status = 'delivered'
                and DATE(od.createdAt) between :from and :to
            ) as item
            on s.id = item.store_id
            group by s.id
            """)
    List<Revenue> findRevenueOfMenuItem(LocalDate from, LocalDate to,String menuItem );
}
