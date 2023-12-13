package com.nhom23.orderapp.service;

import com.nhom23.orderapp.dto.OrderDetailsDto;
import com.nhom23.orderapp.dto.UserDto;
import com.nhom23.orderapp.model.OrderDetail;
import com.nhom23.orderapp.model.OrderItem;
import com.nhom23.orderapp.model.OrderStatus;
import com.nhom23.orderapp.repository.CustomerRepository;
import com.nhom23.orderapp.repository.OrderDetailsRepository;
import com.nhom23.orderapp.repository.OrderItemRepository;
import com.nhom23.orderapp.repository.StoreRepository;
import com.nhom23.orderapp.security.service.UserDetailsImp;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@Transactional(readOnly = true)
public class OrderDetailsService {
    @Autowired
    private OrderItemRepository orderItemRepository;
    @Autowired
    private CustomerRepository customerRepository;
    @Autowired
    private StoreRepository storeRepository;
    @Autowired
    private OrderDetailsRepository orderDetailsRepository;

    @Transactional
    public OrderDetail createOrderDetails(OrderDetailsDto orderDetailsDto){
        OrderDetail orderDetail = new OrderDetail();
        orderDetail.setStore(storeRepository.getReferenceById(orderDetailsDto.getStoreId()));
        orderDetail.setCustomer(customerRepository.getReferenceById(getUserDetails().getId()));
        orderDetail.setCreatedAt(LocalDateTime.now());
        orderDetail.setAddress(orderDetailsDto.getAddress());
        orderDetail.setPrice(orderDetailsDto.getPrice());
        orderDetail.setPhoneNumber(orderDetailsDto.getPhone());
        orderDetail.setStatus(OrderStatus.CREATED);

        List<OrderItem> orderItemList = orderDetailsDto.getOrderItemDtoList()
                .stream()
                .map(item -> orderItemRepository.findById(item.getId()).orElse(null))
                .toList();
        orderItemList.forEach(item-> item.setOrderDetail(orderDetail));
        return orderDetailsRepository.save(orderDetail);
    }
    private UserDto getUserDetails(){
        return (UserDto) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    }
}
