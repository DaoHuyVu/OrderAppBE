package com.nhom23.orderapp.repository;

import com.nhom23.orderapp.dto.OrderDetailsDto;
import com.nhom23.orderapp.model.OrderDetail;

public interface CustomOrderDetailsRepository {
    OrderDetailsDto deleteOrder(Long id);
}
