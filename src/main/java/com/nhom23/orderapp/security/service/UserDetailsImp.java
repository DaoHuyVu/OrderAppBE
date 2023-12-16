package com.nhom23.orderapp.security.service;

import com.nhom23.orderapp.dto.UserDto;
import com.nhom23.orderapp.model.ERole;
import com.nhom23.orderapp.model.Role;
import com.nhom23.orderapp.model.Account;
import com.nhom23.orderapp.model.AccountRole;
import com.nhom23.orderapp.repository.AccountRoleRepository;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Data
@AllArgsConstructor
public class UserDetailsImp implements UserDetails {

    private long id;
    private String email;
    private String password;
    private Boolean isEnable;
    private List<GrantedAuthority> authorities;
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return  authorities;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return email;
    }

    public List<String> getRoles(){
        return authorities.stream().map(GrantedAuthority::getAuthority).collect(Collectors.toList());
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return isEnable;
    }
    public UserDetailsImp (String email,Long id,String password,Boolean isEnable,List<ERole> roles){
        List<GrantedAuthority> authorities = roles
                .stream()
                .map(role -> new SimpleGrantedAuthority(role.name()))
                .collect(Collectors.toList());
        this.id = id;
        this.email = email;
        this.password = password;
        this.isEnable = isEnable;
        this.authorities = authorities;
    }
    public UserDto toUserDto(){
        return new UserDto(id,email);
    }
}
