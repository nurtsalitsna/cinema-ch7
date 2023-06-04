package com.spring.binar.challenge_5.dto;

import com.spring.binar.challenge_5.models.Staff;
import com.spring.binar.challenge_5.models.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class StaffRequestDTO {
    private String name;
    private String idCard;
    private String photoUrl;
    private Integer userId;
    public Staff toStaff(User user){
        return Staff.builder()
                .name(name)
                .idCard(idCard)
                .photoUrl(photoUrl)
                .userProfile(user)
                .lastUpdate(System.currentTimeMillis())
                .build();
    }
}
