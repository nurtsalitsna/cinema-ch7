package com.spring.binar.challenge_5.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AuthenticationResponseDTO {
    private String username;
    private String phoneNumber;
    private String role;
    private String accessToken;
    private String refreshToken;
}
