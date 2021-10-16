package com.example.realworld.core.security.filter;

import com.example.realworld.application.users.dto.RequestLoginUser;
import com.example.realworld.core.security.jwt.JwtFactory;
import com.example.realworld.core.security.token.JwtAuthenticationToken;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.net.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class JwtAuthenticationFilter extends UsernamePasswordAuthenticationFilter {
    private static final String BEARER_PREFIX = "Bearer ";

    private final ObjectMapper mapper;
    private final JwtFactory jwtFactory;

    public JwtAuthenticationFilter(AuthenticationManager authenticationManager, JwtFactory jwtFactory, ObjectMapper mapper) {
        super(authenticationManager);
        this.jwtFactory = jwtFactory;
        this.mapper = mapper;
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        try {
            RequestLoginUser loginUser = mapper.readValue(request.getReader(), RequestLoginUser.class);
            JwtAuthenticationToken jwtAuthenticationToken = new JwtAuthenticationToken(loginUser.getEmail(), loginUser.getPassword());
            return super.getAuthenticationManager().authenticate(jwtAuthenticationToken);
        } catch (IOException e) {
            throw new AuthenticationServiceException("로그인 인증 처리 실패");
        }
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authResult) throws IOException {
        String principal = (String) authResult.getPrincipal();

        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setHeader(HttpHeaders.AUTHORIZATION, BEARER_PREFIX + jwtFactory.generateToken(principal, 1));
        response.getWriter();
    }
}
