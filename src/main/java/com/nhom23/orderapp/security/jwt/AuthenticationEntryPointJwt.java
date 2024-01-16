package com.nhom23.orderapp.security.jwt;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nhom23.orderapp.response.ErrorResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.access.ExceptionTranslationFilter;
import org.springframework.security.web.access.intercept.AuthorizationFilter;
import org.springframework.web.servlet.mvc.method.annotation.ExceptionHandlerExceptionResolver;


import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.AccessDeniedException;


public class AuthenticationEntryPointJwt implements AuthenticationEntryPoint {
    private final ObjectMapper mapper = new ObjectMapper();
    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException {
        ErrorResponse errorResponse = new ErrorResponse(authException.getMessage(),HttpStatus.UNAUTHORIZED);
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        OutputStream os = response.getOutputStream();
        mapper.writeValue(os,errorResponse);
        os.flush();
    }
}
