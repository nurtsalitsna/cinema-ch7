package com.spring.binar.challenge_5.models;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.spring.binar.challenge_5.dto.AuthenticationResponseDTO;
import com.spring.binar.challenge_5.dto.UserResponseDTO;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "user", schema = "public")
public class User implements UserDetails, Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id",nullable = false)
    private int userId;
    @Column(name = "username", length = 50, nullable = false, unique = true)
    private String username;
    @Column(name = "password", nullable = false)
    private String password;
    @Column(name = "phone_number", length = 13, nullable = false)
    private String phoneNumber;
    @Enumerated(EnumType.STRING)
    private Role role;

    @JsonBackReference
    @OneToMany(mappedBy = "user")
    private List<Token> tokens;


    public UserResponseDTO convertToUserResponseDto(){
        return UserResponseDTO.builder()
                .userId(this.userId)
                .username(this.username)
                .phoneNumber(this.phoneNumber)
                .role(role.name())
                .build();
    }

    public AuthenticationResponseDTO convertToAuthenticationResponseDto(String accessToken, String refreshToken){
        return AuthenticationResponseDTO.builder()
                .username(username)
                .phoneNumber(phoneNumber)
                .role(role.name())
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .build();
    }


    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority(role.name()));
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return username;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
