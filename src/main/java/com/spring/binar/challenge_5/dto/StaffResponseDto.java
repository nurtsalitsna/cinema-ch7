package com.spring.binar.challenge_5.dto;

import lombok.Builder;
import lombok.Data;

import java.util.Date;

@Data
@Builder
public class StaffResponseDto {
    private int staffId;

    private String name;

    private String idCard;

    private String photoUrl;

    private UserResponseDTO userProfile;

    private Date lastUpdate;
}
