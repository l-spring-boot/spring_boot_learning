package com.ig.spring_boot_learning.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.security.SignatureException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class AuthorizationFilter extends OncePerRequestFilter {

    private final JwtUtilProvider jwtProvider;

    @Override
    protected void doFilterInternal(HttpServletRequest request, @NonNull HttpServletResponse response, @NonNull FilterChain filterChain) throws ServletException, IOException {
        var requestTokenHeader = request.getHeader("Authorization");
        Claims claim = null;
        String jwtToken;
        String invalid = null;
        if (requestTokenHeader != null && requestTokenHeader.startsWith("Bearer ")) {
            jwtToken = requestTokenHeader.substring(7);
            try {
                claim = jwtProvider.getAllClaimsFromToken(jwtToken);
            } catch (IllegalArgumentException e) {
                invalid = "Invalid session";
            } catch (ExpiredJwtException e) {
                invalid = "Session has been expired";
            } catch (MalformedJwtException | SignatureException e) {
                invalid = e.getMessage();
            }
        }

        if (invalid != null) {
            request.setAttribute("invalid", invalid);
        } else {
            if (claim != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                /*
                  if token is valid configure Spring Security to manually set authentication */
                // Retrieve the authorities as List<?> first
                List<?> authoritiesList = claim.get("authorities", List.class);

                // Then map it to List<SimpleGrantedAuthority>
                List<SimpleGrantedAuthority> authorities = authoritiesList.stream()
                        .map(String.class::cast) // Cast each element to String
                        .map(SimpleGrantedAuthority::new) // Convert to SimpleGrantedAuthority
                        .toList();
                var usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(claim.getSubject(), request, authorities);
                /*
                  After setting the Authentication in the context, we specify
                  that the current user is authenticated. So it passes the
                  Spring Security Configurations successfully.
                 */
                SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
            }
        }
        filterChain.doFilter(request, response);
    }
}
