package com.nhom23.orderapp.service;

import com.nhom23.orderapp.dto.OrderDetailsDto;
import com.nhom23.orderapp.dto.OrderItemDto;
import com.nhom23.orderapp.dto.StoreDto;
import com.nhom23.orderapp.dto.UserDto;
import com.nhom23.orderapp.exception.NotFoundException;
import com.nhom23.orderapp.model.OrderDetail;
import com.nhom23.orderapp.model.OrderItem;
import com.nhom23.orderapp.model.OrderStatus;
import com.nhom23.orderapp.repository.*;
import org.hibernate.query.Order;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
@Transactional(readOnly = true)
public class OrderDetailsService {
    @Autowired
    private ShipperRepository shipperRepository;
    @Autowired
    private OrderItemRepository orderItemRepository;
    @Autowired
    private CustomerRepository customerRepository;
    @Autowired
    private StoreRepository storeRepository;
    @Autowired
    private OrderDetailsRepository orderDetailsRepository;

    @Transactional
    public OrderDetailsDto createOrderDetails(
            String phone,
            String address,
            List<OrderItemDto> list,
            Double price,
            String userName,
            StoreDto storeDto
    ){
        OrderDetail orderDetail = new OrderDetail();
        orderDetail.setStore(storeRepository.getReferenceById(storeDto.getId()));
        orderDetail.setCustomer(customerRepository.getReferenceById(getUserDetails().getId()));
        //UTC Timezone without nano second display
        LocalDateTime localDateTime = LocalDateTime.ofInstant(
                Instant.now(), ZoneId.of("UTC")).withNano(0);
        orderDetail.setCreatedAt(localDateTime);
        orderDetail.setAddress(address);
        orderDetail.setPrice(price);
        orderDetail.setPhone(phone);
        orderDetail.setStatus(OrderStatus.CREATED);

        List<OrderItem> orderItemList = list
                .stream()
                .map(item -> orderItemRepository.findById(item.getId()).orElse(null))
                .toList();
        orderItemList.forEach(item-> item.setOrderDetail(orderDetail));
        OrderDetail od = orderDetailsRepository.save(orderDetail);
        return new OrderDetailsDto(
                od.getId(),phone,address,list,price,userName,
                localDateTime.toString(),OrderStatus.CREATED,storeDto.getAddress()
        );
    }
    public List<OrderDetailsDto> getAllOrder(){
        List<OrderDetailsDto> orderDetailsDto = orderDetailsRepository.findAllByManagerId(getUserDetails().getId());
        orderDetailsDto.forEach(
                dto -> dto.setOrderItemDtoList(orderItemRepository.findByOrderDetailsId(dto.getId()))
        );
        return orderDetailsDto;
    }
    public OrderDetailsDto getOrderById(Long id){
        OrderDetailsDto orderDetailsDto = orderDetailsRepository.findDtoById(id)
                .orElseThrow(() -> new NotFoundException("Order not found"));
        orderDetailsDto.setOrderItemDtoList(orderItemRepository.findByOrderDetailsId(orderDetailsDto.getId()));
        return orderDetailsDto;
    }
    @Transactional
    public Long delegateJob(Long id,Long shipperId){
        OrderDetail orderDetail = orderDetailsRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Order not found"));
        orderDetail.setShipper(shipperRepository.getReferenceById(shipperId));
        orderDetail.setStatus(OrderStatus.DELIVERING);
        orderDetailsRepository.save(orderDetail);
        return id;
    }
    private UserDto getUserDetails(){
        return (UserDto) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    }

}
