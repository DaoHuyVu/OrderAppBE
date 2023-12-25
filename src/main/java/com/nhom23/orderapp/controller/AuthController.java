package com.nhom23.orderapp.controller;

import com.nhom23.orderapp.request.LoginRequest;
import com.nhom23.orderapp.request.SignUpRequest;
import com.nhom23.orderapp.response.AuthResponse;
import com.nhom23.orderapp.response.Response;
import com.nhom23.orderapp.service.AuthService;
import com.nhom23.orderapp.service.AdminService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(AuthController.CLASS_MAPPING)
public class AuthController {
    final static String CLASS_MAPPING = "/api/auth";
    @Autowired
    private AuthService authService;
    @Autowired
    private AdminService adminService;

    private static final Logger logger = LoggerFactory.getLogger(AuthController.class);
    @PostMapping("/login")
    public ResponseEntity<?> login(@Valid @RequestBody LoginRequest loginRequest){
        AuthResponse authResponse = authService.login(loginRequest);
        return new ResponseEntity<>(authResponse,HttpStatus.OK);
    }
    @PostMapping("/signup")
    public ResponseEntity<?> signUp(
            @Valid @RequestBody SignUpRequest signUpRequest,
            HttpServletRequest request){
        String requestUrl = request.getRequestURL().toString();
        String siteUrl = requestUrl.replace(request.getServletPath(),CLASS_MAPPING);
        Response response = authService.signUp(signUpRequest,siteUrl);
        return new ResponseEntity<>(response,HttpStatus.CREATED);
    }
    @GetMapping("/verify")
    public ResponseEntity<?> verify(@RequestParam("email") String email){
        Response response = authService.verify(email);
        return ResponseEntity.ok().body(response);
    }
}
