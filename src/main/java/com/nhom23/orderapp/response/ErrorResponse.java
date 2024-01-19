package com.nhom23.orderapp.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;
@AllArgsConstructor
@Getter
@NoArgsConstructor
public class ErrorResponse  {
    private String message;
    private HttpStatus status;
}
