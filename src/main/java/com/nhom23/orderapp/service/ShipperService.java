package com.nhom23.orderapp.service;

import com.nhom23.orderapp.dto.ShipperDto;
import com.nhom23.orderapp.exception.AlreadyExistException;
import com.nhom23.orderapp.exception.NotFoundException;
import com.nhom23.orderapp.model.*;
import com.nhom23.orderapp.repository.*;
import com.nhom23.orderapp.request.LoginRequest;
import com.nhom23.orderapp.response.AuthResponse;
import com.nhom23.orderapp.security.jwt.JwtUtil;
import com.nhom23.orderapp.security.service.UserDetailsImp;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDate;

@Service
@Transactional(readOnly = true)
public class ShipperService {
    @Autowired
    private StoreRepository storeRepository;
    @Autowired
    private PasswordEncoder encoder;
    @Autowired
    private JwtUtil jwtUtil;
    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private ManagerRepository managerRepository;
    @Autowired
    private AccountRoleRepository accountRoleRepository;
    @Autowired
    private AccountRepository accountRepository;
    @Autowired
    private ShipperRepository shipperRepository;
    @Autowired
    private RoleRepository roleRepository;
    @Transactional
    public ShipperDto addShipper(
            String email,
            String password,
            String name,
            String phone,
            Integer salary,
            LocalDate dateOfBirth,
            String gender
    ){
        if(accountRepository.existsByEmail(email)){
            throw new AlreadyExistException("This email is already exist");
        }
        //Create shipper account
        Account account = new Account(email, encoder.encode(password));
        account.setIsEnable(true);
        AccountRole accountRole = new AccountRole();
        Role role = roleRepository.findByRole(ERole.ROLE_STAFF);

        role.addRole(accountRole);
        account.addRole(accountRole);
        accountRepository.save(account);

        //Create shipper instance
        Shipper shipper = new Shipper(
            name,phone,dateOfBirth,salary,Gender.valueOf(gender)
        );
        UserDetailsImp user = getUserDetail();
        Long storeId = managerRepository.findStoreIdByManagerId(user.getId())
                .orElseThrow(()-> new NotFoundException("Store not found"));
        shipper.setAccount(account);
        shipper.setStore(storeRepository.getReferenceById(storeId));


        accountRoleRepository.save(accountRole);
        return shipperRepository.save(shipper).toDto();
    }
    public AuthResponse login(LoginRequest loginRequest){
        UsernamePasswordAuthenticationToken token =
                new UsernamePasswordAuthenticationToken(
                        loginRequest.getEmail(),
                        loginRequest.getPassword());
        Authentication authentication = authenticationManager.authenticate(token);
        UserDetailsImp userDetails = (UserDetailsImp) authentication.getPrincipal();
        String accessToken = jwtUtil.generateAccessTokenFromAccount(userDetails.getUsername());
        Shipper shipper = shipperRepository.findById(userDetails.getId()).orElseThrow(null);
        return new AuthResponse(accessToken, shipper.getName(),userDetails.getAuthorities());
    }
    private UserDetailsImp getUserDetail(){
        return (UserDetailsImp) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    }
}
