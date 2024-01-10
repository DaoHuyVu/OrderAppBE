package com.nhom23.orderapp.service;

import com.nhom23.orderapp.dto.ManagerDto;
import com.nhom23.orderapp.dto.OrderDetailsDto;
import com.nhom23.orderapp.dto.ShipperDto;
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
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ReflectionUtils;

import java.lang.reflect.Field;
import java.text.NumberFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Collections;
import java.util.List;
import java.util.Map;

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
    @Autowired
    private OrderItemRepository orderItemRepository;
    @Autowired
    private OrderDetailsRepository orderDetailsRepository;
    @Transactional
    public ShipperDto addShipper(
            String email,
            String password,
            String name,
            String phone,
            Double salary,
            LocalDate dateOfBirth,
            String gender,
            Long storeId
    ){
        if(accountRepository.existsByEmail(email)){
            throw new AlreadyExistException("This email is already exist");
        }
        //Create shipper account
        Account account = new Account(email, encoder.encode(password));
        account.setIsEnable(true);
        AccountRole accountRole = new AccountRole();
        Role role = roleRepository.findByRole(ERole.ROLE_STAFF);

        accountRole.setAccount(account);
        accountRole.setRole(role);

        accountRepository.save(account);

        //Create shipper instance
        Shipper shipper = new Shipper(
            name,phone,dateOfBirth,salary,Gender.valueOf(gender)
        );
        shipper.setAccount(account);
        shipper.setStore(storeRepository.getReferenceById(storeId));


        accountRoleRepository.save(accountRole);
        return shipperRepository.save(shipper).toDto();
    }
    @Transactional
    public AuthResponse login(LoginRequest loginRequest,String url){
        UsernamePasswordAuthenticationToken token =
                new UsernamePasswordAuthenticationToken(
                        loginRequest.getUserName(),
                        loginRequest.getPassword());
        Authentication authentication = authenticationManager.authenticate(token);
        UserDetailsImp userDetails = (UserDetailsImp) authentication.getPrincipal();
        String accessToken = jwtUtil.generateAccessTokenFromAccount(userDetails.getUsername(),url);
        Shipper shipper = shipperRepository.findById(userDetails.getId()).orElseThrow(null);
        return new AuthResponse(accessToken, shipper.getName(),userDetails.getRoles());
    }
    private UserDto getUserDetail(){
        return (UserDto) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    }
    //Find all shipper of a store
    public List<ShipperDto> getAllShipperOfStore(){
        Long storeId = managerRepository.findStoreIdByManagerId(getUserDetail()
                .getId()).orElseThrow(()-> new NotFoundException("Store not found"));
        return shipperRepository.findAllByStoreId(storeId);
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
    public List<ShipperDto> getAllShipper(){
        return shipperRepository.findAllShipper();
    }
    @Transactional
    public ShipperDto deleteShipper(Long id){
        return shipperRepository.deleteShipper(id);
    }
    @Transactional
    public ShipperDto updateShipper(
            Long id,
            String email,
            String name,
            String phone,
            String salary,
            String dateOfBirth,
            String gender
    ){
        Shipper shipper = shipperRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Shipper not found"));
        if(accountRepository.existsByEmail(email))
            throw new AlreadyExistException("Email already exists");
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        NumberFormat numberFormat = NumberFormat.getCurrencyInstance();

        numberFormat.setMaximumFractionDigits(0);
        shipper.setName(name);
        shipper.setSalary(Double.valueOf(salary));
        shipper.setPhone(phone);
        shipper.setDateOfBirth(LocalDate.parse(dateOfBirth,formatter));
        shipper.setGender(Gender.valueOf(gender));
        shipper.getAccount().setEmail(email);
        return shipperRepository.save(shipper).toDto();
    }
    @Transactional
    public ShipperDto updateShipper(Long id, Map<String,String> fields){
        Shipper shipper = shipperRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Shipper not found"));
        fields.forEach((key,value) -> {
            if (key.equals("email")){
                if(accountRepository.existsByEmail(value))
                    throw new AlreadyExistException("Email already exist");
                shipper.getAccount().setEmail(value);
            }
            else {
                Field field = ReflectionUtils.findField(Shipper.class,key);
                if(field != null){
                    field.setAccessible(true);
                    if(field.getType().getCanonicalName().equals(LocalDate.class.getCanonicalName())){
                        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
                        LocalDate newValue = LocalDate.parse(value,formatter);
                        ReflectionUtils.setField(field,shipper,newValue);
                    }
                    else if(field.getType().getCanonicalName().equals(Double.class.getCanonicalName())){
                        Double newValue = Double.valueOf(value);
                        ReflectionUtils.setField(field,shipper,newValue);
                    }
                    else ReflectionUtils.setField(field,shipper,value);
                }
            }
        });
        return shipperRepository.save(shipper).toDto();
    }
}
