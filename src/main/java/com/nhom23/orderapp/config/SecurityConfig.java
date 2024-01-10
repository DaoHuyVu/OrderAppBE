package com.nhom23.orderapp.config;

import com.google.gson.Gson;
import com.nhom23.orderapp.security.jwt.AccessDeniedExceptionFilter;
import com.nhom23.orderapp.security.jwt.AuthTokenFilter;
import com.nhom23.orderapp.security.service.UserDetailsServiceImp;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
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
import org.springframework.security.web.access.intercept.AuthorizationFilter;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.filter.OncePerRequestFilter;

@Configuration
@EnableMethodSecurity(
        securedEnabled = true
)
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
    @Autowired
    @Qualifier("authEntryPointJwt")
    private AuthenticationEntryPoint authenticationEntryPoint;
    @Bean
    public FilterRegistrationBean<AuthTokenFilter> authTokenRegistrationBean(){
        return provideRegistrationBean(authTokenFilter());
    }
    @Bean
    public FilterRegistrationBean<AccessDeniedExceptionFilter> accessDeniedExceptionFilterRegistrationBean(){
        return provideRegistrationBean(accessDeniedExceptionFilter());
    }

    @Bean
    public AccessDeniedExceptionFilter accessDeniedExceptionFilter(){
        return new AccessDeniedExceptionFilter();
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
    @Order(1)
    public SecurityFilterChain authFilterChain(HttpSecurity http) throws Exception{
        http
                .securityMatcher(
                        "api/auth/**",
                        "/api/manager/login",
                        "/api/shipper/login",
                        "/admin/login",
                        "/admin/test")
                .authorizeHttpRequests(auth ->
                        auth.anyRequest().permitAll())
                .cors(AbstractHttpConfigurer::disable)
                .csrf(AbstractHttpConfigurer::disable)
                .authenticationManager(authenticationManager())
                .exceptionHandling(exception ->
                        exception
                                .authenticationEntryPoint(authenticationEntryPoint))
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS));

        return http.build();
    }
    @Bean
    @Order(2)
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests(auth -> auth.anyRequest().authenticated())
                .cors(AbstractHttpConfigurer::disable)
                .csrf(AbstractHttpConfigurer::disable)
                .exceptionHandling(exception ->
                        exception
                                .authenticationEntryPoint(authenticationEntryPoint))
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authenticationManager(authenticationManager())
                .addFilterBefore(authTokenFilter(), UsernamePasswordAuthenticationFilter.class)
                .addFilterBefore(accessDeniedExceptionFilter(), AuthorizationFilter.class);
        return http.build();
    }
}
