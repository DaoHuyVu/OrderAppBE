package com.nhom23.orderapp.service;

import com.nhom23.orderapp.dto.OrderDetailsDto;
import com.nhom23.orderapp.dto.UserDto;
import com.nhom23.orderapp.exception.NotFoundException;
import com.nhom23.orderapp.model.OrderItem;
import com.nhom23.orderapp.repository.OrderDetailsRepository;
import com.nhom23.orderapp.repository.OrderItemRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
public class CustomerService {
    @Autowired
    private OrderDetailsRepository orderDetailsRepository;
    @Autowired
    private OrderItemRepository orderItemRepository;

    public List<OrderDetailsDto> getAllOrder(){
        List<OrderDetailsDto> orderDetailsDto = orderDetailsRepository.findAllByCustomerId(getUserDetail().getId())
                .orElseThrow(() -> new NotFoundException("No order found"));
        orderDetailsDto.forEach(dto -> dto.setOrderItemDtoList(orderItemRepository.findByOrderDetailsId(dto.getId())));
        return orderDetailsDto;
    }
    private UserDto getUserDetail(){
        return (UserDto) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    }
}
