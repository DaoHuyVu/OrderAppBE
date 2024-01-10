package com.nhom23.orderapp.service;

import com.nhom23.orderapp.dto.Revenue;
import com.nhom23.orderapp.repository.OrderDetailsRepository;
import com.nhom23.orderapp.repository.StoreRepository;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwt;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.crypto.SecretKey;
import java.security.Key;
import java.util.Base64;
import java.util.List;

@Service
@Transactional(readOnly = true)
public class RevenueService {
    @Autowired
    private OrderDetailsRepository orderDetailsRepository;
    @Autowired
    private StoreRepository storeRepository;

    public List<Revenue> getRevenue(Integer day, Integer month, Integer year){
        if(day == null && month == null)
            return storeRepository.findRevenue(year);
        else if (day == null )
            return storeRepository.findRevenue(month,year);
        return storeRepository.findRevenue(day,month,year);
    }
    public List<Revenue> getRevenueOfCategory(Integer day, Integer month, Integer year,String category){
        if(day == null && month == null)
            return storeRepository.findRevenueOfCategory(category,year);
        else if (day == null )
            return storeRepository.findRevenueOfCategory(category,month,year);
        return storeRepository.findRevenueOfCategory(category,day,month,year);
    }

}
