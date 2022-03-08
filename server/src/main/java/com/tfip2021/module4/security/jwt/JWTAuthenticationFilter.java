package com.tfip2021.module4.security.jwt;

import java.io.IOException;
import java.util.Optional;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.tfip2021.module4.models.DatabaseUser;
import com.tfip2021.module4.services.model.DatabaseUserService;
import com.tfip2021.module4.utils.HttpServletRequestUtils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.web.filter.OncePerRequestFilter;

import lombok.extern.slf4j.Slf4j;
@Slf4j
public class JWTAuthenticationFilter extends OncePerRequestFilter {
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
        Optional<DatabaseUser> dbUser = HttpServletRequestUtils
            // Get literal bearer string
            .getBearerToken(request)
            // Parse JWT and get UserId
            .flatMap(jwtService::getUserIdFromJWT)
            // Retrieve user from database
            .flatMap(databaseUserService::getById);
        log.debug(dbUser.toString());
        if (dbUser.isPresent()) {
            DatabaseUser foundUser = dbUser.get();
            UsernamePasswordAuthenticationToken authentication =
                new UsernamePasswordAuthenticationToken(
                    foundUser, null, foundUser.getAuthorities()
                );
            authentication.setDetails(
                new WebAuthenticationDetailsSource().buildDetails(request)
            );
            SecurityContextHolder.getContext().setAuthentication(authentication);
        }
        
        filterChain.doFilter(request, response);
    }
}
