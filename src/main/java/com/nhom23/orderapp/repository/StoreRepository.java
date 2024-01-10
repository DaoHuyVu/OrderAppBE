package com.nhom23.orderapp.repository;

import com.nhom23.orderapp.dto.Revenue;
import com.nhom23.orderapp.model.Address;
import com.nhom23.orderapp.model.Store;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface StoreRepository extends JpaRepository<Store,Long>,CustomStoreRepository{
    @Query("""
            SELECT s FROM Store s where not exists (SELECT m FROM Manager m where m.store = s)
            """)
    List<Store> findAllUnmanaged();
    //Find revenue of all store
    @Query("""
            Select new com.nhom23.orderapp.dto.Revenue(
            CONCAT(s.address.street, ' - ', s.address.district, ' - ', s.address.city)
            ,COALESCE(SUM(o.price),0.0))
            from Store s left join OrderDetail o ON s.id = o.store.id
            AND o.status = 'DELIVERED'
            AND FUNCTION('YEAR',o.createdAt) = :year
            AND FUNCTION('MONTH',o.createdAt) = :month
            AND FUNCTION('DAY',o.createdAt) = :day
            group by s.address.street, s.address.district, s.address.city
            """)
    List<Revenue> findRevenue(Integer day,Integer month,Integer year);

    @Query("""
            Select new com.nhom23.orderapp.dto.Revenue(
            CONCAT(s.address.street, ' - ', s.address.district, ' - ', s.address.city)
            ,COALESCE(SUM(o.price),0.0))
            from Store s left join OrderDetail o ON s.id = o.store.id
            AND o.status = 'DELIVERED'
            AND FUNCTION('YEAR',o.createdAt) = :year
            AND FUNCTION('MONTH',o.createdAt) = :month
            group by s.address.street, s.address.district, s.address.city
            """)
    List<Revenue> findRevenue(Integer month,Integer year);

    @Query("""
            Select new com.nhom23.orderapp.dto.Revenue(
            CONCAT(s.address.street, ' - ', s.address.district, ' - ', s.address.city)
            ,COALESCE(SUM(o.price),0.0))
            from Store s left join OrderDetail o ON s.id = o.store.id
            AND o.status = 'DELIVERED'
            AND FUNCTION('YEAR',o.createdAt) = :year
            group by s.address.street, s.address.district, s.address.city
            """)
    List<Revenue> findRevenue(Integer year);
    @Query("""
            SELECT new com.nhom23.orderapp.dto.Revenue(
            CONCAT(s.address.street, ' - ', s.address.district, ' - ', s.address.city),
            coalesce(total,0.0))
            from Store s left join
            (
                Select od.store.id as id,Coalesce(sum(mi.price*oi.quantity),0.0) as total from MenuItem mi
                left join OrderItem oi on mi.id = oi.item.id
                left join OrderDetail od on oi.orderDetail.id = od.id
                and function('year',od.createdAt) = :year
                and FUNCTION('MONTH',od.createdAt) = :month
                AND FUNCTION('DAY',od.createdAt) = :day
                where od.status = 'Delivered' and mi.name = :name
                group by od.store.id
            )  as item on s.id = item.id
            """)
    List<Revenue> findRevenueOfCategory(String name,Integer day,Integer month,Integer year);
    @Query("""
            SELECT new com.nhom23.orderapp.dto.Revenue(
            CONCAT(s.address.street, ' - ', s.address.district, ' - ', s.address.city),
            coalesce(total,0.0))
            from Store s left join
            (
                Select od.store.id as id,Coalesce(sum(mi.price*oi.quantity),0.0) as total from MenuItem mi
                left join OrderItem oi on mi.id = oi.item.id
                left join OrderDetail od on oi.orderDetail.id = od.id
                and function('year',od.createdAt) = :year
                and FUNCTION('MONTH',od.createdAt) = :month
                where od.status = 'Delivered' and mi.name = :name
                group by od.store.id
            )  as item on s.id = item.id
            """)
    List<Revenue> findRevenueOfCategory(String name,Integer month,Integer year);
    @Query("""
            SELECT new com.nhom23.orderapp.dto.Revenue(
            CONCAT(s.address.street, ' - ', s.address.district, ' - ', s.address.city),
            coalesce(total,0.0))
            from Store s left join
            (
                Select od.store.id as id,sum(mi.price*oi.quantity) as total from MenuItem mi
                left join OrderItem oi on mi.id = oi.item.id
                left join OrderDetail od on oi.orderDetail.id = od.id
                and function('year',od.createdAt) = :year
                where od.status = 'Delivered' and mi.name = :name
                group by od.store.id
            )  as item on s.id = item.id
            """)
    List<Revenue> findRevenueOfCategory(String name,Integer year);
}
