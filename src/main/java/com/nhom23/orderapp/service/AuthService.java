package com.nhom23.orderapp.service;

import com.nhom23.orderapp.exception.AlreadyExistException;
import com.nhom23.orderapp.exception.NotFoundException;
import com.nhom23.orderapp.model.*;
import com.nhom23.orderapp.repository.CustomerRepository;
import com.nhom23.orderapp.repository.RoleRepository;
import com.nhom23.orderapp.repository.AccountRepository;
import com.nhom23.orderapp.repository.AccountRoleRepository;
import com.nhom23.orderapp.request.LoginRequest;
import com.nhom23.orderapp.request.SignUpRequest;
import com.nhom23.orderapp.response.AuthResponse;
import com.nhom23.orderapp.response.Response;
import com.nhom23.orderapp.security.jwt.JwtUtil;
import com.nhom23.orderapp.security.service.UserDetailsImp;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@Service
public class AuthService {

    private static final Logger logger = LoggerFactory.getLogger(AuthService.class);
    @Autowired
    private JavaMailSender sender;
    @Autowired
    private CustomerRepository customerRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private RoleRepository roleRepository;
    @Autowired
    private AccountRoleRepository accountRoleRepository;
    @Autowired
    private JwtUtil jwtUtil;
    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private AccountRepository accountRepository;
    @Value("${spring.mail.username}")
    private String adminEmail;
    @PostAuthorize("hasAuthority('ROLE_USER')")
    public AuthResponse login(LoginRequest loginRequest,String url){
        UsernamePasswordAuthenticationToken token =
                new UsernamePasswordAuthenticationToken(
                        loginRequest.getUserName(),
                        loginRequest.getPassword());
        Authentication authentication = authenticationManager.authenticate(token);
        UserDetailsImp userDetails = (UserDetailsImp) authentication.getPrincipal();
        SecurityContextHolder.getContext().setAuthentication(authentication);
        String accessToken = jwtUtil.generateAccessTokenFromAccount(userDetails.getUsername(),url);
        Customer customer = customerRepository.findById(userDetails.getId()).orElseThrow(null);
        return new AuthResponse(accessToken, customer.getUserName(),userDetails.getRoles());
    }

    public Response signUp(SignUpRequest signUpRequest,String siteUrl){
        if(accountRepository.existsByEmail(signUpRequest.getEmail()))
            throw new AlreadyExistException("Email already exist");
        Account account = new Account(
                signUpRequest.getEmail(),
                passwordEncoder.encode(signUpRequest.getPassword())
        );
        Customer customer = new Customer(signUpRequest.getUserName());
        customer.setAccount(account);
        Role role = roleRepository.findByRole(ERole.ROLE_USER);

        AccountRole accountRole = new AccountRole();
        accountRole.setRole(role);
        accountRole.setAccount(account);

        accountRepository.save(account);
        accountRoleRepository.save(accountRole);
        customerRepository.save(customer);
        roleRepository.save(role);
        
        sendVerificationEMail(customer,account,siteUrl);
        
        return new Response("Sign up successfully");
    }
    public void sendVerificationEMail(
            Customer customer,
            Account account,
            String siteUrl){
        try{
            String fromAddress = adminEmail;
            String toAddress = account.getEmail();
            MimeMessage message = sender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message);
            helper.addTo(toAddress);
            helper.setFrom(fromAddress);
            helper.setSubject("Activate account");
            String content = getString(customer, siteUrl,account);
            helper.setText(content,true);
            sender.send(message);
        }catch(MessagingException ex){
            logger.error("Can't send verification Email");
        }
    }

    private static String getString(Customer customer, String siteUrl,Account account) {
        String verifyUrl = siteUrl + "/verify?email=" + account.getEmail();
        return String.format(
                "Dear %s,<br>"
                        + "Please click the link below to verify your registration:<br>"
                        + "<h3><a rel =\"icon\" href=\"%s\" target=\"_self\">VERIFY</a></h3>"
                        + "Thank you,<br>"
                        + "OrderApp.",customer.getUserName(),verifyUrl
        );
    }
    public Response verify(String email){
        Account account = accountRepository.findAccountByEmail(email)
                .orElseThrow(()->new NotFoundException("Account is not exist"));
        if(!account.getIsEnable()){
            account.setIsEnable(true);
            accountRepository.save(account);
            return new Response("Account activated");
        }
        else
            return new Response("Your account has already been activated");
    }
}
