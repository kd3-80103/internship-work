package com.bootcampassignment.filter;

import com.bootcampassignment.util.JwtUtil;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.Collections;

public class JwtFilter extends OncePerRequestFilter {

    private final JwtUtil jwtUtil;

    public JwtFilter(JwtUtil jwtUtil) {
        this.jwtUtil = jwtUtil;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, FilterChain filterChain) throws ServletException, IOException {
        // Retrieve the JWT token from the Authorization header
        String jwtToken = request.getHeader("Authorization");

        if (jwtToken != null && jwtToken.startsWith("Bearer ")) {
            // Extract token by removing "Bearer " prefix
            String token = jwtToken.substring(7);  
            // Extract the username from the token
            String username = jwtUtil.extractUsername(token);  

            if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                // If the username is valid and no authentication exists, create a new Authentication token
                UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(username, null, Collections.emptyList());
                SecurityContextHolder.getContext().setAuthentication(authentication);  // Set the authentication in the context
            }
        }

        // Proceed with the next filter in the chain
        filterChain.doFilter(request, (ServletResponse) response);
    }
}
