package com.spring.binar.challenge_5.controller.rest;

import com.spring.binar.challenge_5.dto.AuthenticationRequestDTO;
import com.spring.binar.challenge_5.dto.UserRegisterDTO;
import com.spring.binar.challenge_5.service.UserService;
import com.spring.binar.challenge_5.utils.Constants;
import com.spring.binar.challenge_5.utils.ResponseHandler;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin
@RequiredArgsConstructor
@RequestMapping("/api/auth")
public class AuthController {

    private final UserService userService;

    @PostMapping("/login")
    public ResponseEntity<Object> authenticate(
            @RequestBody AuthenticationRequestDTO request
    ){
        return ResponseHandler.generateResponse(Constants.SUCCESS_RETRIEVE_MSG, HttpStatus.OK, userService.authentication(request));
    }

    @PostMapping("/register")
    public ResponseEntity<Object> register(
            @RequestBody UserRegisterDTO request
    ){
        return ResponseHandler.generateResponse(Constants.SUCCESS_RETRIEVE_MSG, HttpStatus.OK, userService.register(request));
    }

    @PostMapping("/refresh-token")
    public ResponseEntity<Object> refreshToken(
            HttpServletRequest request
    ){
        return ResponseHandler.generateResponse(Constants.SUCCESS_RETRIEVE_MSG, HttpStatus.OK, userService.refreshToken(request));
    }

}
