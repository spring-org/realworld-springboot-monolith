package com.example.realworld.security.filter;

import com.example.realworld.security.jwt.JwtFactory;
import com.example.realworld.security.token.JwtAuthenticationToken;
import org.apache.logging.log4j.util.Strings;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Objects;

public class JwtAuthorizationFilter extends BasicAuthenticationFilter {
    private static final String BEARER_PREFIX = "Bearer ";
    private static final String REPLACEMENT_EMPTY_DELIMITER = "";

    private final UserDetailsService userDetailsService;
    private final JwtFactory jwtFactory;

    public JwtAuthorizationFilter(AuthenticationManager authenticationManager, UserDetailsService userDetailsService, JwtFactory jwtFactory) {
        super(authenticationManager);
        this.userDetailsService = userDetailsService;
        this.jwtFactory = jwtFactory;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws IOException, ServletException {

        String header = request.getHeader(HttpHeaders.AUTHORIZATION);

        if (Strings.isEmpty(header)) {
            chain.doFilter(request, response);
            return;
        }

        if (header.startsWith(BEARER_PREFIX)) {
            String token = header.replace(BEARER_PREFIX, REPLACEMENT_EMPTY_DELIMITER);
            if (jwtFactory.isValidToken(token)) {
                String email = jwtFactory.extractEmail(token);
                UserDetails userDetails = userDetailsService.loadUserByUsername(email);

                if (Objects.nonNull(userDetails)) {
                    JwtAuthenticationToken jwtAuthenticationToken = new JwtAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                    SecurityContextHolder.getContext().setAuthentication(jwtAuthenticationToken);
                    chain.doFilter(request, response);
                }
            }
        }
    }
}
