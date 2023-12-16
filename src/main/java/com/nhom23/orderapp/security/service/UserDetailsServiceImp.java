package com.nhom23.orderapp.security.service;

import com.nhom23.orderapp.exception.NotFoundException;
import com.nhom23.orderapp.model.Account;
import com.nhom23.orderapp.model.AccountRole;
import com.nhom23.orderapp.model.ERole;
import com.nhom23.orderapp.model.Role;
import com.nhom23.orderapp.repository.AccountRepository;
import com.nhom23.orderapp.repository.AccountRoleRepository;
import com.nhom23.orderapp.repository.RoleRepository;
import jakarta.persistence.Tuple;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

public class UserDetailsServiceImp implements UserDetailsService {
    @Autowired
    private AccountRepository accountRepository;
    @Autowired
    private AccountRoleRepository repository;
    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        List<Tuple> tuples = repository.findAccountAndRole(email)
                .orElseThrow(() -> new NotFoundException("User not found"));
        List<ERole> roles = tuples.stream().map(tuple ->(ERole) tuple.get(3)).toList();
        Long id = (Long) tuples.get(0).get(0);
        String password = (String) tuples.get(0).get(1);
        Boolean isEnable = (Boolean) tuples.get(0).get(2);
        return new UserDetailsImp(email,id,password,isEnable,roles);
    }
}
