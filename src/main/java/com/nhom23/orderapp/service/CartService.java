package com.nhom23.orderapp.service;

import com.nhom23.orderapp.dto.OrderItemDto;
import com.nhom23.orderapp.exception.NotFoundException;
import com.nhom23.orderapp.model.MenuItem;
import com.nhom23.orderapp.model.OrderItem;
import com.nhom23.orderapp.repository.CustomerRepository;
import com.nhom23.orderapp.repository.MenuRepository;
import com.nhom23.orderapp.repository.OrderItemRepository;
import com.nhom23.orderapp.security.service.UserDetailsImp;
import com.nhom23.orderapp.security.service.UserDetailsServiceImp;
import org.apache.catalina.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
public class CartService {
    @Autowired
    private MenuRepository menuRepository;
    @Autowired
    private CustomerRepository customerRepository;
    @Autowired
    private OrderItemRepository orderItemRepository;
    @Transactional()
    public OrderItemDto addItem(Long itemId,Integer quantity){
        UserDetailsImp user = getUserDetails();
        Optional<OrderItemDto> existingItemOptional
                = orderItemRepository.findByItemIdAndCustomerId(itemId,user.getId());
        if(existingItemOptional.isPresent()){
            OrderItemDto existingItem = existingItemOptional.get();
            int newQuantity = existingItem.getQuantity() + quantity;
            return modifyQuantity(existingItem.getId(),newQuantity);
        }
        MenuItem menuItem = menuRepository.getReferenceById(itemId);
        OrderItem orderItem = new OrderItem();
        orderItem.setCustomer(
                customerRepository.getReferenceById(user.getId())
        );
        orderItem.setItem(menuItem);
        orderItem.setQuantity(quantity);
        return orderItemRepository.save(orderItem).toItemDto();
    }
    public List<OrderItemDto> getAllCartItem(){
        UserDetailsImp user = getUserDetails();
        return orderItemRepository.findByEmail(user.getEmail())
                .orElseThrow(() -> new NotFoundException("User not found"));
    }
    @Transactional
    public OrderItemDto modifyQuantity(Long id,Integer quantity){
        UserDetailsImp user = getUserDetails();
        orderItemRepository.updateQuantity(id,user.getId(),quantity);
        return orderItemRepository.getItemDto(id,user.getId())
                .orElseThrow(() -> new NotFoundException("Order item not found"));
    }
    @Transactional
    public OrderItemDto deleteItem(Long id){
        UserDetailsImp user = getUserDetails();
        OrderItemDto item = orderItemRepository.findByIdAndCustomerId(id, user.getId())
                .orElseThrow(() ->new NotFoundException("Order item not found"));
        orderItemRepository.deleteById(id);
        return item;
    }
    private UserDetailsImp getUserDetails(){
        return (UserDetailsImp) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    }

}
