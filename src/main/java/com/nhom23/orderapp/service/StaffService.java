package com.nhom23.orderapp.service;

import com.nhom23.orderapp.dto.OrderDetailsDto;
import com.nhom23.orderapp.dto.StaffDto;
import com.nhom23.orderapp.dto.UserDto;
import com.nhom23.orderapp.exception.AlreadyExistException;
import com.nhom23.orderapp.exception.NotFoundException;
import com.nhom23.orderapp.model.*;
import com.nhom23.orderapp.repository.*;
import com.nhom23.orderapp.request.LoginRequest;
import com.nhom23.orderapp.response.AuthResponse;
import com.nhom23.orderapp.security.jwt.JwtUtil;
import com.nhom23.orderapp.security.service.UserDetailsImp;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ReflectionUtils;

import java.lang.reflect.Field;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Collections;
import java.util.List;
import java.util.Map;

@Service
@Transactional(readOnly = true)
public class StaffService {
    @Autowired
    private JwtUtil jwtUtil;
    @Autowired
    private PasswordEncoder encoder;
    @Autowired
    private StoreRepository storeRepository;
    @Autowired
    private StaffRepository staffRepository;
    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private AccountRoleRepository accountRoleRepository;
    @Autowired
    private AccountRepository accountRepository;
    @Autowired
    private RoleRepository roleRepository;
    @Autowired
    private OrderDetailsRepository orderDetailsRepository;
    @Autowired
    private OrderItemRepository orderItemRepository;

    @Transactional()
    public StaffDto addStaff(
            String email,
            String password,
            String name,
            String phone,
            Long storeId,
            Double salary,
            LocalDate dateOfBirth,
            String gender,
            String role
    ){
        if(accountRepository.existsByEmail(email)){
            throw new AlreadyExistException("This email is already exist");
        }
        Account account = new Account(email,encoder.encode(password));
        account.setIsEnable(true);
        accountRepository.save(account);
        Staff staff = new Staff(
                name,phone,dateOfBirth,salary,Gender.valueOf(gender)
        );
        Role role1 = roleRepository.findByRole(ERole.valueOf(role));

        AccountRole accountRole1 = new AccountRole();
        accountRole1.setAccount(account);
        accountRole1.setRole(role1);

        Role role2 = roleRepository.findByRole(ERole.ROLE_USER);
        AccountRole accountRole2 = new AccountRole();
        accountRole2.setRole(role2);
        accountRole2.setAccount(account);

        staff.setAccount(account);
        staff.setStore(storeRepository.getReferenceById(storeId));

        accountRoleRepository.save(accountRole1);
        accountRoleRepository.save(accountRole2);
        return staffRepository.save(staff).toDto();
    }
    @PostAuthorize("hasAnyAuthority('ROLE_MANAGER','ROLE_STAFF')")
    public AuthResponse login(LoginRequest loginRequest,String url){
        UsernamePasswordAuthenticationToken token =
                new UsernamePasswordAuthenticationToken(
                        loginRequest.getUserName(),
                        loginRequest.getPassword());
        Authentication authentication = authenticationManager.authenticate(token);
        UserDetailsImp userDetails = (UserDetailsImp) authentication.getPrincipal();
        SecurityContextHolder.getContext().setAuthentication(authentication);
        String accessToken = jwtUtil.generateAccessTokenFromAccount(userDetails.getUsername(),url);
        Staff staff = staffRepository.findById(userDetails.getId()).orElseThrow(null);
        return new AuthResponse(accessToken, staff.getName(),userDetails.getRoles());
    }
    public List<StaffDto> getAllManager(){
        return staffRepository.findAllManager();
    }
    @Transactional
    public StaffDto deleteManager(Long id){
        return staffRepository.deleteManager(id);
    }
    @Transactional
    public StaffDto deleteShipper(Long id){
        return staffRepository.deleteShipper(id);
    }
    @Transactional
    public StaffDto updateStaff(Long id, Map<String,String> fields){
        Staff staff = staffRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Manager not found"));
        fields.forEach((key,value) -> {
            if (key.equals("email")){
                if(accountRepository.existsByEmail(value))
                    throw new AlreadyExistException("Email already exist");
                staff.getAccount().setEmail(value);
            }
            else {
                Field field = ReflectionUtils.findField(Staff.class,key);
                if(field != null){
                    field.setAccessible(true);
                    if(field.getType().getCanonicalName().equals(LocalDate.class.getCanonicalName())){
                        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
                        LocalDate newValue = LocalDate.parse(value,formatter);
                        ReflectionUtils.setField(field,staff,newValue);
                    }
                    else if(field.getType().getCanonicalName().equals(Double.class.getCanonicalName())){
                        Double newValue = Double.valueOf(value);
                        ReflectionUtils.setField(field,staff,newValue);
                    }
                    else ReflectionUtils.setField(field,staff,value);
                }
            }
        });
        return staffRepository.save(staff).toDto();
    }
    public List<StaffDto> getAllShipperOfStore(){
        Long storeId = staffRepository.findStoreIdByManagerId(getUserDetail()
                .getId()).orElseThrow(()-> new NotFoundException("Store not found"));
        return staffRepository.findAllShipperByStoreId(storeId);
    }

    private UserDto getUserDetail(){
        return (UserDto) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    }
    public List<OrderDetailsDto> getAllOrder(){
        List<OrderDetailsDto> orderDetails = orderDetailsRepository
                .findAllByShipperId(getUserDetail().getId());
        orderDetails.forEach(od -> od.setOrderItemDtoList(orderItemRepository.findByOrderDetailsId(od.getId())));
        return orderDetails;
    }
    @Transactional
    public Map<Long,String> informOrder(Long id,Boolean isSucceed){
        OrderDetail orderDetail = orderDetailsRepository.findById(id).orElseThrow(() -> new NotFoundException("Order not found"));
        if(isSucceed){
            orderDetail.setStatus(OrderStatus.DELIVERED);
            return Collections.singletonMap(id, OrderStatus.DELIVERED.name());
        }
        else orderDetail.setStatus(OrderStatus.CANCELLED);
        return Collections.singletonMap(id,OrderStatus.CANCELLED.name());
    }
    public List<StaffDto> getAllShipper(){
        return staffRepository.findAllShipper();
    }
}
