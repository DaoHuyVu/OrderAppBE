package com.nhom23.orderapp.helper;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nhom23.orderapp.response.ErrorResponse;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import java.io.IOException;
import java.io.OutputStream;

public class JwtExceptionResolver {
    private static final ObjectMapper mapper = new ObjectMapper();
    public static void resolve(HttpServletResponse response,Exception ex) throws IOException {
        ErrorResponse errorResponse = new ErrorResponse(ex.getMessage(), HttpStatus.UNAUTHORIZED);
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        OutputStream os = response.getOutputStream();
        mapper.writeValue(os,errorResponse);
        os.flush();
    }
}
