package com.nhom23.orderapp.request;

import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.AllArgsConstructor;

@AllArgsConstructor
@Getter

public class SignUpRequest {
    @NotBlank
    @Size(min = 8, max = 25)
    private String userName;
    @NotBlank
    @Email
    private String email;
    @Size(min = 8, max = 25)
    @NotBlank
    @Pattern(regexp = "[0-9a-zA-Z.!@#$%^&*]{8,}")
    private String password;
}
