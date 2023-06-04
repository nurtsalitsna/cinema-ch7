package com.spring.binar.challenge_5.service.implementation;

import com.spring.binar.challenge_5.dto.*;
import com.spring.binar.challenge_5.models.Token;
import com.spring.binar.challenge_5.models.User;
import com.spring.binar.challenge_5.repos.TokenRepository;
import com.spring.binar.challenge_5.security.JwtService;
import com.spring.binar.challenge_5.exception.UserErrorException;
import com.spring.binar.challenge_5.models.Role;
import com.spring.binar.challenge_5.repos.UserRepository;
import com.spring.binar.challenge_5.service.UserService;
import com.spring.binar.challenge_5.utils.Constants;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final TokenRepository tokenRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authManager;

    @Override
    public Page<UserResponseDTO> findAll(Pageable pageable) {
        return null;
    }

    @Override
    public List<UserResponseDTO> findAll() {
        return userRepository.findAll().stream()
                .map(User::convertToUserResponseDto)
                .toList();
    }

    @Override
    public AuthenticationResponseDTO authentication(AuthenticationRequestDTO request) {
        authManager.authenticate(
            new UsernamePasswordAuthenticationToken(
                    request.getUsername(),
                    request.getPassword()
            )
        );

        var user    = userRepository.findByUsername(request.getUsername()).orElseThrow(() -> new UserErrorException("User not found."));
        var claims  = new HashMap<String, Object>();
        claims.put("phoneNumber", user.getPhoneNumber());
        claims.put("role", user.getRole().name());

        var accessToken = jwtService.generateToken(claims,user);
        var refreshToken = jwtService.generateRefreshToken(claims,user);

        revokeAllUserTokens(user);
        saveUserToken(user,accessToken);


        return user.convertToAuthenticationResponseDto(accessToken, refreshToken);
    }

    @Override
    public String authentication(AuthenticationRequestDTO request, HttpSession session) {
        authManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getUsername(),
                        request.getPassword()
                )
        );

        var user    = userRepository.findByUsername(request.getUsername()).orElseThrow(() -> new UserErrorException("User not found."));
        var accessToken = jwtService.generateToken(user);
        var refreshToken = jwtService.generateRefreshToken(user);

        revokeAllUserTokens(user);
        saveUserToken(user,accessToken);

        if(session.getAttribute(Constants.ACCESS_TOKEN) != null || session.getAttribute(Constants.REFRESH_TOKEN) != null){
            session.removeAttribute(Constants.ACCESS_TOKEN);
            session.removeAttribute(Constants.REFRESH_TOKEN);
        }
        // set session to pass in next filter
        session.setAttribute(Constants.ACCESS_TOKEN, accessToken);
        session.setAttribute(Constants.REFRESH_TOKEN, refreshToken);

//        if(authToken.isAuthenticated()){
//            SecurityContextHolder.getContext().setAuthentication(authToken);
//            session.setAttribute(Constants.LOGGED_USER, userRepository.findByUsername(SecurityService.findLoggedInUsername()));
//        }

        return "redirect:/web-public/schedule/list";
    }

    @Override
    public AuthenticationResponseDTO register(UserRegisterDTO request) {

        if(request.getPassword() == null || request.getPhoneNumber() == null || request.getRole() == null || request.getUsername() == null)
            throw new UserErrorException("Input not valid! Please provide correct username, password, phone number and role");

        if(userRepository.existsByUsername(request.getUsername()))
            throw new UserErrorException("User already exists.");

        if(request.getUsername().isEmpty() || request.getPassword().isEmpty() || request.getPhoneNumber().isEmpty())
            throw new UserErrorException("Input is empty! Please fill with the correct username, password and phone number and role");

        if(request.getPhoneNumber().length() > 13 || request.getPhoneNumber().length() < 10)
            throw new UserErrorException("Invalid phone number, the length must be > 10 or < 14. Please provide a valid phone number");

        if(!isRoleValid(request.getRole().toUpperCase()))
            throw new UserErrorException("Role must be a valid role (COSTUMER, ADMIN, STAFF)");

        var user = request.convertToUser(getRole(request.getRole().toUpperCase()), passwordEncoder.encode(request.getPassword()));

        userRepository.save(user);
        var claims  = new HashMap<String, Object>();
        claims.put("phoneNumber", user.getPhoneNumber());
        claims.put("role", user.getRole().name());

        var accessToken = jwtService.generateToken(claims,user);
        var refreshToken = jwtService.generateRefreshToken(claims,user);

        saveUserToken(user,accessToken);

        return user.convertToAuthenticationResponseDto(accessToken, refreshToken);
    }

    @Override
    public AuthenticationResponseDTO update(UserUpdateRequestDTO updateUser) {
        return null;
    }

    @Override
    public RefreshTokenResponseDTO refreshToken(HttpServletRequest request) {
        final String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
        final String refreshToken;
        final String username;

        if(authHeader == null || !authHeader.startsWith("Bearer "))
            return null;

        refreshToken = authHeader.substring(7);
        username = jwtService.extractUsername(refreshToken);

        if(username != null){
            var user = userRepository.findByUsername(username).orElseThrow();
            if(jwtService.isTokenValid(refreshToken, user)){
                var claims  = new HashMap<String, Object>();
                claims.put("phoneNumber", user.getPhoneNumber());
                claims.put("role", user.getRole().name());
                var accessToken = jwtService.generateToken(claims,user);
                revokeAllUserTokens(user);
                saveUserToken(user, accessToken);

                return RefreshTokenResponseDTO
                        .builder()
                        .accessToken(accessToken)
                        .refreshToken(refreshToken)
                        .build();
            }

        }
        return null;
    }

    @Override
    public void delete(int id) {

    }

    /*
     * This will call when user generate a new token
     * So other token that already stored in database will not valid anymore
     * get access in service*/
    private void revokeAllUserTokens(User user) {
        var validUserTokens = tokenRepository.findAllValidTokenByUser(user.getUserId());
        if(validUserTokens.isEmpty()) return;

        validUserTokens.forEach(t -> {
            t.setExpired(true);
            t.setRevoked(true);
        });
        tokenRepository.saveAll(validUserTokens);
    }

    private void saveUserToken(User user, String accessToken) {
        var token = Token.builder().token(accessToken).user(user).build();
        tokenRepository.save(token);
    }

    private boolean isRoleValid(String role) {
        return Arrays.stream(Role.values()).anyMatch((r -> r == Role.valueOf(role)));
    }

    private Role getRole(String role) {
        return Role.valueOf(role);
    }
}
