package com.nhom23.orderapp.request;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class LoginRequest {
    @NotBlank
    @Size(min = 8, max = 25)
    private String userName;
    @NotBlank
    @Size(min = 8, max = 25)
    @Pattern(regexp = "[0-9a-zA-Z!@#$%^&*.]{8,}")
    private String password;
}
