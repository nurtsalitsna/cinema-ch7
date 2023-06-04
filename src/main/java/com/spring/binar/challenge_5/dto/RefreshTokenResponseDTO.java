package com.spring.binar.challenge_5.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class RefreshTokenResponseDTO {
    private String accessToken;
    private String refreshToken;
}
