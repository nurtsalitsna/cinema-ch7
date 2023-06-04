package com.spring.binar.challenge_5.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UserResponseDTO {
    private int userId;
    private String username;
    private String phoneNumber;
    private String role;
}