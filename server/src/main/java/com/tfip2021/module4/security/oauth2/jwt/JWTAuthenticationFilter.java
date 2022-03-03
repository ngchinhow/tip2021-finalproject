package com.tfip2021.module4.security.oauth2.jwt;

import java.io.IOException;

import javax.security.sasl.AuthenticationException;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.tfip2021.module4.models.DatabaseUser;
import com.tfip2021.module4.services.DatabaseUserService;
import com.tfip2021.module4.utils.HttpServletRequestUtils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.web.filter.OncePerRequestFilter;

public class JWTAuthenticationFilter extends OncePerRequestFilter {
    private static final Logger logger = LoggerFactory.getLogger(JWTAuthenticationFilter.class);
    @Autowired
    private JWTService jwtService;

    @Autowired
    private DatabaseUserService databaseUserService;

    @Override
    protected void doFilterInternal(
        HttpServletRequest request,
        HttpServletResponse response,
        FilterChain filterChain
    ) throws IOException, ServletException {
        try {
            DatabaseUser dbUser = HttpServletRequestUtils
                .getBearerToken(request)
                .flatMap(jwtService::getUserIdFromJWT)
                .flatMap(databaseUserService::getById)
                .orElseThrow(AuthenticationException::new);
            UsernamePasswordAuthenticationToken authentication =
                new UsernamePasswordAuthenticationToken(
                    dbUser, null, dbUser.getAuthorities()
                );
            authentication.setDetails(
                new WebAuthenticationDetailsSource().buildDetails(request)
            );
            SecurityContextHolder.getContext().setAuthentication(authentication);
        } catch (AuthenticationException e) {
            logger.error("Cannot authenticate user");
        }
        
        filterChain.doFilter(request, response);
    }
}
