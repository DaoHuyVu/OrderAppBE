package com.nhom23.orderapp.security.jwt;

import com.nhom23.orderapp.security.service.UserDetailsImp;
import com.nhom23.orderapp.security.service.UserDetailsServiceImp;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
public class AuthTokenFilter extends OncePerRequestFilter {
    @Autowired
    private JwtUtil jwtUtil;
    @Autowired
    private UserDetailsServiceImp userDetailsServiceImp;

    private static final Logger logger = LoggerFactory.getLogger(AuthTokenFilter.class);
    private static final String AUTHORIZATION = "Authorization";
    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain) throws ServletException, IOException {
        String authHeader = request.getHeader(AUTHORIZATION);
        if(authHeader == null || !authHeader.startsWith("Bearer ")){
            filterChain.doFilter(request,response);
            return;
        }
        String accessToken = authHeader.substring("Bearer ".length());
        if(jwtUtil.validateToken(accessToken)){
            String email = jwtUtil.generateEmailFromToken(accessToken);
            UserDetailsImp userDetails = (UserDetailsImp) userDetailsServiceImp.loadUserByUsername(email);
            UsernamePasswordAuthenticationToken authentication =
                    new UsernamePasswordAuthenticationToken(
                            userDetails.toUserDto(),
                            userDetails.getPassword(),
                            userDetails.getAuthorities()
                    );
            authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
            SecurityContextHolder.getContext().setAuthentication(authentication);
            logger.info("{}", authentication);
            filterChain.doFilter(request,response);
        }

    }

}
