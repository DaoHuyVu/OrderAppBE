package com.nhom23.orderapp.helper;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nhom23.orderapp.response.ErrorResponse;
import io.jsonwebtoken.JwtException;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import java.io.IOException;
import java.io.OutputStream;

public class ExceptionResolver {
    private static final ObjectMapper mapper = new ObjectMapper();
    public static void resolveJwtException(HttpServletResponse response, JwtException ex) throws IOException {
        ErrorResponse errorResponse = new ErrorResponse(ex.getMessage(), HttpStatus.UNAUTHORIZED);
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        OutputStream os = response.getOutputStream();
        mapper.writeValue(os,errorResponse);
        os.flush();
    }
    public static void resolveException(HttpServletResponse response, Exception ex) throws IOException {
        ErrorResponse errorResponse = new ErrorResponse(ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        OutputStream os = response.getOutputStream();
        mapper.writeValue(os,errorResponse);
        os.flush();
    }
}
