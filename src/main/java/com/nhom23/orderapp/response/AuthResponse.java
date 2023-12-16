package com.nhom23.orderapp.response;

import com.nhom23.orderapp.model.ERole;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;

@AllArgsConstructor
@Getter
public class AuthResponse implements Serializable {
    private String accessToken;
    private String userName;
    private List<String> role;
}
