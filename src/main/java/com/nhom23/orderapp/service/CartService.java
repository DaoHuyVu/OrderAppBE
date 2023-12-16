package com.nhom23.orderapp.service;

import com.nhom23.orderapp.dto.OrderItemDto;
import com.nhom23.orderapp.dto.UserDto;
import com.nhom23.orderapp.exception.NotFoundException;
import com.nhom23.orderapp.model.MenuItem;
import com.nhom23.orderapp.model.OrderItem;
import com.nhom23.orderapp.repository.CustomerRepository;
import com.nhom23.orderapp.repository.MenuRepository;
import com.nhom23.orderapp.repository.OrderItemRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
        Optional<OrderItemDto> existingItemOptional
                = orderItemRepository.findByItemIdAndCustomerIdAndNotYetBeIncludedInOrderDetail(itemId,getUserDetails().getId());
        if(existingItemOptional.isPresent()){
            OrderItemDto existingItem = existingItemOptional.get();
            int newQuantity = existingItem.getQuantity() + quantity;
            return modifyQuantity(existingItem.getId(),newQuantity);
        }
        MenuItem menuItem = menuRepository.getReferenceById(itemId);
        OrderItem orderItem = new OrderItem();
        orderItem.setCustomer(
                customerRepository.getReferenceById(getUserDetails().getId())
        );
        orderItem.setItem(menuItem);
        orderItem.setQuantity(quantity);
        return orderItemRepository.save(orderItem).toItemDto();
    }
    public List<OrderItemDto> getAllCartItem(){
        return orderItemRepository.findByEmail(getUserDetails().getEmail())
                .orElseThrow(() -> new NotFoundException("User not found"));
    }
    @Transactional
    public OrderItemDto modifyQuantity(Long id,Integer quantity){
        orderItemRepository.updateQuantity(id,getUserDetails().getId(),quantity);
        return orderItemRepository.getItemDto(id,getUserDetails().getId())
                .orElseThrow(() -> new NotFoundException("Order item not found"));
    }
    @Transactional
    public OrderItemDto deleteItem(Long id){
        OrderItemDto item = orderItemRepository.findByIdAndCustomerId(id, getUserDetails().getId())
                .orElseThrow(() -> new NotFoundException("Order item not found"));
        orderItemRepository.deleteById(id);
        return item;
    }
    private UserDto getUserDetails(){
        return (UserDto) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    }

}
