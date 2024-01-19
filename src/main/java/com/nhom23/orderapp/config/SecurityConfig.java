package com.nhom23.orderapp.config;

import com.nhom23.orderapp.TsidUtils;
import com.nhom23.orderapp.model.ERole;
import com.nhom23.orderapp.security.jwt.AuthTokenFilter;
import com.nhom23.orderapp.security.jwt.AuthenticationEntryPointJwt;
import com.nhom23.orderapp.security.service.UserDetailsServiceImp;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.filter.OncePerRequestFilter;

@Configuration
@EnableMethodSecurity
@SuppressWarnings("unused")
public class SecurityConfig {

    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }
    @Bean
    public UserDetailsServiceImp userDetailsServiceImp(){
        return new UserDetailsServiceImp();
    }
    @Bean
    public DaoAuthenticationProvider daoAuthenticationProvider(){
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setUserDetailsService(userDetailsServiceImp());
        provider.setPasswordEncoder(passwordEncoder());
        return provider;
    }
    @Bean
    public AuthenticationManager authenticationManager(){
        return new ProviderManager(daoAuthenticationProvider());
    }
    @Bean
    public AuthenticationEntryPoint authenticationEntryPoint(){
        return new AuthenticationEntryPointJwt();
    }
    @Bean
    public FilterRegistrationBean<AuthTokenFilter> authTokenRegistrationBean(){
        return provideRegistrationBean(authTokenFilter());
    }

    @Bean
    public AuthTokenFilter authTokenFilter(){
        return new AuthTokenFilter();
    }
    private <T extends OncePerRequestFilter> FilterRegistrationBean<T> provideRegistrationBean(T filter) {
        FilterRegistrationBean<T> registrationBean = new FilterRegistrationBean<>(filter);
        registrationBean.setEnabled(false);
        return registrationBean;
    }
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests(auth ->
                        auth
                                .requestMatchers(
                                        "api/auth/**",
                                        "api/manager/login",
                                        "api/shipper/login",
                                        "admin/login"
                                        ).permitAll()
                                .requestMatchers("admin/**").hasAuthority(ERole.ROLE_ADMIN.name())
                                .requestMatchers("api/manager/**").hasAuthority(ERole.ROLE_MANAGER.name())
                                .requestMatchers("api/shipper/**").hasAuthority(ERole.ROLE_STAFF.name())
                                .requestMatchers("api/customer/**").hasAuthority(ERole.ROLE_USER.name())
                                .anyRequest().authenticated())
                .cors(AbstractHttpConfigurer::disable)
                .csrf(AbstractHttpConfigurer::disable)
                .exceptionHandling(exception ->
                        exception
                                .authenticationEntryPoint(authenticationEntryPoint()))
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authenticationManager(authenticationManager())
                .addFilterBefore(authTokenFilter(), UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }
}
