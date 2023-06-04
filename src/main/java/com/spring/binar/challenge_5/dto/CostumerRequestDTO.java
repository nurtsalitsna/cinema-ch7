package com.spring.binar.challenge_5.dto;

import com.spring.binar.challenge_5.models.Costumer;
import com.spring.binar.challenge_5.models.User;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CostumerRequestDTO {
    private String firstName;
    private String lastName;
    private String email;
    private String photoUrl;
    private Integer userId;

    public Costumer toCostumer(User user){
        return Costumer.builder()
                .firstName(this.getFirstName())
                .lastName(this.getLastName())
                .email(this.getEmail())
                .photoUrl(this.getEmail())
                .userProfile(user)
                .lastUpdate(System.currentTimeMillis())
                .build();
    }

}
