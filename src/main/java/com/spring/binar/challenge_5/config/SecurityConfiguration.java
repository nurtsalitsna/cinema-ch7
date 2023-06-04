package com.spring.binar.challenge_5.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.spring.binar.challenge_5.dto.LogoutResponseDTO;
import com.spring.binar.challenge_5.models.Role;
import com.spring.binar.challenge_5.security.JwtAuthEntryPoint;
import com.spring.binar.challenge_5.security.JwtAuthenticationFilter;
import com.spring.binar.challenge_5.service.implementation.LogoutService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.logout.LogoutHandler;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfiguration {

    private final JwtAuthenticationFilter jwtAuthFilter;
    private final AuthenticationProvider authProvider;
    private final JwtAuthEntryPoint authEntryPoint;
    private final LogoutHandler logoutHandler;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .csrf()
                .disable()
                .authorizeHttpRequests(request ->
                        request.requestMatchers("/swagger-ui/**","/v3/api-docs/**").permitAll()
                                .requestMatchers("/api/auth/**", "/error","/web-public/auth/**").permitAll()
                                .requestMatchers(HttpMethod.GET, "/api/schedule/**","/api/payment/**", "/api/film/**", "/api/user/*").authenticated()
                                .requestMatchers(HttpMethod.POST, "/api/payment").authenticated()
                                .requestMatchers("/web-public/schedule/**").authenticated()
                )
                .authorizeHttpRequests(request ->
                        request.requestMatchers(HttpMethod.GET, "/api/payment","/api/staff/*")
                                .hasAnyAuthority(Role.STAFF.name(), Role.ADMIN.name())
                )
                .authorizeHttpRequests(request ->
                        request.requestMatchers(HttpMethod.GET, "/api/costumer").hasAuthority(Role.ADMIN.name())
                                .requestMatchers("/api/costumer/**").hasAnyAuthority(Role.ADMIN.name(), Role.COSTUMER.name())
                )
                .authorizeHttpRequests(request -> request.requestMatchers("/api/**").hasAuthority(Role.ADMIN.name()))
                .authorizeHttpRequests(request -> request.anyRequest().authenticated())
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED)
                .and()
                .formLogin().loginPage("/web-public/auth/login-form")
                .and()
                .authenticationProvider(authProvider)
                .exceptionHandling()
                .authenticationEntryPoint(authEntryPoint)
                .and()
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class)
                .logout().invalidateHttpSession(true)
                .clearAuthentication(true)
                .logoutUrl("/authLogout")
                .addLogoutHandler(logoutHandler)
                .logoutSuccessUrl("/web-public/auth/login-form");
//                .logout().invalidateHttpSession(false)
//                .logoutUrl("/api/auth/logout")
//
//                .addLogoutHandler(logoutHandler)
//                .logoutSuccessHandler(((request, response, authentication) -> {
//                    new ObjectMapper().writeValue(response.getOutputStream(),
//                            LogoutResponseDTO
//                                    .builder()
//                                    .message("Logged out successfully")
//                                    .build()
//                    );
//                    SecurityContextHolder.clearContext();
//                }));

        return http.build();
    }



}
