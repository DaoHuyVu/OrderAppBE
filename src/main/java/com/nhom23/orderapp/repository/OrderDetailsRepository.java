package com.nhom23.orderapp.repository;

import com.nhom23.orderapp.model.OrderDetail;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderDetailsRepository extends JpaRepository<OrderDetail,Long> {

}
