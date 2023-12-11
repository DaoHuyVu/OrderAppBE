package com.nhom23.orderapp.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;
@AllArgsConstructor
@Getter
public class ErrorResponse  {
    private String message;
    private HttpStatus status;
}
