package com.nhom23.orderapp.service;

import com.nhom23.orderapp.dto.Revenue;
import com.nhom23.orderapp.repository.OrderDetailsRepository;
import com.nhom23.orderapp.repository.StoreRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
@Transactional(readOnly = true)
public class RevenueService {
    @Autowired
    private OrderDetailsRepository orderDetailsRepository;
    @Autowired
    private StoreRepository storeRepository;
    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    public List<Revenue> getRevenue(String from,String to){
        LocalDate fromLocalDate = LocalDate.parse(from,formatter);
        LocalDate toLocalDate = LocalDate.parse(to,formatter);
        return storeRepository.findRevenue(fromLocalDate,toLocalDate);
    }
    public List<Revenue> getRevenueOfMenuItem(String from,String to,String menuItem){
        LocalDate fromLocalDate = LocalDate.parse(from,formatter);
        LocalDate toLocalDate = LocalDate.parse(to,formatter);
        return storeRepository.findRevenueOfMenuItem(fromLocalDate,toLocalDate,menuItem);
    }
}
