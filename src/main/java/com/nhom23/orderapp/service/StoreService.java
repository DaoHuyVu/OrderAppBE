package com.nhom23.orderapp.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nhom23.orderapp.exception.NotFoundException;
import com.nhom23.orderapp.model.Address;
import com.nhom23.orderapp.model.Store;
import com.nhom23.orderapp.repository.StoreRepository;
import io.jsonwebtoken.io.Decoders;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ReflectionUtils;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.lang.reflect.Field;
import java.time.DateTimeException;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.List;
import java.util.Map;

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
        return storeRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Store not found"));
    }
    public List<Store> getAllStore(){
            return storeRepository.findAll();
    }
    @Transactional
    public Store deleteStore(Long id){
        return storeRepository.deleteByStoreId(id);
    }
    @Transactional
    public Store updateStore(Long id, Map<String,String> fields){
        Store store = storeRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Store not found"));
        fields.forEach((key,value) -> {
            switch (key){
                case "city" -> store.getAddress().setCity(value);
                case "district" -> store.getAddress().setDistrict(value);
                case "street" -> store.getAddress().setStreet(value);
                default -> {
                    Field field = ReflectionUtils.findField(Store.class,key);
                    if(field != null){
                        field.setAccessible(true);
                        if (field.getType().getCanonicalName().equals(LocalTime.class.getCanonicalName())) {
                            LocalTime localTime = LocalTime.parse(value);
                            ReflectionUtils.setField(field, store, localTime);

                        } else ReflectionUtils.setField(field, store, value);
                    }
                    else throw new NotFoundException("Field " + key + " not found");
                }
            }
        });
        return storeRepository.save(store);
    }
}
