package com.spring.binar.challenge_5.dto;

import com.spring.binar.challenge_5.models.Role;
import com.spring.binar.challenge_5.models.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserRegisterDTO {

    private String username;
    private String password;
    private String phoneNumber;
    private String role;

    public User convertToUser(Role role, String psw){
        return User.builder()
                .username(this.username)
                .password(psw)
                .phoneNumber(this.phoneNumber)
                .role(role)
                .build();
    }

}
