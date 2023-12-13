package com.nhom23.orderapp.service;

import com.nhom23.orderapp.exception.NotFoundException;
import com.nhom23.orderapp.model.Address;
import com.nhom23.orderapp.model.Store;
import com.nhom23.orderapp.repository.StoreRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.DateTimeException;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.List;

@Service
@Transactional(readOnly = true)
public class StoreService {
    @Autowired
    private StoreRepository storeRepository;
    @Transactional
    public Store addStore(Address address, LocalTime openingTime, LocalTime closingTime){
        Store store = new Store();
        store.setAddress(address);
        store.setClosingTime(closingTime);
        store.setOpeningTime(openingTime);
        return storeRepository.save(store);
    }
    public Store getStore(Long id){
        return storeRepository.findById(id).orElseThrow(() -> new NotFoundException("Store not found"));
    }
    public List<Store> getAllStore(){
        return storeRepository.findAll();
    }
}
