package com.spring.binar.challenge_5.models;

import com.spring.binar.challenge_5.dto.CostumerResponseDto;
import jakarta.persistence.*;
import lombok.*;

import java.io.Serializable;
import java.util.Date;

@Entity
@Setter @Getter
@NoArgsConstructor
@Builder
@ToString
@AllArgsConstructor
@Table(name="costumer", schema = "public")
public class Costumer implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "costumer_id", nullable = false, unique = true)
    private int costumerId;

    @Column(name = "first_name")
    private String firstName;

    @Column(name = "last_name")
    private String lastName;

    @Column(name = "email")
    private String email;

    @Column(name = "photo_url")
    private String photoUrl;

    @OneToOne(targetEntity = User.class, cascade = CascadeType.MERGE)
    @JoinColumn(name = "user_id", referencedColumnName = "user_id", nullable = false)
    private User userProfile;

//    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSZ", shape = JsonFormat.Shape.STRING)
    @Column(name = "last_update")
    private Long lastUpdate;

    public CostumerResponseDto convertToCostumerDto(){
        return CostumerResponseDto.builder()
                .costumerId(this.costumerId)
                .email(this.email)
                .lastUpdate(new Date(this.lastUpdate))
                .firstName(this.firstName)
                .lastName(this.lastName)
                .userProfile(this.userProfile.convertToUserResponseDto())
                .photoUrl(this.photoUrl)
                .build();
    }

}
