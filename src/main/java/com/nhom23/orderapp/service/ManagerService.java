package com.nhom23.orderapp.service;

import com.nhom23.orderapp.dto.ManagerDto;
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
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestParam;

import java.text.NumberFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
@Transactional(readOnly = true)
public class ManagerService {
    @Autowired
    private JwtUtil jwtUtil;
    @Autowired
    private PasswordEncoder encoder;
    @Autowired
    private StoreRepository storeRepository;
    @Autowired
    private ManagerRepository managerRepository;
    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private AccountRoleRepository accountRoleRepository;
    @Autowired
    private AccountRepository accountRepository;
    @Autowired
    private RoleRepository roleRepository;

    @Transactional()
    public ManagerDto addManagerAccount(
            String email,
            String password,
            String name,
            String phone,
            Long storeId,
            Double salary,
            LocalDate dateOfBirth,
            String gender
    ){
        if(accountRepository.existsByEmail(email)){
            throw new AlreadyExistException("This email is already exist");
        }
        Account account = new Account(email,encoder.encode(password));
        account.setIsEnable(true);
        accountRepository.save(account);

        AccountRole accountRole = new AccountRole();

        Manager manager = new Manager(
                name,phone,dateOfBirth,salary,Gender.valueOf(gender)
        );
        Role role = roleRepository.findByRole(ERole.ROLE_MANAGER);
        accountRole.setAccount(account);
        accountRole.setRole(role);

        manager.setAccount(account);
        manager.setStore(storeRepository.getReferenceById(storeId));

        accountRoleRepository.save(accountRole);
        return managerRepository.save(manager).toDto();
    }
    public AuthResponse login(LoginRequest loginRequest){
        UsernamePasswordAuthenticationToken token =
                new UsernamePasswordAuthenticationToken(
                        loginRequest.getUserName(),
                        loginRequest.getPassword());
        Authentication authentication = authenticationManager.authenticate(token);
        UserDetailsImp userDetails = (UserDetailsImp) authentication.getPrincipal();
        String accessToken = jwtUtil.generateAccessTokenFromAccount(userDetails.getUsername());
        Manager manager = managerRepository.findById(userDetails.getId()).orElseThrow(null);
        return new AuthResponse(accessToken, manager.getName(),userDetails.getRoles());
    }
    public List<ManagerDto> getAllManager(){
        return managerRepository.findAllManager();
    }
    @Transactional
    public ManagerDto deleteManager(Long id){
        return managerRepository.deleteManager(id);
    }
    @Transactional
    public ManagerDto updateManager(
            Long id,
            String email,
            String name,
            String phone,
            String salary,
            String dateOfBirth,
            String gender
    ){
        Manager manager = managerRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Manager not found"));
        if(accountRepository.existsByEmail(email))
            throw new AlreadyExistException("Email already exists");
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        NumberFormat numberFormat = NumberFormat.getCurrencyInstance();
        numberFormat.setMaximumFractionDigits(0);
        manager.setName(name);
        manager.setSalary(Double.valueOf(salary));
        manager.setPhoneNumber(phone);
        manager.setDateOfBirth(LocalDate.parse(dateOfBirth,formatter));
        manager.setGender(Gender.valueOf(gender));
        manager.getAccount().setEmail(email);
        return managerRepository.save(manager).toDto();
    }
}
