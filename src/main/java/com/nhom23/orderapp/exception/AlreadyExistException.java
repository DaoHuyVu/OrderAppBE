package com.nhom23.orderapp.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public class AlreadyExistException extends RuntimeException{
    private String message;
}
