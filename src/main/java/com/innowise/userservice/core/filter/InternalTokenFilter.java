package com.innowise.userservice.core.filter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

@Component
@RequiredArgsConstructor
public class InternalTokenFilter extends OncePerRequestFilter {

    private final String INTERNAL_HEADER_NAME = "x-internal-key";

    private final Set<String> PROTECTED_PATHS = Set.of(
        "/api/users/email",
        "/api/users/registration"
    );

    @Value("${internal.api-key}")
    private String internalApiKey;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {

        String path = request.getRequestURI();

        boolean protectedPath = PROTECTED_PATHS.contains(path);

        String requestKey = request.getHeader(INTERNAL_HEADER_NAME);

        if (protectedPath &&
            requestKey != null && requestKey.equals(internalApiKey)) {

            UsernamePasswordAuthenticationToken authentication =
                new UsernamePasswordAuthenticationToken(
                    "system",
                    null,
                    List.of(new SimpleGrantedAuthority("ROLE_INTERNAL"))
                );

            SecurityContextHolder.getContext().setAuthentication(authentication);
        }

        filterChain.doFilter(request, response);
    }
}
