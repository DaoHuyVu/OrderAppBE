package com.nhom23.orderapp.service;

import com.nhom23.orderapp.repository.StaffRepository;
import com.nhom23.orderapp.repository.MenuRepository;
import com.nhom23.orderapp.repository.StoreRepository;
import com.nhom23.orderapp.response.AuthResponse;
import com.nhom23.orderapp.security.jwt.JwtUtil;
import com.nhom23.orderapp.security.service.UserDetailsImp;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.hibernate5.support.OpenSessionInViewFilter;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service

public class AdminService {
    @Autowired
    private StaffRepository staffRepository;
    @Autowired
    private StoreRepository storeRepository;
    @Autowired
    private MenuRepository menuRepository;
    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private JwtUtil jwtUtil;
    @PostAuthorize("hasAuthority('ROLE_ADMIN')")
    public AuthResponse login(String userName,String password,String url){
        UsernamePasswordAuthenticationToken token =
                new UsernamePasswordAuthenticationToken(
                        userName,
                        password);
        Authentication authentication = authenticationManager.authenticate(token);
        SecurityContextHolder.getContext().setAuthentication(authentication);
        UserDetailsImp userDetails = (UserDetailsImp) authentication.getPrincipal();
        String accessToken = jwtUtil.generateAccessTokenFromAccount(userDetails.getUsername(),url);
        return new AuthResponse(accessToken,userDetails.getUsername(),userDetails.getRoles());
    }
}
