package com.spring.binar.challenge_5.security;

import com.spring.binar.challenge_5.exception.AuthException;
import com.spring.binar.challenge_5.repos.TokenRepository;
import com.spring.binar.challenge_5.utils.Constants;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtService jwtService;
    private final UserDetailsService userDetailsService;
    private final TokenRepository tokenRepository;

    private static final Logger log = LoggerFactory.getLogger(JwtAuthenticationFilter.class);

    @Override
    protected void doFilterInternal(
            @NonNull HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull FilterChain filterChain
    ) throws ServletException, IOException {
        String jwt = "";
        String username = "";
        final String requestUrl = request.getRequestURL().toString();

        // get session from mvc url to retrieve access token
        if(requestUrl.contains("/web-public")){
            var userRequest = request.getSession().getAttribute(Constants.ACCESS_TOKEN);
            log.info("Access token: {}", userRequest);
            if(userRequest instanceof String) jwt = (String) userRequest;
        }

        log.info("path info: {}", request.getRequestURL());

        // if request is from api
        if(requestUrl.contains("/api")){
            final String authHeader = request.getHeader("Authorization");
            if(authHeader == null || !authHeader.startsWith("Bearer ")){
                filterChain.doFilter(request,response);
                return;
            }
            jwt = authHeader.substring(7); // get token
        }

        if(!jwt.isEmpty()) username = jwtService.extractUsername(jwt);

        if(!username.isEmpty() && SecurityContextHolder.getContext().getAuthentication() == null){
            UserDetails userDetails = this.userDetailsService.loadUserByUsername(username);
            boolean isTokenValid = tokenRepository.findByToken(jwt)
                    .map(t -> !t.isExpired() && !t.isRevoked())
                    .orElse(false);
            if(jwtService.isTokenValid(jwt, userDetails) && isTokenValid){
                logger.info("Token valid");
                UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                        userDetails,
                        null,
                        userDetails.getAuthorities()
                );
                authToken.setDetails(
                        new WebAuthenticationDetailsSource().buildDetails(request)
                );
                SecurityContextHolder.getContext().setAuthentication(authToken);
            }else if(!isTokenValid){
                throw new AuthException("Invalid token! your token may not exist in our server. Please try again");
            }
        }
        filterChain.doFilter(request, response);
    }
}
