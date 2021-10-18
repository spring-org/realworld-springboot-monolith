package com.example.realworld.core.config;

import com.example.realworld.core.security.filter.JwtAuthenticationFilter;
import com.example.realworld.core.security.filter.JwtAuthorizationFilter;
import com.example.realworld.core.security.jwt.JwtFactory;
import com.example.realworld.core.security.provider.JwtAuthenticationProvider;
import com.example.realworld.core.security.service.UserDetailsServiceImpl;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.servlet.http.HttpServletResponse;

import static org.springframework.http.HttpMethod.GET;
import static org.springframework.http.HttpMethod.POST;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private final UserDetailsServiceImpl userDetailsService;
    private final ObjectMapper objectMapper;
    private final JwtFactory jwtFactory;

    public SecurityConfig(JwtFactory jwtFactory, UserDetailsServiceImpl userDetailsService, ObjectMapper objectMapper) {
        this.jwtFactory = jwtFactory;
        this.userDetailsService = userDetailsService;
        this.objectMapper = objectMapper;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }


    @Bean
    public AuthenticationProvider authenticationProvider() {
        return new JwtAuthenticationProvider(userDetailsService, passwordEncoder());
    }

    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) {
        auth.authenticationProvider(authenticationProvider());
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .cors()
                .and()
                .csrf().disable()
                .formLogin().disable()
                .logout().disable()
                .headers().frameOptions().disable()
                .and()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);

        http
                .authorizeRequests()
                .antMatchers(POST, "/api/users/login", "/api/users").permitAll()
                .antMatchers(GET,
                        "/api/tags", "/api/articles", "/api/articles/*",
                        "/api/articles/feed", "/api/profiles/*", "/api/articles/*/comments"
                ).permitAll()
                .anyRequest().authenticated()
                .and()
                .addFilter(jwtAuthenticationFilter())
                .addFilter(new JwtAuthorizationFilter(authenticationManager(), userDetailsService, jwtFactory));

        http
                .exceptionHandling().authenticationEntryPoint((request, response, authException) -> {
                    response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "UnAuthorized");
                });
    }

    private JwtAuthenticationFilter jwtAuthenticationFilter() throws Exception {
        JwtAuthenticationFilter jwtAuthenticationFilter = new JwtAuthenticationFilter(authenticationManager(), jwtFactory, objectMapper);
        jwtAuthenticationFilter.setFilterProcessesUrl("/api/users/login");
        return jwtAuthenticationFilter;
    }
}
