package com.spring.binar.challenge_5.models;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.*;

import java.io.Serializable;

@Entity
@Table(name = "token",schema = "public")
@AllArgsConstructor
@NoArgsConstructor
@Setter @Getter
@Builder
public class Token implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "token_id",nullable = false)
    private int tokenId;
    @Column(name = "token",nullable = false)
    private String token;
    @Column(name = "expired",nullable = false)
    private boolean expired = false;
    @Column(name = "revoke", nullable = false)
    private boolean revoked = false;

    @ManyToOne(targetEntity = User.class)
    @JoinColumn(name = "user_id", referencedColumnName = "user_id", nullable = false)
    @JsonManagedReference
    private User user;

}
