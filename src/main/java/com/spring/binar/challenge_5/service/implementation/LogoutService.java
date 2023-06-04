package com.spring.binar.challenge_5.service.implementation;

import com.spring.binar.challenge_5.exception.AuthException;
import com.spring.binar.challenge_5.repos.TokenRepository;
import com.spring.binar.challenge_5.utils.Constants;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.lang.NonNull;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.LogoutHandler;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class LogoutService implements LogoutHandler {

    private static final Logger logger = LoggerFactory.getLogger(LogoutService.class);
    private final TokenRepository tokenRepository;

    @Override
    public void logout(
            @NonNull HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull Authentication authentication
    ) {
        String jwt = "";
        final String requestUrl = request.getRequestURL().toString();

        // get session from mvc url to retrieve access token
        if(requestUrl.contains("/authLogout")){
            var apiAccess = request.getHeader(HttpHeaders.AUTHORIZATION);
            var userRequest = request.getSession().getAttribute(Constants.ACCESS_TOKEN);
            logger.info("Access token: {}", userRequest);
            logger.info("Api Access: {}", apiAccess);
            if(userRequest instanceof String) {
                jwt = (String) userRequest;
            }else if(apiAccess != null) {
                jwt = apiAccess.substring(7);
            }
        }

        logger.info("path info: {}", request.getRequestURL());

        // if request is from api
        if(requestUrl.contains("/api")){
            final String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
            if(authHeader == null || !authHeader.startsWith("Bearer ")){
                return;
            }
            jwt = authHeader.substring(7); // get token
        }

        var storedToken = (!jwt.isEmpty()) ? tokenRepository.findByToken(jwt).orElse(null) : null;

        if(storedToken != null){
            storedToken.setRevoked(true);
            storedToken.setExpired(true);
            tokenRepository.save(storedToken);
            SecurityContextHolder.clearContext();
        }else{
            throw new AuthException("Invalid token! your token may not exist in our server. Please try again");
        }

    }
}
