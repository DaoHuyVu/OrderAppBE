package com.nhom23.orderapp.service;

import com.nhom23.orderapp.dto.ManagerDto;
import com.nhom23.orderapp.dto.ShipperDto;
import com.nhom23.orderapp.model.MenuItem;
import com.nhom23.orderapp.model.Store;
import com.nhom23.orderapp.repository.ManagerRepository;
import com.nhom23.orderapp.repository.MenuRepository;
import com.nhom23.orderapp.repository.ShipperRepository;
import com.nhom23.orderapp.repository.StoreRepository;
import com.nhom23.orderapp.response.AuthResponse;
import com.nhom23.orderapp.security.jwt.JwtUtil;
import com.nhom23.orderapp.security.service.UserDetailsImp;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
public class AdminService {
    @Autowired
    private ManagerRepository managerRepository;
    @Autowired
    private StoreRepository storeRepository;
    @Autowired
    private MenuRepository menuRepository;
    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private ShipperRepository shipperRepository;
    @Autowired
    private JwtUtil jwtUtil;
    @Transactional
    public AuthResponse login(String userName,String password,String url){
        UsernamePasswordAuthenticationToken token =
                new UsernamePasswordAuthenticationToken(
                        userName,
                        password);
        Authentication authentication = authenticationManager.authenticate(token);
        UserDetailsImp userDetails = (UserDetailsImp) authentication.getPrincipal();
        String accessToken = jwtUtil.generateAccessTokenFromAccount(userDetails.getUsername(),url);
        return new AuthResponse(accessToken,userDetails.getUsername(),userDetails.getRoles());
    }
}
