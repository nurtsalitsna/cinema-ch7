package com.spring.binar.challenge_5.dto;

import lombok.Builder;
import lombok.Data;

import java.util.Date;

@Data
@Builder
public class CostumerResponseDto {

    private int costumerId;

    private String firstName;

    private String lastName;

    private String email;

    private String photoUrl;

    private UserResponseDTO userProfile;

    private Date lastUpdate;
}
