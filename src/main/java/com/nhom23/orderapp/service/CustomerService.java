package com.nhom23.orderapp.service;

import com.nhom23.orderapp.dto.OrderDetailsDto;
import com.nhom23.orderapp.dto.UserDto;
import com.nhom23.orderapp.exception.AlreadyExistException;
import com.nhom23.orderapp.exception.ForbiddenException;
import com.nhom23.orderapp.exception.NotFoundException;
import com.nhom23.orderapp.model.Account;
import com.nhom23.orderapp.model.OrderDetail;
import com.nhom23.orderapp.model.OrderItem;
import com.nhom23.orderapp.model.OrderStatus;
import com.nhom23.orderapp.repository.AccountRepository;
import com.nhom23.orderapp.repository.OrderDetailsRepository;
import com.nhom23.orderapp.repository.OrderItemRepository;
import org.apache.coyote.BadRequestException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
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
    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private AccountRepository accountRepository;
    @Autowired
    private PasswordEncoder encoder;
    public List<OrderDetailsDto> getAllOrder(){
        List<OrderDetailsDto> orderDetailsDto = orderDetailsRepository.findAllByCustomerId(getUserDetail().getId())
                .orElseThrow(() -> new NotFoundException("No order found"));
        orderDetailsDto.forEach(dto -> dto.setOrderItemDtoList(orderItemRepository.findByOrderDetailsId(dto.getId())));
        return orderDetailsDto;
    }
    @Transactional
    public OrderDetailsDto cancelOrder(Long id){
        OrderDetail order = orderDetailsRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Order not found"));
        if(order.getStatus() == OrderStatus.CREATED){
            order.setStatus(OrderStatus.CANCELLED);
            return order.toDto();
        }
        else throw new ForbiddenException("Order had been delivered, thus can't be cancelled");
    }
    @Transactional
    public void changePassword(String oldPassword,String newPassword){
        UserDto user = getUserDetail();
        Account account = accountRepository.findAccountByEmail(user.getEmail())
                .orElseThrow(() -> new NotFoundException("Account does not exist"));
        if(!encoder.matches(oldPassword,account.getPassword())){
            throw new ForbiddenException("Old password does not match ");
        }
        account.setPassword(encoder.encode(newPassword));

        accountRepository.save(account);
    }
    private UserDto getUserDetail(){
        return (UserDto) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    }

}
