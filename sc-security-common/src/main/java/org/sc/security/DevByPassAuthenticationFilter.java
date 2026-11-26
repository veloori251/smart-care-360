package org.sc.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

public class DevByPassAuthenticationFilter extends OncePerRequestFilter {

    private static final String DEFAULT_DEV_USER_ID = "00000000-0000-0000-0000-000000000001";

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        if(SecurityContextHolder.getContext().getAuthentication() == null){
            String headerUserId = request.getHeader("X-User-Id");
            String effectiveUserId = isValidUuid(headerUserId)
                    ?headerUserId.trim()
                    :DEFAULT_DEV_USER_ID;
            UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(
                   effectiveUserId,null, List.of(new SimpleGrantedAuthority("ROLE_USER"))
            );
            SecurityContextHolder.getContext().setAuthentication(auth);
        }
        filterChain.doFilter(request,response);
    }

    private boolean isValidUuid(String value) {
        if(value==null || value.isBlank()){
            return false;
        }
        try {
            UUID.fromString(value);
            return true;
        }catch (IllegalArgumentException e){
            return false;
        }
    }
}
