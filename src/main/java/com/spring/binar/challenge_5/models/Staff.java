package com.spring.binar.challenge_5.models;

import com.spring.binar.challenge_5.dto.StaffResponseDto;
import jakarta.persistence.*;
import lombok.*;

import java.io.Serializable;
import java.util.Date;

@Entity
@Setter @Getter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name="staff", schema = "public")
public class Staff implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "staff_id", nullable = false, unique = true)
    private int staffId;

    @Column(name = "name")
    private String name;

    @Column(name = "id_card", nullable = false, unique = true)
    private String idCard;

    @Column(name = "photo_url")
    private String photoUrl;

    @OneToOne(targetEntity = User.class, cascade = CascadeType.MERGE)
    @JoinColumn(name = "user_id", referencedColumnName = "user_id", nullable = false)
    private User userProfile;

    @Column(name = "last_update")
    private long lastUpdate;
    public StaffResponseDto convertToStaffDto(){
        return StaffResponseDto.builder()
                .staffId(this.staffId)
                .idCard(this.idCard)
                .name(this.name)
                .photoUrl(this.photoUrl)
                .lastUpdate(new Date(this.lastUpdate))
                .userProfile(this.userProfile.convertToUserResponseDto())
                .build();
    }
}
